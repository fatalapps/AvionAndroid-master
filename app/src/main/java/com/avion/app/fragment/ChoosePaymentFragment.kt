package com.avion.app.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.adapter.BankCardAdapter
import com.avion.app.adapter.viewholder.BankCardViewHolder
import com.avion.app.entity.BankCardEntity
import com.avion.app.fragment.viewmodel.BankCardViewModel
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekConfigs
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dlg.*
import kotlinx.android.synthetic.main.fragment_choose_payment.*
import ru.tinkoff.acquiring.sdk.*
import java.util.*


class ChoosePaymentFragment : NekFragment() {
    val own: LifecycleOwner = this
    override fun onCreateViewModel(): NekViewModel {
        mViewModel.nekFragment = this
        return mViewModel
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
        choosepayment_progressBar.visibility = View.GONE
        //choosepayment_pay_btn.visibility = View.VISIBLE
    }

    override fun onStartProgress() {
        choosepayment_progressBar.visibility = View.VISIBLE
        //choosepayment_pay_btn.visibility = View.INVISIBLE
    }

    private var totalPrice = 0.0

    private var bankCardAdapter: BankCardAdapter? = null
    private val mViewModel: MakeOrderViewModel by lazy { ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java) }
    // private val bankCardViewModel: BankCardViewModel by lazy { ViewModelProviders.of(activity!!).get(BankCardViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_payment, container, false)
    }

    private var isFromMenu: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bankCardAdapter = BankCardAdapter(activity!!)
        isFromMenu = true//arguments!!.getBoolean("isFromMenu", false)
        onStopProgress()
        bankCardViewModel = ViewModelProviders.of(activity!!).get(BankCardViewModel::class.java)
        bankCardViewModel.init()
        bankCardAdapter!!.setBankCardCallBack {
            MainActivity.logstr("BANKCARDCALLBACK TRIGGERED")

        }
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity!!)
        bankCardAdapter!!.setHasStableIds(true)
        linearLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        choosepayment_cardlist_recycleview.layoutManager = linearLayoutManager
        choosepayment_cardlist_recycleview.adapter = bankCardAdapter
        bankCardViewModel.bankCardRepository.refresh()
        MainActivity.logstr("REFRESHED")


        val itemTouchHelperCallback =
                object :
                        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                        if (!(viewHolder as BankCardViewHolder).ent.id.equals("cash") && !viewHolder.ent.id.equals("gpay") && !viewHolder.ent.id.equals("bonusp")) {
                            return super.getMovementFlags(recyclerView, viewHolder)
                        } else return ItemTouchHelper.Callback.makeMovementFlags(0, 0)
                    }

                    override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                    ): Boolean {

                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        // noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                        if ((viewHolder as BankCardViewHolder).ent.id.equals("cash") || viewHolder.ent.id.equals("bonusp")) {
                            return
                        }
                        lateinit var dialog: Dialog
                        val title = TextView(context!!)
                        title.text = getString(R.string.woring)
                        title.setBackgroundColor(Color.WHITE)
                        title.setTextColor(Color.BLACK)
                        title.textSize = (18).toFloat()
                        title.setTypeface(null, Typeface.BOLD)
                        title.setPadding(10, 15, 15, 5)
                        title.gravity = Gravity.CENTER
                        dialog = Dialog(activity!!, android.R.style.Theme_Dialog)
                        dialog.setContentView(R.layout.dlg)
                        dialog.setCanceledOnTouchOutside(true)

                        dialog.window!!.setBackgroundDrawable(
                                ColorDrawable(Color.TRANSPARENT))
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        dialog.changeBtn.text = getString(R.string.cancl)
                        dialog.changeBtn.setOnClickListener {
                            choosepayment_cardlist_recycleview.adapter = null
                            bankCardAdapter = BankCardAdapter(activity!!)
                            bankCardAdapter!!.setHasStableIds(true)
                            choosepayment_cardlist_recycleview.adapter = bankCardAdapter

                            bankCardViewModel.cardList.observe(own, Observer {

                                bankCardAdapter!!.submitList(it)
                                bankCardAdapter!!.notifyDataSetChanged()
                            })
                            dialog.hide()
                        }
                        dialog.sendBtn.text = getString(R.string.delt)
                        dialog.sendBtn.setTextColor(Color.RED)
                        dialog.sendBtn.setOnClickListener {
                            //choosepayment_cardlist_recycleview.removeView((viewHolder as BankCardViewHolder).itemv)
                            MainActivity.logstr("CARD VIEW REMOVED")
                            removeCard(NekConfigs.getUserID(), viewHolder.ent.id, bankCardAdapter!!, bankCardViewModel, viewHolder)
                            // bankCardAdapter.notifyDataSetChanged()
                            bankCardAdapter = BankCardAdapter(activity!!)
                            bankCardAdapter!!.setHasStableIds(true)
                            choosepayment_cardlist_recycleview.adapter = bankCardAdapter

                            bankCardViewModel.cardList.observe(own, Observer {

                                bankCardAdapter!!.submitList(it)
                                bankCardAdapter!!.notifyDataSetChanged()
                                if (viewHolder.ent.id.equals(NekConfigs.paymethod)) {
                                    NekConfigs.paymethod = "cash"
                                }
                            })
                            dialog.hide()
                        }
                        dialog.phone_confirm.text = getString(R.string.delCard) + "\n" + viewHolder.bankcard_num_txtview.text
                        dialog.show()


                    }

                }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(choosepayment_cardlist_recycleview)


        bankCardViewModel.cardList.observe(this, Observer {

            MainActivity.logstr("LEN: " + it.size)

            bankCardAdapter!!.submitList(it)
        })

        choosepayment_attach_btn.setOnClickListener {
            var email = ""
            if (NekConfigs.getUserEmail().trim().length > 1) email = NekConfigs.getUserEmail().trim()
            //Navigation.findNavController(view).navigate(R.id.action_choosePaymentFragment_to_addPaymentFragment)
            AttachCardFormActivity
                    .init(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey) // данные продавца
                    .prepare(
                            NekConfigs.getUserID(),                         // уникальный ID пользователя для сохранения данных его карты
                            CheckType.THREE_DS,                     // тип привязки карты
                            true,                                  // флаг использования безопасной клавиатуры
                            email)
                    .setTheme(R.style.AcquiringTheme_Custom)
                    .startActivityForResult(this.activity, 33)
            MainActivity.attached_live.observe(own, Observer {
                if (it) {
                    MainActivity.logstr("RELOAD_ON_ATTACH")
                    bankCardViewModel.reload()
                    bankCardAdapter!!.notifyDataSetChanged()
                    MainActivity.attached_live.value = false
                }
            })


        }

        viewN = view


    }

    companion object {
        lateinit var bankCardViewModel: BankCardViewModel
        lateinit var viewN: View
        val isPayOk: MutableLiveData<Boolean> = MutableLiveData()

        @JvmStatic
        fun reloadDataFromActivty(requestCode: Int, resultCode: Int) {
            MainActivity.logstr(requestCode.toString() + " " + resultCode)
            if (requestCode == 33) {
                MainActivity.logstr(requestCode.toString() + " CODE33 " + resultCode)


            }
            if (requestCode == 22) {
                if (resultCode == Activity.RESULT_OK) {
                    bankCardViewModel.reload()

                } else {
                    Snackbar.make(viewN, R.string.error_add_card, Snackbar.LENGTH_LONG).show()
                }
            } else if (requestCode == 11) {
                if (resultCode == Activity.RESULT_OK) {
                    isPayOk.value = true
                } else {
                    isPayOk.value = false
                    Snackbar.make(viewN, R.string.paying_error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Nek", "onActivityResult")
        Logger.d("On activity result")
        if (requestCode == 22) {
            if (resultCode == Activity.RESULT_OK) {
                Logger.d("Card saved")
            } else {
                Logger.e("Save card error")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        if (isFromMenu)
            return


        // totalPrice += mViewModel.makeOrderLiveData.value!!.tarif!!.price.toDouble()

        // mViewModel.setPrice(totalPrice)

        // val paymentPrice = Math.round(((totalPrice * 20) / 100))

        // choosepayment_pay_btn.text = getString(R.string.pay_) + " " + paymentPrice + " " + getString(R.string.rub)

    }

    private fun makeOrder() {
        mViewModel.makeOrder().observe(this, Observer {

            if (it == null)
                return@Observer
            val bundle = Bundle()
            bundle.putString("order_id", it)
            Navigation.findNavController(view!!).navigate(R.id.action_choosePaymentFragment_to_orderInfoFragment, bundle)
        })
    }

    fun removeCard(customerKey: String, cardId: String, adapter: BankCardAdapter, viewModel: BankCardViewModel, viewHolder: BankCardViewHolder) {
        doAsync {

            var sdk: AcquiringSdk = AcquiringSdk(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey)
            AcquiringSdk.setDebug(true)
            try {
                sdk.removeCard(customerKey, cardId)
            } catch (e: Exception) {
                MainActivity.logstr("CARD_DELETE_ERROR")
            }
            var cards: Array<Card?>? = null
            sdk = AcquiringSdk(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey)
            var cardManager: CardManager = CardManager(sdk)
            //cardManager.getCardById("").
            Completable.fromAction {
                cardManager.clear(NekConfigs.getUserID())
                cards = cardManager.getActiveCards(NekConfigs.getUserID())
            }
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {}
                        override fun onComplete() {
                            Logger.d("Done work from delete " + cards!!.size)
                            val bankCardEntityList: MutableList<BankCardEntity> = ArrayList()
                            val cash = BankCardEntity()
                            cash.id = "cash"
                            cash.card_num = "cash"
                            bankCardEntityList.add(cash)
                            for (activeCard in cards!!) {
                                val bankCardEntity = BankCardEntity()
                                bankCardEntity.card_num = activeCard!!.pan
                                bankCardEntity.id = activeCard.cardId
                                bankCardEntityList.add(bankCardEntity)
                                MainActivity.logstr("NUM " + activeCard.pan)
                            }
                            bankCardViewModel.delete(viewHolder.ent)

                        }

                        override fun onError(e: Throwable) {
                            Logger.e(e.toString())
                        }
                    })
            MainActivity.logstr("CARD WAS  REMOVED FROM DB")


        }.execute()
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }

    fun reloadRecycler() {
        bankCardViewModel.cardList.observe(own, Observer {
            val en = BankCardEntity()
            en.card_num = "cash"
            en.id = "cash"
            MainActivity.logstr("LEN: " + it.size)

            bankCardAdapter!!.submitList(it)
            bankCardAdapter!!.notifyDataSetChanged()
        })
    }
}

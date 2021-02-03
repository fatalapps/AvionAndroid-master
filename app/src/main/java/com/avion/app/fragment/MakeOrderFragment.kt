package com.avion.app.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avion.app.LinearLayoutManagerWithSmoothScroller
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.action.MakeOrder
import com.avion.app.action.Option
import com.avion.app.adapter.TarifAdapter
import com.avion.app.entity.Currency
import com.avion.app.fragment.viewmodel.BankCardViewModel
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.models.Payment
import com.avion.app.models.Tarif
import com.avion.app.models.TarifObj
import com.avion.app.repository.TinkoffApi
import com.avion.app.unit.NekConfigs
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.wallet.WalletConstants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.common.hash.Hashing
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_make_order.*
import org.json.JSONArray
import org.json.JSONObject
import ru.tinkoff.acquiring.sdk.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList


class MakeOrderFragment : NekFragment() {

    private var makeOrderViewModel: MakeOrderViewModel? = null
    val tarifsList = ArrayList<TarifObj>()
    var cur_pos = 0
    var old_currency: Currency = Currency()
    var to_str: String? = null
    var from_str: String? = null
    var counted: Boolean = false
    var do_count: Boolean = false
    var nekk: Boolean = false
    var regionsJson: JSONArray? = JSONArray()
    private val makeOrdeerObserver = Observer<MakeOrder> {
        assert(it != null)
        makeorder_extra_parkingprice_textview.visibility = View.GONE
        makeorder_select_from_clear_imagebtn.visibility = View.INVISIBLE
        makeorder_select_to_clear_imagebtn.visibility = View.INVISIBLE
        if (it!!.date != null && it.time != null)
            makeorder_extra_time_textview.setData("${it.date?.replace("/", ".")} ${it.time}")
        else
            makeorder_extra_time_textview.hideLabel()
        if (it.parking_price !== null && it.parking_price != 0.0) {
            MainActivity.logstr(it.parking_price.toString() + " PARK_PRICE")
            makeorder_extra_parkingprice_textview.setData("${it.parking_price} руб/час")
        } else
            makeorder_extra_parkingprice_textview.hideLabel()
        if (it.from != null) {
            makeorder_select_from_clear_imagebtn.visibility = View.VISIBLE
            makeorder_select_from_textview.text = it.from!!.fullAddress
            // Toast.makeText(context!!.applicationContext,it.from!!.fullAddress, Toast.LENGTH_SHORT).show()
            //  Toast.makeText(context!!.applicationContext, makeorder_select_from_textview.text.toString()+""+makeorder_select_to_textview.text.toString()+" "+from_str+" "+to_str , Toast.LENGTH_SHORT).show()

            from_str = it.from!!.fullAddress
            if ((ChooseTimeFragment.txtAddressTypeToInt(it.from?.type!!) == ChooseTimeFragment.TYPE_AIRPORT
                            || ChooseTimeFragment.txtAddressTypeToInt(it.from?.type!!) == ChooseTimeFragment.TYPE_TRAIN)
                    && it.options != null && it.options!!.meeting_with_a_table != null) {
                makeorder_extra_parkingprice_textview.visibility = View.VISIBLE
            } else {
                makeorder_extra_parkingprice_textview.visibility = View.GONE
            }

        } else {
            if (old_currency.id != null && old_currency.value != null) {
                if (old_currency.id > 0) {
                    makeOrderViewModel!!.newApi.liveCurrency.setValue(old_currency.value.toFloat())
                    makeOrderViewModel!!.newApi.liveCurrency.id = (old_currency.id)
                    MainActivity.currency = Currency(old_currency.id, old_currency.value.toFloat())
                }
                old_currency = Currency()
            }
            makeorder_select_from_textview.text = getString(R.string.go_place)
        }
        if (it.to != null) {
            // Toast.makeText(context!!.applicationContext, makeorder_select_from_textview.text.toString()+""+makeorder_select_to_textview.text.toString()+" "+from_str+" "+to_str , Toast.LENGTH_SHORT).show()

            // Toast.makeText(context!!.applicationContext,it.to!!.fullAddress, Toast.LENGTH_SHORT).show()
            makeorder_select_to_clear_imagebtn.visibility = View.VISIBLE
            makeorder_select_to_textview.text = it.to!!.fullAddress
            to_str = it.to!!.fullAddress
        } else
            makeorder_select_to_textview.text = getString(R.string.end_place)

        makeorder_btn.isEnabled = it.from != null && it.to != null

        if (it.options == null)
            makeorder_extra_options_textview.hideLabel()
        else
            displayOptions(it.options!!)

        GlobalOrderEntity = it
        if (it.to != null && it.from != null) {
            nekk = true
            if (!counted) {
                count_trans(false)
                counted = true
            }
        } else {
            counted = false
            nekk = false
            //  Toast.makeText(context!!.applicationContext, "NO_COUNT", Toast.LENGTH_SHORT).show()
            if ((it.to != null && it.from == null) || (it.to == null && it.from != null)) {
                //  Toast.makeText(context!!.applicationContext, "REFRESH", Toast.LENGTH_SHORT).show()
                makeorder_tafirlist_recycleview.post(Runnable {
                    @Override
                    fun run() {
                        count_trans(true)

                    }
                })
                //onStopProgress()


            }
        }
    }

    private lateinit var tarifAdapter: TarifAdapter

    private val tarifInfoFragment: TarifInfoFragment by lazy { TarifInfoFragment.getInstance(this.fragmentManager!!) }
    private fun clearPrices() {
        var listt: ArrayList<TarifObj> = java.util.ArrayList()
        tarifsList.forEach {
            var obj: TarifObj = it
            obj.price = "-1"

        }
        tarifAdapter.setTarifObjList(listt)
    }

    private fun displayOptions(options: Option) {
        var optionsTxt = ""
        var count_o = 0
        if (options.meeting_with_a_table != null && options.meeting_with_a_table!!.isNotEmpty()) {
            //makeorder_extra_parkingprice_textview.visibility = View.GONE
            optionsTxt += getString(R.string.meeting_with_a_table) + " "
            count_o++
        }
        if (options.child_seat.booster_seat > 0 || options.child_seat.child_safety_seat > 0) {
            optionsTxt += getString(R.string.child_seat) + " "
            count_o++
        }
        if (options.child_seat.car_cradle > 0) count_o++
        if (options.car_with_yellow_plates) {
            optionsTxt += getString(R.string.car_with_yellow_plates) + " "
            count_o++
        }
        if (options.driver_language.english || options.driver_language.german || options.driver_language.spain) {
            optionsTxt += getString(R.string.driver_language) + " "
            count_o++
        }
        if (options.smoking_salon) {
            optionsTxt += getString(R.string.smoking_salon) + " "
            count_o++
        }
        if (options.pet_transportation) {
            optionsTxt += getString(R.string.pet_transportation) + " "
            count_o++
        }
        if (options.oversized_baggage) {
            optionsTxt += getString(R.string.oversized_baggage) + " "
            count_o++
        }
        if (options.invoice_for_company) {
            optionsTxt += getString(R.string.invoice_for_company)
            count_o++
        }

        if (count_o == 0) {
            makeorder_extra_options_textview.hideLabel()
            makeorder_extra_options_textview.hideOptions()
        } else {
            makeorder_extra_options_textview.setData(getString(R.string.selected))
            makeorder_extra_options_textview.setOptions(count_o)
        }
    }

    private fun calkChossenOptionsPrices(): Int {
        var totalPrice = 0
        val options = makeOrderViewModel!!.makeOrderLiveData.value!!.options!!
        val dopUslugi = makeOrderViewModel!!.getInfoRegion.value!!.dopUslugi
        if (options.invoice_for_company) {
            totalPrice += dopUslugi.bso.price
        }
        if (options.oversized_baggage) {
            totalPrice += dopUslugi.baggage.price
        }
        if (options.pet_transportation) {
            totalPrice += dopUslugi.animal.price
        }
        if (options.smoking_salon) {
            totalPrice += dopUslugi.smoking.price
        }
        if (options.car_with_yellow_plates) {
            totalPrice += dopUslugi.yellowNumbers.price
        }
        if (options.driver_language.spain || options.driver_language.german || options.driver_language.english) {
            totalPrice += dopUslugi.language.price
        }
        if (options.meeting_with_a_table != null && options.meeting_with_a_table!!.isNotEmpty()) {
            totalPrice += dopUslugi.vstrecha.price
        }
        if (options.child_seat.child_safety_seat > 0 || options.child_seat.booster_seat > 0) {
            totalPrice += dopUslugi.kreslo.price * (options.child_seat.child_safety_seat + options.child_seat.booster_seat)
        }
        if (options.child_seat.car_cradle > 0) {
            totalPrice += dopUslugi.lulka.price * options.child_seat.car_cradle

        }
        return totalPrice
    }

    override fun onCreateViewModel(): NekViewModel {
        if (makeOrderViewModel == null) {
            makeOrderViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
            makeOrderViewModel!!.nekFragment = this
        }

        return makeOrderViewModel!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_make_order, container, false)
    }

    override fun onViewCreated(root_view: View, savedInstanceState: Bundle?) {
        //Toast.makeText(context!!.applicationContext, makeorder_select_from_textview.text.toString()+""+makeorder_select_to_textview.text.toString()+" "+from_str+" "+to_str , Toast.LENGTH_SHORT).show()
        makeorder_extra_paytype_textview.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isFromMenu", false)
            Navigation.findNavController(view!!).navigate(R.id.choosePaymentFragment, bundle)
            //else Toast.makeText(context, "Выберите маршрут!", Toast.LENGTH_SHORT).show()
        }
        makeorder_btn.isEnabled = false
        makeorder_select_from_cardview.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("address_type", PickUpAddressFragment.FROM)
            Navigation.findNavController(view!!).navigate(R.id.action_makeOrderFragment_to_pickUpAddressFragment, bundle)
        }
        makeorder_select_to_cardview.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("address_type", PickUpAddressFragment.TO)
            Navigation.findNavController(view!!).navigate(R.id.action_makeOrderFragment_to_pickUpAddressFragment, bundle)
        }
        if (GlobalOrderEntity != null) {
            if (GlobalOrderEntity?.from != null) {
                makeorder_extra_time_textview.setOnClickListener { Navigation.findNavController(root_view).navigate(R.id.action_makeOrderFragment_to_chooseTimeFragment) }
            } else {
                makeorder_extra_time_textview.setOnClickListener { Toast.makeText(context, getString(R.string.choose_from), Toast.LENGTH_SHORT).show() }
            }
        } else makeorder_extra_time_textview.setOnClickListener { Toast.makeText(context, getString(R.string.choose_from), Toast.LENGTH_SHORT).show() }

        makeorder_extra_options_textview.setOnClickListener { Navigation.findNavController(root_view).navigate(R.id.action_makeOrderFragment_to_chooseOptionsFragment) }

        makeorder_region_textview.setOnClickListener {
            //clearPrices()
            Navigation.findNavController(root_view).navigate(R.id.action_makeOrderFragment_to_chooseRegionFragment)
        }
        tarifAdapter = TarifAdapter()

        MainActivity.chooseRegionLiveData.observe(this, Observer {
            if (it != null) {

                makeorder_region_textview.text = it.name
                if (!it.name.equals(MainActivity.last_reg)) {
                    clearPrices()
                    Logger.e("START_PRAGRESS " + it.name)
                    onStartProgress()
                    //makeOrderViewModel!!.getInfoRegion.observe(this, Observer<GetInfoRegion> { })
                    makeOrderViewModel!!.setAddressFrom(null)
                    makeOrderViewModel!!.setAddressTo(null)
                    makeOrderViewModel!!.setDate(null)
                    makeOrderViewModel!!.setTime(null)
                    makeOrderViewModel!!.setDepartingFrom(null)
                    makeOrderViewModel!!.setOptions(Option())
                    //makeOrderViewModel!!.makeOrderLiveData.value!!.options = null
                    MainActivity.last_reg = it.name
                }//else   tarifAdapter.setTarifObjList(tarifsList)

            }

        })

        makeorder_tafirlist_recycleview.adapter = tarifAdapter
        val lm = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        makeorder_tafirlist_recycleview.layoutManager = lm
        val smoothScroller: RecyclerView.SmoothScroller = LinearLayoutManagerWithSmoothScroller(makeorder_tafirlist_recycleview.context)
        var cwidth = 0
        smoothScroller.targetPosition = 0
        lm.startSmoothScroll(smoothScroller)

        tarifAdapter.setOnTafif(object : TarifAdapter.OnTafif {
            override fun onSelect(tarifObj: TarifObj) {
                try {
                    makeOrderViewModel!!.setTarif(tarifObj)

                    if (cwidth == 0) cwidth = makeorder_tafirlist_recycleview.getChildAt(0).width
                    val centerOfScreen = makeorder_tafirlist_recycleview.width / 2 - (cwidth + convertDpToPixel((8).toFloat(), context!!.applicationContext)) / 2
                    var i1 = 0

                    tarifsList.forEach {
                        if (it.name.equals(MainActivity.curr_itemm)) {
                            //makeorder_tafirlist_recycleview.smoothScrollToPosition(i1)
                            smoothScroller.targetPosition = i1

                            lm.startSmoothScroll(smoothScroller)
                            //lm.scrollToPositionWithOffset(i1, centerOfScreen.toInt())
                            //Toast.makeText(context,lm.findViewByPosition(i1+3)!!.rootView.findViewById<TextView>(R.id.car_name_txtview).text,Toast.LENGTH_SHORT).show()
                            Log.e("OFFSET", centerOfScreen.toString())
                        }
                        i1++
                    }
                } catch (e: Exception) {
                    Logger.e("ON_SELECT_EXC:\r\n" + e.toString())
                }

                //  makeorder_tafirlist_recycleview.scrollToPosition(2)

                // makeorder_btn.isEnabled = true

            }

            override fun onInfo(tarifObj: TarifObj) {
                var i2 = 0
                var pos = 0

                tarifsList.forEach {
                    if (it.name.equals(MainActivity.curr_itemm)) pos = i2
                    i2++
                }
                // tarifInfoFragment.show(tarifObj, fragmentManager!!)
                //MainActivity.logstr(tarifsList.get(0).crm_name+" "+tarifObj.crm_name)
                val recycleinfo = TarifRecyclerInfo.newInstance(tarifsList, pos, lm, smoothScroller, makeorder_tafirlist_recycleview, tarifAdapter)
                if (fragmentManager!!.findFragmentByTag("add_tarrifrecycler_info") == null)
                    recycleinfo.show(fragmentManager!!, "add_tarrifrecycler_info")
            }
        })

        makeorder_select_from_clear_imagebtn.setOnClickListener {
            makeOrderViewModel!!.setCurrency()
            var clear_ob = false
            if (old_currency.id != null && old_currency.value != null) {
                if (old_currency.id > 0) {
                    makeOrderViewModel!!.newApi.liveCurrency.setValue(old_currency.value.toFloat())
                    makeOrderViewModel!!.newApi.liveCurrency.id = (old_currency.id)
                    MainActivity.currency = Currency(old_currency.id, old_currency.value.toFloat())
                }
            }
            makeOrderViewModel!!.setAddressFrom(null)
            makeorder_extra_time_textview.setOnClickListener { Toast.makeText(context, getString(R.string.choose_from), Toast.LENGTH_SHORT).show() }
            makeOrderViewModel!!.setCarToTime(false)
            val reg = MainActivity.chooseRegionLiveData.value!!.id
            try {
                MainActivity.logstr("Region: " + reg)
                makeOrderViewModel!!.regionsFromModel
                MainActivity.logstr("Region: " + reg + " \r\n")
            } catch (e: Exception) {
                MainActivity.logstr("EXCEPTION: \r\n" + e.toString())
            }
            // Toast.makeText(context!!.applicationContext, "CLICKED"+tarifsList.get(0).price, Toast.LENGTH_LONG).show()

            makeOrderViewModel!!.getInfoRegion.observe(this, Observer {
                if (it == null)
                    return@Observer
                if (!clear_ob) {
                    setTarifPrices(it.tarrifs, false, true)
                    clear_ob = true
                }
                //onStopProgress()

            })

        }
        makeorder_select_to_clear_imagebtn.setOnClickListener {
            var clear_ob2 = false
            if (old_currency.id != null && old_currency.value != null) {
                if (old_currency.id > 0) {
                    makeOrderViewModel!!.newApi.liveCurrency.setValue(old_currency.value.toFloat())
                    makeOrderViewModel!!.newApi.liveCurrency.id = (old_currency.id)
                    MainActivity.currency = Currency(old_currency.id, old_currency.value.toFloat())
                }
            }
            makeOrderViewModel!!.setAddressTo(null)
            makeOrderViewModel!!.getInfoRegion.observe(this, Observer {
                if (it == null)
                    return@Observer
                if (!clear_ob2) {
                    setTarifPrices(it.tarrifs, false, true)
                    clear_ob2 = true
                }
                //onStopProgress()

            })
        }



        makeOrderViewModel!!.getInfoRegion.observe(this, Observer {

            if (it == null)
                return@Observer

            MainActivity.logstr(it.tarrifs.econom.min.toString() + " econom")
            setTarifPrices(it.tarrifs, false, false)
            if (nekk) count_trans(false)
            //onStopProgress()

        })

        if (arguments !== null) {
            Log.d("Nek", "arguments is not null")
            val serializable = arguments!!.getSerializable("last_order")
            if (serializable !== null) {
                Log.d("Nek", "serializable is not null")

                old_currency.id = MainActivity.currency.id
                old_currency.setValue(MainActivity.currency.value.toFloat())
                Logger.e("OLD_CURR:" + old_currency.id)
                makeOrderViewModel!!.setMakeOrder(serializable as? MakeOrder)

                MainActivity.currency.setValue(makeOrderViewModel!!.makeOrderLiveData!!.value!!.payment!!.currencyValue.toFloat())
                MainActivity.currency.id = makeOrderViewModel!!.makeOrderLiveData!!.value!!.payment!!.currencyType.toInt()
                Logger.e("OLD_CURR_NEW:" + old_currency.id)

            }
            arguments = null
        }
        if (!NekConfigs.paymethod.equals("cash")) {
            makeorder_extra_paytype_textview.setData(NekConfigs.last4)
            val density: Float = resources.displayMetrics.density
            makeorder_extra_paytype_textview.setIcon(resources.getDrawable(NekConfigs.paylogo))
        } else {
            makeorder_extra_paytype_textview.setData(resources.getString(R.string.nalicknie))
            makeorder_extra_paytype_textview.setIcon(resources.getDrawable(R.drawable.ic_money))

        }
    }

    companion object {
        var GlobalOrderEntity: MakeOrder? = null
        lateinit var bankCardViewModel: BankCardViewModel
        lateinit var viewN: View
        val isPayOk: MutableLiveData<Boolean> = MutableLiveData()

        @JvmStatic
        fun reloadDataFromActivty(requestCode: Int, resultCode: Int) {
            MainActivity.logstr(requestCode.toString() + " " + resultCode)
            if (requestCode == 22) {
                if (resultCode == Activity.RESULT_OK) {
                    bankCardViewModel.reload()
                } else {
                    Snackbar.make(viewN, R.string.error_add_card, Snackbar.LENGTH_LONG).show()
                }
            } else if (requestCode == 11) {
                MainActivity.logstr("PAYMENT")
                if (resultCode == Activity.RESULT_OK) {
                    isPayOk.value = true
                } else {
                    isPayOk.value = false
                    Snackbar.make(viewN, R.string.paying_error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Logger.d("onStop")
        makeOrderViewModel!!.makeOrderLiveData.removeObserver(makeOrdeerObserver)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard(activity!!)
        Logger.d("onStart")
        makeOrderViewModel!!.nekFragment = this
        makeOrderViewModel!!.makeOrderLiveData.observe(this, makeOrdeerObserver)
        //Toast.makeText(context!!.applicationContext, makeorder_select_from_textview.text.toString()+""+makeorder_select_to_textview.text.toString()+" "+from_str+" "+to_str , Toast.LENGTH_SHORT).show()

        makeorder_btn.setOnClickListener {
            count_trans(false)
        }

        if (MainActivity.trans_paid) {
            MainActivity.trans_paid = false
            if (makeOrderViewModel!!.makeOrderLiveData.value!!.date == null && makeOrderViewModel!!.makeOrderLiveData.value!!.time == null) {
                Snackbar.make(makeorder_btn, R.string.select_pick_up_time, Snackbar.LENGTH_LONG).show()
            } else {
                val currentUser = FirebaseAuth.getInstance().currentUser
                //Toast.makeText(context!!.applicationContext, MainActivity.chooseRegionLiveData!!.value!!.id.toString(), Toast.LENGTH_SHORT).show();
                try {
                    makeOrderViewModel!!.setUserPhone(NekConfigs.getUserPgone())
                    MainActivity.logstr("T_PRICE: " + makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble())
                    makeOrderViewModel!!.setPrice(makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble())
                    makeOrderViewModel!!.setUserName(currentUser!!.displayName)
                    makeOrderViewModel!!.setRegion(MainActivity.chooseRegionLiveData!!.value!!.id.toString())
                    makeOrderViewModel!!.makeOrder().observe(this, Observer {
                        if (it == null)
                            return@Observer
                        Toast.makeText(context!!, "Ваш заказ принят", Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        bundle.putString("order_id", it)
                        val ow = ObjectMapper().writer().withDefaultPrettyPrinter()
                        val json = ow.writeValueAsString(makeOrderViewModel!!.makeOrderLiveData.value!!)
                        bundle.putString("json", json)
                        //MainActivity.logstr("JSON: "+json)

                        Logger.json(Gson().toJson(makeOrderViewModel!!.makeOrderLiveData.value))
                        makeOrderViewModel!!.refrefData()

                        Navigation.findNavController(view!!).navigate(R.id.action_makeOrderFragment_to_orderInfoFragment, bundle)
                        makeOrderViewModel!!.getInfoRegion.observe(this, Observer {
                            if (it == null)
                                return@Observer
                            setTarifPrices(it.tarrifs, false, true)
                            //onStopProgress()

                        })
                    })
                } catch (e: Exception) {
                    Toast.makeText(context, "По данному маршруту нельзя оформить заказ", Toast.LENGTH_LONG).show()
                    Logger.e("BTN_TO_BOOK_EXC:\r\n" + e.toString())
                }
            }
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {

        val ans = dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return ans
    }


    private fun changeBtnToBook() {
        makeorder_btn.setText(R.string.order_taxi)
        makeorder_btn.setOnClickListener {
            MainActivity.logstr("METHOD: " + NekConfigs.paymethod)
            if (makeOrderViewModel!!.makeOrderLiveData.value!!.date == null && makeOrderViewModel!!.makeOrderLiveData.value!!.time == null) {
                Snackbar.make(makeorder_btn, R.string.select_pick_up_time, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!NekConfigs.paymethod.equals("cash") && !NekConfigs.paymethod.equals("gpay") && !NekConfigs.paymethod.equals("bonusp")) {
                var params: GooglePayParams = GooglePayParams.Builder()
                        .setAddressRequired(false)
                        .setPhoneRequired(false)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
                        //.setBuyButtonAppearance()//WalletConstants.ENVIRONMENT_TEST
                        .build()

                var sum: Long = makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toLong()
                //AcquiringSdk.setDebug(true)
                var payment: Payment = Payment()
                var dial: BottomSheetDialog = BottomSheetDialog(context!!)
                var viewD: View = LayoutInflater.from(context).inflate(R.layout.sheet, null)
                var pay100: ConstraintLayout = viewD.findViewById(R.id.pay100)
                var pay2080: ConstraintLayout = viewD.findViewById(R.id.pay2080)
                var pay5050: ConstraintLayout = viewD.findViewById(R.id.pay5050)
                var click100: View.OnClickListener = View.OnClickListener {
                    viewD.findViewById<RadioButton>(R.id.check100)!!.isChecked = true
                    viewD.findViewById<RadioButton>(R.id.check2080)!!.isChecked = false
                    viewD.findViewById<RadioButton>(R.id.check5050)!!.isChecked = false
                    payment.type = "100online"
                    payment.pay_online = sum.toDouble()
                    payment.pay_offline = 0.toDouble()
                    payment.currency = MainActivity.currency.id.toString()
                    payment.currencyValue = MainActivity.currency.value
                    makeOrderViewModel!!.setPayType(payment)
                    var itt: Item = Item(makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress, sum * 100, 1.0, sum * 100, Tax.VAT_0)
                    val receipt: Receipt = Receipt(arrayOf(itt), NekConfigs.getUserEmail(), Taxation.OSN)



                    doPayment(sum.toDouble(), itt)
                    dial.dismiss()


                }
                var click2080: View.OnClickListener =
                        View.OnClickListener {
                            viewD.findViewById<RadioButton>(R.id.check100)!!.isChecked = false
                            viewD.findViewById<RadioButton>(R.id.check2080)!!.isChecked = true
                            viewD.findViewById<RadioButton>(R.id.check5050)!!.isChecked = false
                            payment.type = "2080"
                            payment.currency = MainActivity.currency.id.toString()
                            payment.pay_online = sum.toDouble() * 0.2
                            MainActivity.logstr("2080_online: " + payment.pay_online + " long: " + payment.pay_online.toLong())
                            payment.pay_offline = sum.toDouble() - payment.pay_online
                            payment.currencyValue = MainActivity.currency.value
                            makeOrderViewModel!!.setPayType(payment)
                            var itt: Item = Item(makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress, payment.pay_online.toLong() * 100, 1.0, payment.pay_online.toLong() * 100, Tax.VAT_0)
                            val receipt: Receipt = Receipt(arrayOf(itt), NekConfigs.getUserEmail(), Taxation.OSN)
                            //AcquiringSdk.setDebug(true)

                            doPayment(payment.pay_online, itt)
                            dial.dismiss()
                        }
                var click5050: View.OnClickListener = View.OnClickListener {
                    viewD.findViewById<RadioButton>(R.id.check100)!!.isChecked = false
                    viewD.findViewById<RadioButton>(R.id.check2080)!!.isChecked = false
                    viewD.findViewById<RadioButton>(R.id.check5050)!!.isChecked = true
                    payment.type = "5050"
                    payment.currency = MainActivity.currency.id.toString()
                    payment.pay_online = sum.toDouble() * 0.5
                    payment.pay_offline = sum.toDouble() - payment.pay_online
                    payment.currencyValue = MainActivity.currency.value
                    makeOrderViewModel!!.setPayType(payment)
                    var itt: Item = Item(makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress, payment.pay_online.toLong() * 100, 1.0, payment.pay_online.toLong() * 100, Tax.VAT_0)
                    val receipt: Receipt = Receipt(arrayOf(itt), NekConfigs.getUserEmail(), Taxation.OSN)

                    doPayment(payment.pay_online, itt)
                    dial.dismiss()
                }

                viewD.findViewById<RadioButton>(R.id.check100)!!.setOnClickListener(click100)
                viewD.findViewById<RadioButton>(R.id.check2080)!!.setOnClickListener(click2080)
                viewD.findViewById<RadioButton>(R.id.check5050)!!.setOnClickListener(click5050)
                pay100.setOnClickListener(click100)
                pay2080.setOnClickListener(click2080)
                pay5050.setOnClickListener(click5050)
                dial.setContentView(viewD)
                dial.show()


            } else {
                if (NekConfigs.paymethod.equals("gpay")) {
                    MainActivity.logstr("Make Payment With GPAY")
                    var params: GooglePayParams = GooglePayParams.Builder()
                            .setAddressRequired(false)
                            .setPhoneRequired(false)
                            .setTheme(WalletConstants.THEME_LIGHT)
                            .setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
                            // .setBuyButtonAppearance()//WalletConstants.ENVIRONMENT_TEST
                            .build()
                    MainActivity.logstr(MainActivity.currency.id.toString() + " - currency - " + MainActivity.currency.value)
                    var sum: Long = makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toLong() * MainActivity.currency.value
                    AcquiringSdk.setDebug(true)
                    var payment: Payment = Payment()
                    var dial: BottomSheetDialog = BottomSheetDialog(context!!)
                    var viewD: View = LayoutInflater.from(context).inflate(R.layout.sheet, null)
                    var pay100: ConstraintLayout = viewD.findViewById(R.id.pay100)
                    var pay2080: ConstraintLayout = viewD.findViewById(R.id.pay2080)
                    var pay5050: ConstraintLayout = viewD.findViewById(R.id.pay5050)
                    pay100.setOnClickListener(View.OnClickListener {
                        viewD.findViewById<RadioButton>(R.id.check100)!!.isChecked = true
                        viewD.findViewById<RadioButton>(R.id.check2080)!!.isChecked = false
                        viewD.findViewById<RadioButton>(R.id.check5050)!!.isChecked = false
                        payment.type = "100online"
                        payment.currency = MainActivity.currency.id.toString()
                        payment.pay_online = sum.toDouble()
                        payment.pay_offline = 0.toDouble()
                        payment.currencyValue = MainActivity.currency.value
                        makeOrderViewModel!!.setPayType(payment)
                        var itt: Item = Item(makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress, sum * 100, 1.0, sum * 100, Tax.VAT_0)
                        val receipt: Receipt = Receipt(arrayOf(itt), null, Taxation.OSN)
                        receipt.phone = NekConfigs.getUserPgone()
                        PayFormActivity
                                .init(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey) // данные продавца
                                .prepare(SystemClock.elapsedRealtime().toString(),
                                        Money.ofRubles(sum),
                                        getString(R.string.pay_order),
                                        makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress,
                                        NekConfigs.paymethod,
                                        NekConfigs.getUserEmail(),
                                        true,
                                        true)
                                .setChargeMode(true)
                                .setGooglePayParams(params)
                                .setCustomerKey(NekConfigs.getUserID())
                                .setReceipt(receipt)
                                .startActivityForResult(activity!!, 11)
                        dial.dismiss()
                    })
                    pay2080.setOnClickListener(View.OnClickListener {
                        viewD.findViewById<RadioButton>(R.id.check100)!!.isChecked = false
                        viewD.findViewById<RadioButton>(R.id.check2080)!!.isChecked = true
                        viewD.findViewById<RadioButton>(R.id.check5050)!!.isChecked = false
                        payment.type = "2080"
                        payment.currency = MainActivity.currency.id.toString()
                        payment.pay_online = sum.toDouble() * 0.2
                        payment.pay_offline = sum.toDouble() - payment.pay_online
                        payment.currencyValue = MainActivity.currency.value
                        makeOrderViewModel!!.setPayType(payment)
                        var itt: Item = Item(makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress, payment.pay_online.toLong() * 100, 1.0, payment.pay_online.toLong() * 100, Tax.VAT_0)
                        val receipt: Receipt = Receipt(arrayOf(itt), null, Taxation.OSN)
                        receipt.phone = NekConfigs.getUserPgone()
                        PayFormActivity
                                .init(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey) // данные продавца
                                .prepare(SystemClock.elapsedRealtime().toString(),
                                        Money.ofRubles(payment.pay_online.toLong()),
                                        getString(R.string.pay_order),
                                        makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress,
                                        NekConfigs.paymethod,
                                        NekConfigs.getUserEmail(),
                                        true,
                                        true)
                                .setGooglePayParams(params)
                                .setCustomerKey(NekConfigs.getUserID())
                                .setReceipt(receipt)
                                .startActivityForResult(activity!!, 11)
                        dial.dismiss()
                    })
                    pay5050.setOnClickListener(View.OnClickListener {
                        viewD.findViewById<RadioButton>(R.id.check100)!!.isChecked = false
                        viewD.findViewById<RadioButton>(R.id.check2080)!!.isChecked = false
                        viewD.findViewById<RadioButton>(R.id.check5050)!!.isChecked = true
                        payment.type = "5050"
                        payment.currency = MainActivity.currency.id.toString()
                        payment.pay_online = sum.toDouble() * 0.5
                        payment.pay_offline = sum.toDouble() - payment.pay_online
                        payment.currencyValue = MainActivity.currency.value
                        makeOrderViewModel!!.setPayType(payment)
                        var itt: Item = Item(makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress, payment.pay_online.toLong() * 100, 1.0, payment.pay_online.toLong() * 100, Tax.VAT_0)
                        val receipt: Receipt = Receipt(arrayOf(itt), null, Taxation.OSN)
                        receipt.phone = NekConfigs.getUserPgone()

                        PayFormActivity.init(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey)
                                .prepare(SystemClock.elapsedRealtime().toString(),
                                        Money.ofRubles(payment.pay_online.toLong()),
                                        getString(R.string.pay_order),
                                        makeOrderViewModel!!.makeOrderLiveData.value!!.from!!.fullAddress + " - " + makeOrderViewModel!!.makeOrderLiveData.value!!.to!!.fullAddress,
                                        NekConfigs.paymethod,
                                        NekConfigs.getUserEmail(),
                                        true,
                                        true)
                                .setGooglePayParams(params)
                                .setCustomerKey(NekConfigs.getUserID())
                                .setReceipt(receipt)
                                .startActivityForResult(activity!!, 22)
                        dial.dismiss()
                    })
                    dial.setContentView(viewD)
                    dial.show()


                } else {
                    if (NekConfigs.paymethod.equals("bonusp")) makeOrderViewModel!!.bonusP = 1

                    var payment: Payment = Payment()
                    payment.type = "100offline"
                    payment.currency = MainActivity.currency.id.toString()
                    payment.currencyValue = MainActivity.currency.value
                    if (NekConfigs.paymethod.equals("bonusp")) {
                        payment.type = "100online"
                        payment.pay_online = makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble()
                        payment.pay_offline = 0.toDouble()
                    } else {
                        payment.pay_online = 0.toDouble()
                        payment.pay_offline = makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble()
                    }
                    makeOrderViewModel!!.setPayType(payment)
                    //Toast.makeText(context, "Оплата наличными!", Toast.LENGTH_SHORT).show()
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    //Toast.makeText(context!!.applicationContext, MainActivity.chooseRegionLiveData!!.value!!.id.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        makeOrderViewModel!!.setUserPhone(NekConfigs.getUserPgone())
                        MainActivity.logstr("T_PRICE: " + makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble())
                        makeOrderViewModel!!.setPrice(makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble())
                        makeOrderViewModel!!.setUserName(currentUser!!.displayName)
                        makeOrderViewModel!!.setRegion(MainActivity.chooseRegionLiveData!!.value!!.id.toString())
                        makeOrderViewModel!!.makeOrder().observe(this, Observer {
                            if (it == null)
                                return@Observer

                            if (it.length > 20) {
                                onStopProgress()
                                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                                makeOrderViewModel!!.bonusP = 0
                                // makeOrderViewModel!!.refrefData()
                            } else {
                                MainActivity.updateMenuBalance()
                                Toast.makeText(context!!, "Ваш заказ принят", Toast.LENGTH_SHORT).show()
                                val bundle = Bundle()
                                bundle.putString("order_id", it)
                                val ow = ObjectMapper().writer().withDefaultPrettyPrinter()
                                val json = ow.writeValueAsString(makeOrderViewModel!!.makeOrderLiveData.value!!)
                                bundle.putString("json", json)
                                //MainActivity.logstr("JSON: "+json)

                                Logger.json(Gson().toJson(makeOrderViewModel!!.makeOrderLiveData.value))
                                makeOrderViewModel!!.bonusP = 0
                                makeOrderViewModel!!.refrefData()
                                Navigation.findNavController(view!!).navigate(R.id.action_makeOrderFragment_to_orderInfoFragment, bundle)
                                makeOrderViewModel!!.getInfoRegion.observe(this, Observer {
                                    if (it == null)
                                        return@Observer
                                    //  onStartProgress()
                                    setTarifPrices(it.tarrifs, false, true)
                                    //onStopProgress()

                                })
                            }
                        })
                    } catch (e: Exception) {
                        Toast.makeText(context, "По данному маршруту нельзя оформить заказ", Toast.LENGTH_LONG).show()
                        Logger.e("BTN_TO_BOOK_EXC:\r\n" + e.toString())
                    }

                    //Navigation.findNavController(it).navigate(R.id.action_makeOrderFragment_to_choosePaymentFragment, Bundle())
                }
            }
        }
    }

    fun doPayment(summ: Double, itt: Item) {
        var orderId = Date().time
        val sum = (summ * MainActivity.currency.value).toLong()
        MainActivity.logstr("SUM_ON_PAYMENT: " + sum + " summ:" + summ + " cv: " + MainActivity.currency.value)
        ChoosePaymentFragment.doAsync {
            //  onStartProgress()
            val tapi: TinkoffApi = TinkoffApi()
            var preToken: String = (sum * 100).toString() + NekConfigs.getUserID() + orderId.toString() + NekConfigs.tinkoftPassword + "Y" + NekConfigs.tinkoftTerminalKey
            var token = Hashing.sha256()
                    .hashString(preToken, StandardCharsets.UTF_8)
                    .toString()
            MainActivity.logstr("TOKEN_TINKOFF: " + token + " " + NekConfigs.getUserEmail())
            var jsonReceipt: JSONObject = JSONObject()
                    // .put("Email", NekConfigs.getUserEmail())
                    .put("Phone", NekConfigs.getUserPgone())
                    .put("Taxation", "osn")
                    .put("Items", JSONArray().put(JSONObject()
                            .put("Name", itt.name)
                            .put("Price", sum * 100)
                            .put("Quantity", itt.quantity)
                            .put("Amount", sum * 100)
                            .put("Tax", "vat0")))
            var jsonInit: JSONObject = JSONObject()
            jsonInit.put("TerminalKey", NekConfigs.tinkoftTerminalKey)
            jsonInit.put("Amount", sum * 100)
            jsonInit.put("OrderId", orderId)
            jsonInit.put("Token", token)
            jsonInit.put("Recurrent", "Y")
            jsonInit.put("CustomerKey", NekConfigs.getUserID())
            jsonInit.put("Receipt", jsonReceipt)

            val init: JSONObject = JSONObject(tapi.doInit("https://securepay.tinkoff.ru/v2/Init", jsonInit))
            MainActivity.logstr("INIT: Amount" + init.toString())
            if (init.getBoolean("Success")) {
                val jsonFinish: JSONObject = JSONObject()
                        .put("TerminalKey", NekConfigs.tinkoftTerminalKey)
                        .put("Amount", sum * 100)
                        .put("PaymentId", init.getString("PaymentId"))
                var sdk: AcquiringSdk = AcquiringSdk(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey)
                try {
                    var charge: PaymentInfo = sdk.charge(init.getLong("PaymentId"), CardManager(sdk).getCardById(NekConfigs.paymethod).rebillId)
                    MainActivity.logstr("CHARGE: " + charge.isSuccess)
                    onStopProgress()
                    if (charge.isSuccess) {
                        MainActivity.logstr("CHARGE: " + charge.isSuccess)
                        // MainThread().
                        activity!!.runOnUiThread {
                            make_order()
                        }
                    } else {
                        Snackbar.make(makeorder_btn, getString(R.string.paying_error), Snackbar.LENGTH_SHORT)
                                .show()
                    }
                } catch (e: java.lang.Exception) {
//                    onStopProgress()
                    Snackbar.make(makeorder_btn, getString(R.string.paying_error), Snackbar.LENGTH_SHORT)
                            .show()
                    MainActivity.logstr(e.toString())
                }
                // val finish: JSONObject = JSONObject(tapi.doInit("https://securepay.tinkoff.ru/v2/FinishAuthorize ", jsonFinish))
                //MainActivity.logstr("FINISH: " + finish.toString())
            } else {
                Snackbar.make(makeorder_btn, getString(R.string.paying_error), Snackbar.LENGTH_SHORT)
                        .show()
            }


        }.execute()
    }

    private fun setTarifPrices(tarrifs: Tarif, nek: Boolean, def: Boolean) {
        Logger.e("setTarifPrices ")
        // onStartProgress()
        makeOrderViewModel!!.setCurrency()

        tarifsList.clear()
        if (tarrifs.business != null) tarrifs.business
                .icon = R.drawable.car_bissenes
        if (tarrifs.businessPlus != null) tarrifs.businessPlus
                .icon = R.drawable.car_businessplus
        if (tarrifs.comfort != null) tarrifs.comfort
                .icon = R.drawable.car_comfort
        if (tarrifs.econom != null) tarrifs.econom
                .icon = R.drawable.car_econom
        if (tarrifs.minibus != null) tarrifs.minibus
                .icon = R.drawable.car_microbus
        if (tarrifs.minibusPlus != null) tarrifs.minibusPlus
                .icon = R.drawable.car_microbusplus
        if (tarrifs.minibusVip != null) tarrifs.minibusVip
                .icon = R.drawable.car_minibusvip
        if (tarrifs.minivan != null) tarrifs.minivan
                .icon = R.drawable.car_miniven
        if (tarrifs.universal != null) tarrifs.universal
                .icon = R.drawable.car_universal
        if (tarrifs.premium != null) tarrifs.premium
                .icon = R.drawable.car_premium

        if (tarrifs.econom != null) tarrifs.econom
                .name = getString(R.string.econom)
        if (tarrifs.comfort != null) tarrifs.comfort
                .name = getString(R.string.comfort)
        if (tarrifs.universal != null) tarrifs.universal
                .name = getString(R.string.universal)
        if (tarrifs.business != null) tarrifs.business
                .name = getString(R.string.bussines)
        if (tarrifs.businessPlus != null) tarrifs.businessPlus
                .name = getString(R.string.bussines_plus)
        if (tarrifs.minivan != null) tarrifs.minivan
                .name = getString(R.string.minivan)
        if (tarrifs.minibus != null) tarrifs.minibus
                .name = getString(R.string.minibus)
        if (tarrifs.minibusVip != null) tarrifs.minibusVip
                .name = getString(R.string.minibusvip)
        if (tarrifs.minibusPlus != null) tarrifs.minibusPlus
                .name = getString(R.string.minibusplus)
        if (tarrifs.premium != null) tarrifs.premium
                .name = getString(R.string.premium)

        if (tarrifs.econom != null) tarrifs.econom
                .picture = R.drawable.car_econom_pic
        if (tarrifs.comfort != null) tarrifs.comfort
                .picture = R.drawable.car_comfort_pic
        if (tarrifs.universal != null) tarrifs.universal
                .picture = R.drawable.car_universal_pic
        if (tarrifs.business != null) tarrifs.business
                .picture = R.drawable.car_bussines_pic
        if (tarrifs.businessPlus != null) tarrifs.businessPlus
                .picture = R.drawable.car_bussines_plus_pic
        if (tarrifs.minivan != null) tarrifs.minivan
                .picture = R.drawable.car_miniven_pic
        if (tarrifs.minibus != null) tarrifs.minibus
                .picture = R.drawable.car_microbus_pic
        if (tarrifs.minibusVip != null) tarrifs.minibusVip
                .picture = R.drawable.car_microbus_vip_pic
        if (tarrifs.minibusPlus != null) tarrifs.minibusPlus
                .picture = R.drawable.car_minibus_plus_pic
        if (tarrifs.premium != null) tarrifs.premium
                .picture = R.drawable.car_premium_pic


        if (tarrifs.business != null) tarrifs.business
                .carname = "Toyota Camry, Kia Optima"
        if (tarrifs.businessPlus != null) tarrifs.businessPlus
                .carname = "Mersedes E-class, BMW 5"
        if (tarrifs.comfort != null) tarrifs.comfort
                .carname = "Skoda Octavia, Ford Focus"
        if (tarrifs.econom != null) tarrifs.econom
                .carname = "Kia Rio, VW Polo, Hyundai Solaris"
        if (tarrifs.minibus != null) tarrifs.minibus
                .carname = "VW Caravelle, Hyundai Starex"
        if (tarrifs.minibusPlus != null) tarrifs.minibusPlus
                .carname = "Mercedes-Benz Sprinter"
        if (tarrifs.minibusVip != null) tarrifs.minibusVip
                .carname = "Mercedes V-Class"
        if (tarrifs.minivan != null) tarrifs.minivan
                .carname = "VW Caddy, Ford Galaxy"
        if (tarrifs.universal != null) tarrifs.universal
                .carname = "Авто с просторным багажником "
        if (tarrifs.premium != null) tarrifs.premium
                .carname = "Mercedes S, BMW 7, AUDI A8, Hyundai Equus"


        if (!def) {
            tarifsList.clear()
            if (tarrifs.econom != null) {
                tarifsList.addNek(tarrifs.econom, nek)
            }
            if (tarrifs.comfort != null) {
                tarifsList.addNek(tarrifs.comfort, nek)
            }
            if (tarrifs.universal != null) {
                tarifsList.addNek(tarrifs.universal, nek)
            }
            if (tarrifs.minivan != null) {
                tarifsList.addNek(tarrifs.minivan, nek)
            }
            if (tarrifs.minibus != null) {
                tarifsList.addNek(tarrifs.minibus, nek)
            }
            if (tarrifs.minibusPlus != null) {
                tarifsList.addNek(tarrifs.minibusPlus, nek)
            }
            if (tarrifs.minibusVip != null) {
                tarifsList.addNek(tarrifs.minibusVip, nek)
            }
            if (tarrifs.business != null) {
                tarifsList.addNek(tarrifs.business, nek)
            }
            if (tarrifs.businessPlus != null) {
                tarifsList.addNek(tarrifs.businessPlus, nek)
            }
            if (tarrifs.premium != null) {
                tarifsList.addNek(tarrifs.premium, nek)
            }
            val itlist = tarifsList.iterator()

        } else {
            if (tarrifs.econom != null) tarrifs.econom.price = null
            if (tarrifs.comfort != null) tarrifs.comfort.price = null
            if (tarrifs.universal != null) tarrifs.universal.price = null
            if (tarrifs.minivan != null) tarrifs.minivan.price = null
            if (tarrifs.minibus != null) tarrifs.minibus.price = null
            if (tarrifs.minibusPlus != null) tarrifs.minibusPlus.price = null
            if (tarrifs.minibusVip != null) tarrifs.minibusVip.price = null
            if (tarrifs.business != null) tarrifs.business.price = null
            if (tarrifs.businessPlus != null) tarrifs.businessPlus.price = null
            if (tarrifs.premium != null) tarrifs.premium.price = null
            tarifsList.clear()
            if (tarrifs.econom != null) tarifsList.add(tarrifs.econom)
            if (tarrifs.comfort != null) tarifsList.add(tarrifs.comfort)
            if (tarrifs.universal != null) tarifsList.add(tarrifs.universal)
            if (tarrifs.minivan != null) tarifsList.add(tarrifs.minivan)
            if (tarrifs.minibus != null) tarifsList.add(tarrifs.minibus)
            if (tarrifs.minibusPlus != null) tarifsList.add(tarrifs.minibusPlus)
            if (tarrifs.minibusVip != null) tarifsList.add(tarrifs.minibusVip)
            if (tarrifs.business != null) tarifsList.add(tarrifs.business)
            if (tarrifs.businessPlus != null) tarifsList.add(tarrifs.businessPlus)
            if (tarrifs.premium != null) tarifsList.add(tarrifs.premium)
        }
        MainActivity.logstr("MIN ECONOM = " + tarrifs.econom.min)
        tarifAdapter.setTarifObjList(tarifsList)
        onStopProgress()
    }

    fun make_order() {
        if (makeOrderViewModel!!.makeOrderLiveData.value!!.date == null && makeOrderViewModel!!.makeOrderLiveData.value!!.time == null) {
            Snackbar.make(makeorder_btn, R.string.select_pick_up_time, Snackbar.LENGTH_LONG).show()
        } else {
            val currentUser = FirebaseAuth.getInstance().currentUser
            //Toast.makeText(context!!.applicationContext, MainActivity.chooseRegionLiveData!!.value!!.id.toString(), Toast.LENGTH_SHORT).show();
            try {

                makeOrderViewModel!!.setUserPhone(NekConfigs.getUserPgone())
                MainActivity.logstr("T_PRICE: " + makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble())
                makeOrderViewModel!!.setPrice(makeOrderViewModel!!.makeOrderLiveData.value?.tarif?.price!!.toDouble())
                makeOrderViewModel!!.setUserName(currentUser!!.displayName)
                makeOrderViewModel!!.setRegion(MainActivity.chooseRegionLiveData!!.value!!.id.toString())
                makeOrderViewModel!!.makeOrder().observe(this, Observer {
                    if (it == null)
                        return@Observer

                    MainActivity.logstr("pre-update")
                    Toast.makeText(context!!, "Ваш заказ принят", Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    bundle.putString("order_id", it)
                    val ow = ObjectMapper().writer().withDefaultPrettyPrinter()
                    val json = ow.writeValueAsString(makeOrderViewModel!!.makeOrderLiveData.value!!)
                    bundle.putString("json", json)
                    //MainActivity.logstr("JSON: "+json)

                    Logger.json(Gson().toJson(makeOrderViewModel!!.makeOrderLiveData.value))
                    makeOrderViewModel!!.refrefData()

                    Navigation.findNavController(view!!).navigate(R.id.action_makeOrderFragment_to_orderInfoFragment, bundle)
                    makeOrderViewModel!!.getInfoRegion.observe(this, Observer {
                        if (it == null)
                            return@Observer
                        setTarifPrices(it.tarrifs, false, true)
                        //onStopProgress()

                    })
                })
            } catch (e: Exception) {
                Toast.makeText(context, "По данному маршруту нельзя оформить заказ", Toast.LENGTH_LONG).show()
                Logger.e("BTN_TO_BOOK_EXC:\r\n" + e.toString())
            }
        }
    }

    fun count_trans(def: Boolean) {
        // onStartProgress()
        makeOrderViewModel!!.getInfoRegion.observe(this, Observer {
            if (it == null)
                return@Observer
            var defTar = it.tarrifs
            MainActivity.logstr("COUNT_TRANS_DEF: " + def)


            makeOrderViewModel!!.tarifPrices.observe(this, Observer { tariffPrices ->
                assert(tariffPrices != null)
                try {

                    if (it.tarrifs.business != null) it.tarrifs.business
                            .price = tariffPrices!!.business
                    if (it.tarrifs.businessPlus != null) it.tarrifs.businessPlus
                            .price = tariffPrices.businessPlus
                    if (it.tarrifs.comfort != null) it.tarrifs.comfort
                            .price = tariffPrices.comfort
                    if (it.tarrifs.econom != null) it.tarrifs.econom
                            .price = tariffPrices.econom
                    if (it.tarrifs.minibus != null) it.tarrifs.minibus
                            .price = tariffPrices.minibus
                    if (it.tarrifs.minibusPlus != null) it.tarrifs.minibusPlus
                            .price = tariffPrices.minibusPlus
                    if (it.tarrifs.minibusVip != null) it.tarrifs.minibusVip
                            .price = tariffPrices.minibusVip
                    if (it.tarrifs.minivan != null) it.tarrifs.minivan
                            .price = tariffPrices.minivan
                    if (it.tarrifs.universal != null) it.tarrifs.universal
                            .price = tariffPrices.universal
                    if (it.tarrifs.premium != null) it.tarrifs.premium
                            .price = tariffPrices.premium
                    if (def) {
                        if (it.tarrifs.business != null) it.tarrifs.business
                                .price = defTar.business.min.toString()
                        MainActivity.logstr("MIN_BUSINESS: " + it.tarrifs.business.price)
                        if (it.tarrifs.businessPlus != null) it.tarrifs.businessPlus
                                .price = defTar.businessPlus.min.toString()
                        if (it.tarrifs.comfort != null) it.tarrifs.comfort
                                .price = defTar.comfort.min.toString()
                        if (it.tarrifs.econom != null) it.tarrifs.econom
                                .price = defTar.econom.min.toString()
                        if (it.tarrifs.minibus != null) it.tarrifs.minibus
                                .price = defTar.minibus.min.toString()
                        if (it.tarrifs.minibusPlus != null) it.tarrifs.minibusPlus
                                .price = defTar.minibusPlus.min.toString()
                        if (it.tarrifs.minibusVip != null) it.tarrifs.minibusVip
                                .price = defTar.minibusVip.min.toString()
                        if (it.tarrifs.minivan != null) it.tarrifs.minivan
                                .price = defTar.minivan.min.toString()
                        if (it.tarrifs.universal != null) it.tarrifs.universal
                                .price = defTar.universal.min.toString()
                        if (it.tarrifs.premium != null) it.tarrifs.premium
                                .price = defTar.premium.min.toString()
                    }
                    Logger.d("PREMIUM_PRICE =  " + tariffPrices.premium)
                    if (it.tarrifs.business != null) it.tarrifs.business
                            .crm_name = "business"
                    if (it.tarrifs.businessPlus != null) it.tarrifs.businessPlus
                            .crm_name = "businessPlus"
                    if (it.tarrifs.comfort != null) it.tarrifs.comfort
                            .crm_name = "comfort"
                    if (it.tarrifs.econom != null) it.tarrifs.econom
                            .crm_name = "econom"
                    if (it.tarrifs.minibus != null) it.tarrifs.minibus
                            .crm_name = "minibus"
                    if (it.tarrifs.minibusPlus != null) it.tarrifs.minibusPlus
                            .crm_name = "minibusPlus"
                    if (it.tarrifs.minibusVip != null) it.tarrifs.minibusVip
                            .crm_name = "minibusVip"
                    if (it.tarrifs.minivan != null) it.tarrifs.minivan
                            .crm_name = "minivan"
                    if (it.tarrifs.universal != null) it.tarrifs.universal
                            .crm_name = "universal"
                    if (it.tarrifs.premium != null) it.tarrifs.premium
                            .crm_name = "premium"

                    setTarifPrices(it.tarrifs, true, false)
                    changeBtnToBook()


                } catch (e: Exception) {
                    Logger.e("TARIF_EXCEPTION:\r\n" + e.toString())
                    setTarifPrices(it.tarrifs, false, true)
                    if (makeOrderViewModel!!.makeOrderLiveData.value!!.from != null && makeOrderViewModel!!.makeOrderLiveData.value!!.to != null)
                        Toast.makeText(MainActivity.getAppContext(), "По данному маршруту нельзя оформить заказ", Toast.LENGTH_SHORT).show()
                    makeorder_btn.isEnabled = false

                }

            })

        })
        //onStopProgress()
    }

    private fun ArrayList<TarifObj>.addNek(tarifObj: TarifObj, nek: Boolean) {
        if (nek && tarifObj.price != null) {
            if (!tarifObj.price.equals("0")) tarifObj.price = (tarifObj.price.toInt() + calkChossenOptionsPrices()).toString()
        }
        if (tarifObj.price != null || !nek) {
            if (tarifObj.price != null) {
                if (!tarifObj.price.equals("0")) this.add(tarifObj)
            } else this.add(tarifObj)
        }

    }

    fun resetSlider() {

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

    override fun onStartProgress() {
        makeorder_progressBar.visibility = View.VISIBLE
        makeorder_btn.visibility = View.GONE
    }

    override fun onStopProgress() {
        makeorder_progressBar.visibility = View.GONE
        makeorder_btn.visibility = View.VISIBLE
    }

    override fun onReDoNetworkAction() {
    }

}

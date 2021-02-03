package com.avion.app

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import com.avion.app.adapter.AutoSuggestAdapterNew
import com.avion.app.adapter.viewholder.FavoriteListViewModel
import com.avion.app.entity.FaworiteAddressEntity
import com.avion.app.fragment.ChooseTimeFragment
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_new_favorite_address.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar


class CreateNewFavoriteAddressFragment : NekFragment() {
    override fun onCreateViewModel(): NekViewModel {
        favoriteAddressViewModel.nekFragment = this
        return favoriteAddressViewModel
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
    }

    override fun onStartProgress() {
    }

    private val favoriteAddressViewModel: FavoriteListViewModel by lazy { ViewModelProviders.of(activity!!).get(FavoriteListViewModel::class.java) }

    private lateinit var keyboardRegister: Unregistrar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_new_favorite_address, container, false)
    }

    private lateinit var quaryAddressAdapter: AutoSuggestAdapterNew

    private val handler = Handler(Handler.Callback { msg ->
        if (msg.what == 100) {
            if (createfavoriteaddress_address_AutoCompleteTextView == null)
                return@Callback false
            if (!TextUtils.isEmpty(createfavoriteaddress_address_AutoCompleteTextView.text)) {
                favoriteAddressViewModel.quaryAddress(createfavoriteaddress_address_AutoCompleteTextView.text.toString()) { address_list ->
                    quaryAddressAdapter.setData(address_list)
                    quaryAddressAdapter.notifyDataSetChanged()
                }
            }
        }
        return@Callback false
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val addressNew = FaworiteAddressEntity()

        keyboardRegister = KeyboardVisibilityEvent.registerEventListener(
                activity!!,
                object : ChooseTimeFragment.KeyboardVisibilityListener, KeyboardVisibilityEventListener {
                    override fun onVisibilityChanged(isOpen: Boolean) {
                        if (isOpen && createfavoriteaddress_address_AutoCompleteTextView.isFocused) {
                            moveAddressInputUp()
                        } else {
                            moveAddressInputDown()
                        }
                    }

                    override fun onKeyboardVisibilityChanged(keyboardVisible: Boolean) {
                        if (keyboardVisible && createfavoriteaddress_address_AutoCompleteTextView.isFocused) {
                            moveAddressInputUp()
                        } else {
                            moveAddressInputDown()
                        }
                    }
                }
        )

        createfavoriteaddress_home_btn.setOnClickListener {
            addressNew.address_name_icon = R.drawable.ic_home_grey
            createfavoriteaddress_addressname_edittext.setText(getString(R.string.home_address))
        }
        createfavoriteaddress_work_btn.setOnClickListener {
            addressNew.address_name_icon = R.drawable.ic_work_grey
            createfavoriteaddress_addressname_edittext.setText(getString(R.string.work_address))
        }

        quaryAddressAdapter = AutoSuggestAdapterNew(context!!, R.layout.item_region)

        createfavoriteaddress_list.adapter = quaryAddressAdapter
        createfavoriteaddress_list.setOnItemClickListener { _, _, possition, _ ->
            createfavoriteaddress_address_AutoCompleteTextView.setText(quaryAddressAdapter.getItem(possition))
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(createfavoriteaddress_address_AutoCompleteTextView.windowToken, 0) }
        }

        createfavoriteaddress_address_AutoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (createfavoriteaddress_address_AutoCompleteTextView.text.length > 0) {
                    createfavoriteaddress_list.visibility = View.VISIBLE
                    handler.removeMessages(100)
                    handler.sendEmptyMessageDelayed(100, 300)
                } else {
                    createfavoriteaddress_list.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        createfavoriteaddress_add_button.setOnClickListener {
            if (createfavoriteaddress_address_AutoCompleteTextView.text.toString().isEmpty() || createfavoriteaddress_addressname_edittext.text.toString().isEmpty()) {
                Snackbar.make(view, "Заполните все поля", Snackbar.LENGTH_SHORT)
                        .show()
                return@setOnClickListener
            }
            addressNew.id = addressNew.hashCode()
            addressNew.address = createfavoriteaddress_address_AutoCompleteTextView.text.toString()
            addressNew.address_name = createfavoriteaddress_addressname_edittext.text.toString()
            favoriteAddressViewModel.add(addressNew)
            activity!!.onBackPressed()
        }


//            }
//        }

    }

    override fun onStop() {
        super.onStop()
        keyboardRegister.unregister()
    }

    private fun moveAddressInputUp() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(createfavorite_constaintlayout)
        constraintSet.connect(R.id.materialCardView2, ConstraintSet.TOP, R.id.materialCardView2, ConstraintSet.TOP, 8)
        constraintSet.applyTo(createfavorite_constaintlayout)

        materialCardView.visibility = View.INVISIBLE
        createfavoriteaddress_home_btn.visibility = View.INVISIBLE
        createfavoriteaddress_work_btn.visibility = View.INVISIBLE
    }

    private fun moveAddressInputDown() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(createfavorite_constaintlayout)
        constraintSet.connect(R.id.materialCardView2, ConstraintSet.TOP, R.id.createfavoriteaddress_home_btn, ConstraintSet.BOTTOM, 30)
        constraintSet.applyTo(createfavorite_constaintlayout)

        materialCardView.visibility = View.VISIBLE
        createfavoriteaddress_home_btn.visibility = View.VISIBLE
        createfavoriteaddress_work_btn.visibility = View.VISIBLE
    }


}

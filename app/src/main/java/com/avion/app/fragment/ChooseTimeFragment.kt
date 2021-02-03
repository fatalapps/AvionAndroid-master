package com.avion.app.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.action.MakeOrder
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.jakewharton.rxbinding.widget.RxTextView
import com.ycuwq.datepicker.date.DatePickerDialogFragment
import kotlinx.android.synthetic.main.fragment_choose_time.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import rx.Observable


class ChooseTimeFragment : Fragment() {

    private var datePicker: DatePickerDialogFragment? = null
    private var timePicker: TimePickerDialog? = null

    private var mViewModel: MakeOrderViewModel? = null

    private var address_type: Int? = 0
    private var updated = false
    private var notfollowMyFlight = false

    companion object {
        val TYPE_ADDRESS = 0
        val TYPE_AIRPORT = 1
        val TYPE_TRAIN = 2
        fun txtAddressTypeToInt(txt: String): Int {
            return when (txt) {
                intAddressTypeToString(-1) -> TYPE_ADDRESS
                intAddressTypeToString(TYPE_ADDRESS) -> TYPE_ADDRESS
                intAddressTypeToString(TYPE_AIRPORT) -> TYPE_AIRPORT
                intAddressTypeToString(TYPE_TRAIN) -> TYPE_TRAIN
                else -> TYPE_ADDRESS
            }
        }

        fun intAddressTypeToString(num: Int): String {
            return when (num) {
                TYPE_ADDRESS -> "address"
                TYPE_AIRPORT -> "airport"
                TYPE_TRAIN -> "railway"
                else -> "address"
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_time, container, false)
    }

    private val observer = Observer(function = { makeOrder: MakeOrder? -> updateData(makeOrder!!) })

    private lateinit var keyboardRegister: Unregistrar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        keyboardRegister = KeyboardVisibilityEvent.registerEventListener(
                activity!!,
                object : KeyboardVisibilityListener, KeyboardVisibilityEventListener {
                    override fun onVisibilityChanged(isOpen: Boolean) {
                        if (isOpen) {
                            choosetime_textView2.visibility = View.INVISIBLE
                            choosetime_date_cardview.visibility = View.INVISIBLE
                            choosetime_time_cardview.visibility = View.INVISIBLE
                        } else {
                            choosetime_textView2.visibility = View.VISIBLE
                            choosetime_date_cardview.visibility = View.VISIBLE
                            choosetime_time_cardview.visibility = View.VISIBLE
                        }
                    }

                    override fun onKeyboardVisibilityChanged(keyboardVisible: Boolean) {
                        if (keyboardVisible) {
                            choosetime_textView2.visibility = View.INVISIBLE
                            choosetime_date_cardview.visibility = View.INVISIBLE
                            choosetime_time_cardview.visibility = View.INVISIBLE
                        } else {
                            choosetime_textView2.visibility = View.VISIBLE
                            choosetime_date_cardview.visibility = View.VISIBLE
                            choosetime_time_cardview.visibility = View.VISIBLE
                        }
                    }
                }
        )



        mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
        //updateData()
        mViewModel!!.makeOrderLiveData.observe(activity!!, observer)

        if (address_type == TYPE_ADDRESS) {
            choosetime_fightnumber_cardview.visibility = View.INVISIBLE
            choosetime_departingfrom_cardview.visibility = View.INVISIBLE
            choosetime_followwflight_layout.visibility = View.INVISIBLE
        } else if (address_type == TYPE_TRAIN) {
            choosetime_textView2.text = getString(R.string.select_pick_up_time_train)
            choosetime_fightnumber_edittext.hint = getString(R.string.train_number)
            choosetime_departingfrom_edittext.hint = getString(R.string.carriage_number)
            choosetime_followwflight_layout.visibility = View.INVISIBLE
        } else if (address_type == TYPE_AIRPORT) {
            choosetime_textView2.text = getString(R.string.select_pick_up_time_airport)
        }

        choosetime_date_cardview.setOnClickListener { openDatePicker() }
        choosetime_time_cardview.setOnClickListener { openTimePicker() }


        choosetime_select_btn.setOnClickListener {
            if (choosetime_followwflight_checkbox.isChecked) mViewModel!!.setCarToTime(true)
            else mViewModel!!.setCarToTime(false)
            mViewModel!!.setDate(choosetime_date_txtview.text.toString())
            mViewModel!!.setTime(choosetime_time_txtview.text.toString())
            //val api = MainApi()
            MainActivity.logstr(address_type.toString() + "-" + choosetime_fightnumber_edittext.text.toString() + " \r\n...FLIGHT_NUMBER_DEBUG")
            if (address_type == TYPE_AIRPORT) {
                mViewModel!!.setFlightNumber(choosetime_fightnumber_edittext.text.toString())
                mViewModel!!.setDepartingFrom(choosetime_departingfrom_edittext.text.toString())
            } else if (address_type == TYPE_TRAIN) {
                mViewModel!!.setTrainNumber(choosetime_fightnumber_edittext.text.toString())
                mViewModel!!.setCarriageNumber(choosetime_departingfrom_edittext.text.toString())
            }
            activity!!.onBackPressed()
        }

        val choosetime_fightnumber_edittextChangeEvents = RxTextView.textChangeEvents(choosetime_fightnumber_edittext)
        val choosetime_departingfrom_edittextChangeEvents = RxTextView.textChangeEvents(choosetime_departingfrom_edittext)
        val choosetime_date_txtviewChangeEvents = RxTextView.textChangeEvents(choosetime_date_txtview)
        val choosetime_time_txtviewChangeEvents = RxTextView.textChangeEvents(choosetime_time_txtview)


        Observable.combineLatest(
                choosetime_fightnumber_edittextChangeEvents,
                choosetime_departingfrom_edittextChangeEvents,
                choosetime_date_txtviewChangeEvents,
                choosetime_time_txtviewChangeEvents
        ) { fightNumber, departingfrom, date, time ->
            date.text() != getString(R.string.choose_date) && time.text() != getString(R.string.choose_time)
                    && ((fightNumber.text().isNotEmpty()) || address_type == TYPE_ADDRESS || choosetime_followwflight_checkbox.isChecked
                    || (address_type == TYPE_TRAIN && mViewModel?.makeOrderLiveData?.value?.options?.meeting_with_a_table == null))
        }
                .subscribe { aBoolean ->
                    choosetime_select_btn.isEnabled = aBoolean
                }


        choosetime_followwflight_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (choosetime_date_txtview.text.toString() != getString(R.string.choose_date) && choosetime_time_txtview.text.toString() != getString(R.string.choose_time)
                        && ((choosetime_fightnumber_edittext.text.toString().isNotEmpty())
                                || address_type == TYPE_ADDRESS || choosetime_followwflight_checkbox.isChecked) || (address_type == TYPE_TRAIN && mViewModel?.makeOrderLiveData?.value?.options?.meeting_with_a_table == null)) {
                    choosetime_select_btn.isEnabled = true
                    // mViewModel!!.setCarToTime(true)
                }
            } else {
                if (choosetime_fightnumber_edittext.text.toString().trim().length == 0) choosetime_select_btn.isEnabled = false
                MainActivity.logstr(choosetime_fightnumber_edittext.text.toString().trim().length.toString() + " - LENGTH")
                //mViewModel!!.setCarToTime(false)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        mViewModel!!.makeOrderLiveData.removeObserver(observer)
        keyboardRegister.unregister()
    }

    private fun updateData(makeOrder: MakeOrder) {
        if (!updated) {
            MainActivity.logstr(address_type.toString() + "-" + choosetime_fightnumber_edittext.text.toString() + "\r\n...UPDATEDATA DEBUG")

            if (makeOrder.from != null)
                address_type = ChooseTimeFragment.txtAddressTypeToInt(makeOrder.from!!.type)

            if (makeOrder.date != null)
                choosetime_date_txtview.text = makeOrder.date
            if (makeOrder.time != null)
                choosetime_time_txtview.text = makeOrder.time
            choosetime_followwflight_checkbox.isChecked = makeOrder.cartotime == true
            if (address_type == TYPE_AIRPORT) {
                // if(choosetime_fightnumber_edittext.text.toString().isNotEmpty()) makeOrder.flight_number = choosetime_fightnumber_edittext.text.toString()
                if (makeOrder.flight_number != null && choosetime_fightnumber_edittext.text.toString().isEmpty()) {
                    val old = makeOrder.flight_number
                    // makeOrder.flight_number = choosetime_fightnumber_edittext.text.toString()
                    choosetime_fightnumber_edittext.setText(old)
                } else makeOrder.flight_number = choosetime_fightnumber_edittext.text.toString()
                if (makeOrder.departing_from != null && choosetime_departingfrom_edittext.text.toString().isEmpty()) {
                    val old = makeOrder.departing_from
                    //  makeOrder.departing_from = choosetime_departingfrom_edittext.text.toString()
                    choosetime_departingfrom_edittext.setText(old)
                } else makeOrder.departing_from = choosetime_departingfrom_edittext.text.toString()
            } else if (address_type == TYPE_TRAIN) {
                if (makeOrder.train_number != null)
                    choosetime_fightnumber_edittext.setText(makeOrder.train_number)
                if (makeOrder.carriage_number != null)
                    choosetime_departingfrom_edittext.setText(makeOrder.carriage_number)
            }
            updated = true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openDatePicker() {
        if (datePicker == null) {
            datePicker = MyDatePickerDialogFragment()

            datePicker!!.setOnDateChooseListener { year, month, day ->
                var monthS = month.toString()
                var dayS = day.toString()
                if (monthS.length < 2) {
                    monthS = "0$month"
                }
                if (dayS.length < 2) {
                    dayS = "0$day"
                }
                choosetime_date_txtview.text = "$dayS.$monthS.$year"
            }
        }
        datePicker!!.show(fragmentManager!!, datePicker!!.tag)
    }

    @SuppressLint("SetTextI18n")
    private fun openTimePicker() {
        if (timePicker == null) {
            timePicker = TimePickerDialog(context!!,
                    com.avion.app.R.style.Theme_AppCompat_Dialog_Alert,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        var minS = minute.toString()
                        if (minS.length < 2) {
                            minS = "0" + minS
                        }
                        choosetime_time_txtview.text = "$hourOfDay:$minS"
                    }, 12, 12, true)
        }
        timePicker!!.show()
    }

    interface KeyboardVisibilityListener {
        fun onKeyboardVisibilityChanged(keyboardVisible: Boolean)
    }

    fun setKeyboardVisibilityListener(activity: Activity, keyboardVisibilityListener: KeyboardVisibilityListener) {
        val contentView = activity.findViewById<View>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            private var mPreviousHeight: Int = 0

            override fun onGlobalLayout() {
                val newHeight = contentView.height
                if (mPreviousHeight != 0) {
                    if (mPreviousHeight > newHeight) {
                        // Height decreased: keyboard was shown
                        keyboardVisibilityListener.onKeyboardVisibilityChanged(true)
                    } else if (mPreviousHeight < newHeight) {
                        // Height increased: keyboard was hidden
                        keyboardVisibilityListener.onKeyboardVisibilityChanged(false)
                    } else {
                        // No change
                    }
                }
                mPreviousHeight = newHeight
            }
        })
    }

}

package com.avion.app.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_meet_wtable.*

@Suppress("UNREACHABLE_CODE")
class MeetWTableFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null)
            mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
        return mViewModel!!
    }

    override fun onStartProgress() {
    }

    override fun onStopProgress() {
    }

    override fun onReDoNetworkAction() {
    }

    private var updated = false
    private var mViewModel: MakeOrderViewModel? = null

    private var address_type: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meet_wtable, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //meet_w_tableeditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        mViewModel!!.makeOrderLiveData.observe(this, Observer {
            if (!updated) {
                if (it!!.from != null) {
                    MainActivity.logstr(it.from!!.type.toString() + " - TYPE")
                    when (it.from!!.type.toString()) {
                        "railway" -> address_type = ChooseTimeFragment.TYPE_TRAIN
                        "airport" -> address_type = ChooseTimeFragment.TYPE_AIRPORT
                        "address" -> address_type = ChooseTimeFragment.TYPE_ADDRESS
                    }

                    //    address_type = ChooseTimeFragment.txtAddressTypeToInt(it!!.from!!.type)
                }

                if (address_type == ChooseTimeFragment.TYPE_AIRPORT) {
                    if (it.flight_number != null)
                        choosetime_fightnumber_edittext.setText(it.flight_number)
                    if (it.departing_from != null)
                        choosetime_departingfrom_edittext.setText(it.departing_from)
                    if (it.cartotime != true) {
                        meetwtable_txt.text =
                                "Водитель встретит Вас с табличкой в зоне прилета аэропорта. Рейс отслеживается. Бесплатное ожидание 60 мин с момента посадки самолета.\n" +
                                        "Внимание! Платная парковка не включена в стоимость заказа."
                    } else {
                        meetwtable_txt.text =
                                "Водитель встретит Вас с табличкой в зоне прилета аэропорта. Бесплатное ожидание 10 мин от контрольного времени.\n" +
                                        "Внимание! Платная парковка не включена в стоимость заказа."
                    }
                } else if (address_type == ChooseTimeFragment.TYPE_TRAIN) {
                    if (it.train_number != null)
                        choosetime_fightnumber_edittext.setText(it.train_number)
                    if (it.carriage_number != null)
                        choosetime_departingfrom_edittext.setText(it.carriage_number)
                    meetwtable_txt.text =
                            "Водитель встретит Вас с табличкой у вагона поезда. Бесплатное ожидание 20 мин. с момента прибытия поезда.\n" +
                                    "Внимание! Платная парковка не включена в стоимость заказа."
                }


                if (address_type == ChooseTimeFragment.TYPE_ADDRESS) {
                    choosetime_fightnumber_cardview.visibility = View.INVISIBLE
                    choosetime_departingfrom_cardview.visibility = View.INVISIBLE
                    meetwtable_txt.text =
                            "Водитель встретит Вас с табличкой по указанному адресу. Бесплатное ожидание 15 мин. от контрольного времени.\n" +
                                    "Внимание! Платная парковка не включена в стоимость заказа."
                } else if (address_type == ChooseTimeFragment.TYPE_TRAIN) {
                    choosetime_fightnumber_edittext.hint = getString(R.string.train_number)
                    choosetime_departingfrom_edittext.hint = getString(R.string.carriage_number)
                }

                if (it.options!!.meeting_with_a_table != null && meet_w_tableeditText.text.toString().isEmpty()) {
                    MainActivity.logstr("PLATE_NOT_NULL = " + it.options!!.meeting_with_a_table)
                    //var gg = MakeOrderViewModel()!!.makeOrderLiveData!!.value
                    meet_w_tableeditText.setText(it.options!!.meeting_with_a_table)
                }
                updated = true
            }

        })
        meetwtable_btn.setOnClickListener {
            mViewModel!!.setMeetWTableText(meet_w_tableeditText.text.toString())
            if (address_type == ChooseTimeFragment.TYPE_AIRPORT) {
                MainActivity.logstr("SAVE FLIGHT DEPART")
                mViewModel!!.setFlightNumber(choosetime_fightnumber_edittext.text.toString())
                mViewModel!!.setDepartingFrom(choosetime_departingfrom_edittext.text.toString())
            } else if (address_type == ChooseTimeFragment.TYPE_TRAIN) {
                MainActivity.logstr("SAVE TRAIN CARRIAGE")
                mViewModel!!.setTrainNumber(choosetime_fightnumber_edittext.text.toString())
                mViewModel!!.setCarriageNumber(choosetime_departingfrom_edittext.text.toString())
                // mViewModel.refrefData()
            }
            MainActivity.logstr("NEW PLATE = " + meet_w_tableeditText.text.toString())

            // if(meet_w_tableeditText.text.toString().isNotEmpty())mViewModel!!.makeOrderLiveData!!.value!!.options!!.meeting_with_a_table = meet_w_tableeditText.text.toString()
            //else mViewModel!!.makeOrderLiveData!!.value!!.options!!.meeting_with_a_table = null
            activity!!.onBackPressed()
        }
    }


}

package com.avion.app.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.action.MakeOrder
import com.avion.app.action.Option
import com.avion.app.fragment.viewmodel.OrderInfoViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.android.synthetic.main.fragment_order_info.*

class OrderInfoFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        mViewModel.nekFragment = this
        return mViewModel
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
        Log.d("Nek", "Fragment stop")
        orderinfo_progressBar.visibility = View.GONE
    }

    override fun onStartProgress() {
        orderinfo_progressBar.visibility = View.VISIBLE
    }

    private val mViewModel: OrderInfoViewModel by lazy {

        ViewModelProviders.of(activity!!).get(OrderInfoViewModel::class.java)
    }

    private lateinit var makeOrder: MakeOrder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_info, container, false)
    }

    @Throws(Exception::class)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onStopProgress()
        mViewModel.getOrder(arguments!!.getString("order_id")).observe(this, Observer {
            MainActivity.logstr("IN_ORDER: " + arguments!!.getString("order_id") + "\r\n" + arguments!!.getString("json"))
            if (it == null)
                return@Observer

            val om = ObjectMapper()
            if (arguments!!.getString("json") != null) {
                val obj: MakeOrder = om.readValue(arguments!!.getString("json"), MakeOrder::class.java)
                MainActivity.logstr("IN_ORDER_makeOrder_fromJSON: " + obj.id)
                makeOrder = obj
            } else makeOrder = it
            makeorder_select_from_textview.text = makeOrder.from!!.fullAddress
            makeorder_select_to_textview.text = makeOrder.to!!.fullAddress
            orderinfo_num_txtview.text = getString(R.string.order_num) + " " + makeOrder.id
            orderinfo_date_txtview.text = makeOrder.date!!.replace("/", ".") + " " + makeOrder.time

            when (makeOrder.payment!!.type) {
                "100offline" -> {
                    if (makeOrder.bonusPayment != null) {
                        if (makeOrder.bonusPayment == true) {
                            orderinfo_price_txtview.text = makeOrder.payment!!.pay_offline.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                            orderinfo_payed_type_txtview.text = getString(R.string.paybonuses)
                        } else {
                            orderinfo_price_txtview.text = makeOrder.payment!!.pay_offline.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                            orderinfo_payed_type_txtview.text = getString(R.string.nalicknie)
                        }
                    } else {
                        orderinfo_price_txtview.text = makeOrder.payment!!.pay_offline.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                        orderinfo_payed_type_txtview.text = getString(R.string.nalicknie)
                    }
                }
                "100online" -> {
                    if (makeOrder.bonusPayment != null) {
                        if (makeOrder.bonusPayment == true) {
                            orderinfo_price_txtview.text = makeOrder.payment!!.pay_online.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                            orderinfo_payed_type_txtview.text = getString(R.string.paybonuses)
                        } else {
                            orderinfo_price_txtview.text = makeOrder.payment!!.pay_online.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                            orderinfo_payed_type_txtview.text = getString(R.string.prepayment)
                        }
                    } else {
                        orderinfo_price_txtview.text = makeOrder.payment!!.pay_online.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                        orderinfo_payed_type_txtview.text = getString(R.string.prepayment)
                    }
                }
                "5050" -> {
                    orderinfo_price_txtview.text = makeOrder.payment!!.pay_offline.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                    orderinfo_payed_type_txtview.text = getString(R.string.paytodriver)
                }
                "2080" -> {
                    orderinfo_price_txtview.text = makeOrder.payment!!.pay_offline.toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.payment!!.currency.toInt())
                    orderinfo_payed_type_txtview.text = getString(R.string.paytodriver)
                }

            }

            setOptions(it.options!!)
            when {
                makeOrder.status == 0 -> {
                    orderinfo_status_txtview.text = getString(R.string.in_progress)
                    orderinfo_btn2.text = getString(R.string.edit)
                    orderinfo_bnt1.text = getString(R.string.cancel_ride)
                    orderinfo_bnt1.background = resources.getDrawable(R.drawable.border_grey_btn)
                    orderinfo_bnt1.setTextColor(Color.parseColor("#8093A1"))
                    orderinfo_bnt1.setOnClickListener(cancelOrderOnClick)
                    orderinfo_btn2.setOnClickListener(editOrderOnClick)
                }
                makeOrder.status == 1 -> {
                    orderinfo_btn2.setOnClickListener(leveFeedBackOnClick)
                    orderinfo_bnt1.setOnClickListener(repeateOrderOnClick)
                }
                makeOrder.status == 2 -> {
                    orderinfo_status_txtview.text = getString(R.string.canceled)
                    orderinfo_status_txtview.setTextColor(resources.getColor(R.color.red))
                    orderinfo_bnt1.visibility = View.INVISIBLE
                    orderinfo_btn2.setOnClickListener(repeateOrderOnClick)
                }
            }
            @Throws(Exception::class)
            if (makeOrder.driver_id != null) {
                try {
                    setDriverInfo(makeOrder.driver_id!!)

                } catch (e: Exception) {
                }
            } else {
                orderinfo_driver_txtview.text = getString(R.string.no_driver)
                orderinfo_driver_ratingBar.visibility = View.INVISIBLE
                orderinfo_car_name_txtview.text = getString(R.string.no_driver)
                orderinfo_show_driverphone.visibility = View.INVISIBLE
            }
        })

    }

    private val cancelOrderOnClick = View.OnClickListener {
        val url = "https://avion-d0512.firebaseapp.com/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
        //mViewModel.cancelOrder(arguments!!.getString("order_id"))
    }
    private val editOrderOnClick = View.OnClickListener {
        val url = "https://avion-d0512.firebaseapp.com/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
    private val leveFeedBackOnClick = View.OnClickListener {
        Navigation.findNavController(it).navigate(R.id.action_orderInfoFragment_to_feedBackFragment)
    }
    private val repeateOrderOnClick = View.OnClickListener {
        val bundle = Bundle()
        bundle.putSerializable("last_order", makeOrder)
        Navigation.findNavController(it).navigate(R.id.action_orderInfoFragment_to_makeOrderFragment, bundle)
    }

    @Throws(Exception::class)
    @SuppressLint("SetTextI18n")
    private fun setDriverInfo(driverID: String) {
        mViewModel.getDriver(driverID).observe(this, Observer {
            if (it !== null) {
                onStopProgress()
                MainActivity.logstr("DRIVERID: " + driverID)
                var name_slitted = it.family!!.split(" ".toRegex())
                orderinfo_driver_txtview.text = name_slitted[1] + " " + name_slitted[0][0] + "."
                orderinfo_driver_ratingBar.rating = it.rating.toFloat()
                orderinfo_car_name_txtview.text = "${it.abrand} ${it.amodel} (${it.autoColor}, ${it.autoNumber})"

                orderinfo_show_driverphone.setOnClickListener { _ ->
                    orderinfo_show_driverphone.text = "" + it.phone
                    orderinfo_show_driverphone.setOnClickListener { _ ->
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${it.phone}")
                        startActivity(intent)
                    }
                }

            }
        })
    }

    private fun setOptions(options: Option) {
        var optionsTxt = ""
        if (options.meeting_with_a_table != null) {
            optionsTxt += getString(R.string.meeting_with_a_table) + " "
        }
        if (options.child_seat.booster_seat > 0 || options.child_seat.car_cradle > 0 || options.child_seat.child_safety_seat > 0) {
            optionsTxt += getString(R.string.child_seat) + " "
        }
        if (options.car_with_yellow_plates) {
            optionsTxt += getString(R.string.car_with_yellow_plates) + " "
        }
        if (options.driver_language.english || options.driver_language.german || options.driver_language.spain) {
            optionsTxt += getString(R.string.driver_language) + " "
        }
        if (options.smoking_salon) {
            optionsTxt += getString(R.string.smoking_salon) + " "
        }
        if (options.pet_transportation) {
            optionsTxt += getString(R.string.pet_transportation) + " "
        }
        if (options.oversized_baggage) {
            optionsTxt += getString(R.string.oversized_baggage) + " "
        }
        if (options.invoice_for_company) {
            optionsTxt += getString(R.string.invoice_for_company)
        }

        orderinfo_services_txtview.text = optionsTxt
    }


}

package com.avion.app.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.action.MakeOrder
import com.avion.app.adapter.OrderAdapter
import com.avion.app.fragment.viewmodel.OrderListViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_order_list.*

class OrderListFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        mViewModel.nekFragment = this
        return mViewModel
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
        if (orderlist_progressBar !== null)
            orderlist_progressBar.isVisible(false)
    }

    override fun onStartProgress() {
        if (orderlist_progressBar !== null)
            orderlist_progressBar.isVisible(true)
    }

    private val mViewModel: OrderListViewModel by lazy { ViewModelProviders.of(this).get(OrderListViewModel::class.java) }
    private val orderAdapter: OrderAdapter by lazy { OrderAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }

    @Throws(Exception::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onStopProgress()

        orderAdapter.setOnOrderCallBack(object : OrderAdapter.OnOrderCallBack {
            override fun onCancel(makeOrder: MakeOrder) {
                val bundel = Bundle()
                bundel.putString("order_id", makeOrder.id)
                Navigation.findNavController(view).navigate(R.id.action_orderListFragment_to_orderInfoFragment, bundel)
            }

            override fun onEdit(makeOrder: MakeOrder) {
                val url = "https://avion-d0512.firebaseapp.com/"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            @Throws(Exception::class)
            override fun onClick(makeOrder: MakeOrder) {
                val bundel = Bundle()
                MainActivity.logstr("ORDER_ID: " + makeOrder.id)
                bundel.putString("order_id", makeOrder.id)
                bundel.putString("user_phone", makeOrder.user_phone)
                bundel.putString("driver_id", makeOrder.driver_id)
                bundel.putInt("status", makeOrder.status)
                bundel.putString("action", makeOrder.action)
                bundel.putString("from_address", makeOrder.from!!.fullAddress)
                bundel.putString("to_address", makeOrder.to!!.fullAddress)
                bundel.putString("date", makeOrder.date)
                bundel.putString("time", makeOrder.time)
                if (makeOrder.payment != null) {
                    if (makeOrder.payment!!.currency != null) {
                        bundel.putString("currency", makeOrder.payment!!.currency)
                        bundel.putString("type", makeOrder.payment!!.type)
                        bundel.putDouble("pay_offline", makeOrder.payment!!.pay_offline)
                        bundel.putDouble("pay_online", makeOrder.payment!!.pay_online)
                        bundel.putInt("currencyValue", makeOrder.payment!!.currencyValue)
                        // bundel.put
                    }
                }
                // bundel.putString("",makeOrder.)
                //bundel.putString("",makeOrder.)
                val ow = ObjectMapper().writer().withDefaultPrettyPrinter()
                val json = ow.writeValueAsString(makeOrder)
                MainActivity.logstr("JSON: " + json)
                bundel.putString("json", json)
                Navigation.findNavController(view).navigate(R.id.action_orderListFragment_to_orderInfoFragment, bundel)
            }
        })
        mViewModel.list.observe(this, Observer { orderAdapter.submitList(it) })

        orderlist_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                selectedTabIdx = tab.position
                if (tab.position == 0)
                    orderAdapter.setSelectedList(OrderAdapter.SelectedList.upcomming)
                else
                    orderAdapter.setSelectedList(OrderAdapter.SelectedList.complated)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        orderlist_tablayout.getTabAt(selectedTabIdx)?.select()

        orderlist_recycleview.adapter = orderAdapter
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        orderlist_recycleview.layoutManager = linearLayoutManager

    }

    private var selectedTabIdx = 0

    fun View.isVisible(nek: Boolean) {
        if (nek)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE
    }

}

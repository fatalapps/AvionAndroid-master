package com.avion.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.R
import com.avion.app.action.Option
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_cheldren_seats.*


class CheldrenSeatsFragment : NekFragment() {

    override fun onReDoNetworkAction() {

    }

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null)
            mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
        return mViewModel!!
    }

    override fun onStartProgress() {

    }

    override fun onStopProgress() {
    }


    private var mViewModel: MakeOrderViewModel? = null
    private lateinit var options: Option

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cheldren_seats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel!!.makeOrderLiveData.observe(this, Observer {
            options = it!!.options!!
            val child_seat = it.options!!.child_seat
            cheldrenseat_cheldredsafetyseat_countchoosenview.count = child_seat.child_safety_seat
            childrenseat_booster_seat_countchoosenview.count = child_seat.booster_seat
        })
        cheldrenseat_select_btn.setOnClickListener {
            options.child_seat.child_safety_seat = cheldrenseat_cheldredsafetyseat_countchoosenview.count
            options.child_seat.booster_seat = childrenseat_booster_seat_countchoosenview.count
            mViewModel!!.setOptions(options)
            activity!!.onBackPressed()
        }
    }

}

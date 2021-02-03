package com.avion.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.R
import com.avion.app.action.MakeOrder
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_driver_comment.*


class DriverCommentFragment : NekFragment() {
    override fun onReDoNetworkAction() {

    }

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
            mViewModel!!.nekFragment = this
        }
        return mViewModel!!
    }

    override fun onStartProgress() {
    }

    override fun onStopProgress() {
    }

    private var mViewModel: MakeOrderViewModel? = null
    private lateinit var makeOrderModel: MakeOrder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_driver_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel!!.makeOrderLiveData.observe(this, Observer {
            assert(it != null)
            if (it!!.comments_to_the_driver != null)
                drivercomment_edittext.setText(it.comments_to_the_driver!!)

        })
        drivercomment_btn.setOnClickListener {
            //makeOrderModel.comments_to_the_driver = drivercomment_edittext.text.toString()
            if (!drivercomment_edittext.text.toString().isEmpty())
                mViewModel!!.setComments(drivercomment_edittext.text.toString())
            activity!!.onBackPressed()
        }
    }


}

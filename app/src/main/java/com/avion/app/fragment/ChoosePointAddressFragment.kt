package com.avion.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.action.Address
import com.avion.app.action.Geo
import com.avion.app.adapter.AddressPointAdapter
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.models.PointsObj
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_choose_point_address.*


class ChoosePointAddressFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
            mViewModel!!.nekFragment = this
        }
        return mViewModel!!
    }

    override fun onStart() {
        super.onStart()
        mViewModel!!.nekFragment = this
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStartProgress() {
        chooseaddresspoint_progressBar.visibility = View.VISIBLE
    }

    override fun onStopProgress() {
        chooseaddresspoint_progressBar.visibility = View.GONE
    }

    private var mViewModel: MakeOrderViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_point_address, container, false)
    }

    private var pointType: Int = ChooseTimeFragment.TYPE_AIRPORT
    private var ADDRESS_TYPE: Int = PickUpAddressFragment.FROM
    private lateinit var pointsAdapter: AddressPointAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onStopProgress()
        pointType = arguments!!.getInt("pointType", ChooseTimeFragment.TYPE_AIRPORT)
        ADDRESS_TYPE = arguments!!.getInt("address_type")
        if (pointType == ChooseTimeFragment.TYPE_AIRPORT) MainActivity.changeTopName(getString(R.string.airports))
        else if (pointType == ChooseTimeFragment.TYPE_TRAIN) MainActivity.changeTopName(getString(R.string.train))
        pointsAdapter = AddressPointAdapter(activity, PickUpAddressFragment)

        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        linearLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        chooseaddresspoint_recycleview.layoutManager = linearLayoutManager
        chooseaddresspoint_recycleview.adapter = pointsAdapter

        pointsAdapter.setOnPointChoosen { point ->
            val address = Address(point.name, ChooseTimeFragment.intAddressTypeToString(pointType), Geo(0.0, 0.0))
            if (ADDRESS_TYPE == PickUpAddressFragment.FROM) {
                mViewModel!!.setAddressFrom(address)
                mViewModel!!.setParkingPrice(point.parking.toDouble())
            } else {
                mViewModel!!.setAddressTo(address)
            }
        }

        mViewModel!!.makeOrderLiveData.observe(this, Observer {
            val currentAddress: Address? = if (ADDRESS_TYPE == PickUpAddressFragment.FROM) {
                it!!.from
            } else {
                it!!.to
            }

            if (currentAddress != null) {
                pointsAdapter.setChoosenPointName(currentAddress.fullAddress)
            }

        })
        pointsAdapter.setPointsObjList(null)
        mViewModel!!.getInfoRegion.observe(this, Observer {
            if (it == null)
                return@Observer
            val pointsList: List<PointsObj> = if (pointType == ChooseTimeFragment.TYPE_AIRPORT) {
                it.points.airports
            } else {
                it.points.railways
            }
            if (pointsList != null)
                pointsAdapter.setPointsObjList(pointsList)
        })


    }


}

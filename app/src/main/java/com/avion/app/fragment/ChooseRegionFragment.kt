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
import com.avion.app.adapter.RegionsAdapter
import com.avion.app.entity.RegionEntity
import com.avion.app.fragment.viewmodel.ChooseRegionViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.fragment_choose_region.*
import rx.android.schedulers.AndroidSchedulers

class ChooseRegionFragment : NekFragment() {

    override fun onReDoNetworkAction() {

    }

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(ChooseRegionViewModel::class.java)
        }
        return mViewModel!!
    }

    override fun onStart() {
        super.onStart()
        mViewModel!!.nekFragment = this
    }

    override fun onStopProgress() {
    }

    override fun onStartProgress() {
    }

    private lateinit var regionAdater: RegionsAdapter

    private var mViewModel: ChooseRegionViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // activity!!.actionBar.title = getString(R.string.chooseReg)
        // activity!!.setTitle(getString(R.string.chooseReg))
        return inflater.inflate(R.layout.fragment_choose_region, container, false)
    }

    private var selectedRegion: RegionEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        selectedRegion = MainActivity.chooseRegionLiveData.value

        regionAdater = RegionsAdapter(activity)
        regionAdater.setGetRegion { regionEntity ->
            selectedRegion = regionEntity
        }
        mViewModel!!.list("").observe(this, Observer {
            it.forEach {
                System.out.println("FROM_FRAGMENT: " + it.name + " " + it.order)
            }
            regionAdater.submitList(it)
        })
        //mViewModel!!.getRegionsFromModel("").observe(this, Observer {  MainActivity.logstr("REGIONS_IN_ChooseRegFrag\r\n"+it.toString())  regionAdater.submitList(it) })
        val linerLayoutManeger = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        //regionAdater.setHasStableIds(true)
        linerLayoutManeger.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        chooseregion_recycleview.layoutManager = linerLayoutManeger
        chooseregion_recycleview.itemAnimator = null
        chooseregion_recycleview.adapter = regionAdater

        RxTextView.textChangeEvents(chooseregion_searcheditext)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    MainActivity.logstr("KEY- " + it.text().toString())
                    regionAdater.submitList(null)

                    mViewModel!!.list(it.text().toString()).observe(this, Observer {
                        val iter = it.iterator()
                        var y = 0
                        iter.forEach {
                            MainActivity.logstr(it.name + " " + y)
                            y++
                        }
                        regionAdater.submitList(it)
                    })

                }


    }


}

package com.avion.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.avion.app.PhoneCodesMaster
import com.avion.app.R
import com.avion.app.adapter.CountriesAdapter
import com.avion.app.entity.CountryEntity
import com.avion.app.fragment.viewmodel.ChooseRegionViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel

class ChooseCountryFragment : NekFragment() {
    var observer: Observer<PagedList<CountryEntity>>? = null
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

    private lateinit var regionAdater: CountriesAdapter

    private var mViewModel: ChooseRegionViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_country, container, false)
    }

    private var selectedRegion: CountryEntity? = null
    var bound: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // selectedRegion = MainActivity.chooseRegionLiveData.value
        regionAdater = CountriesAdapter(activity)

        regionAdater.setGetRegion { regionEntity ->
            selectedRegion = regionEntity
        }

        var phmaster = PhoneCodesMaster(context, activity!!.application, mViewModel)

    }
}





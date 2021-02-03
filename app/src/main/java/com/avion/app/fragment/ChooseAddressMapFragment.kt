package com.avion.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.avion.app.R
import com.avion.app.unit.AutoSuggestAdapter
import com.avion.app.unit.GetAddtessList
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.fragment_choose_address_map.*

class ChooseAddressMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var myPossitionMaker: Marker

    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
        chooseaddress_seacher_autocomplatetextview.isEnabled = true
    }

    companion object {
        val ADDRESS_FROM_TYPE = 1
        val ADDRESS_TO_TYPE = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_address_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.chooseadress_mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        chooseadress_confire_btn.isEnabled = false
        chooseaddress_seacher_autocomplatetextview.isEnabled = false

        val autoAddressTextAdapter = AutoSuggestAdapter(this.context!!, android.R.layout.simple_dropdown_item_1line)
        chooseaddress_seacher_autocomplatetextview.setAdapter(autoAddressTextAdapter)

        val getAddressQuary = GetAddtessList(this.context!!, GetAddtessList.Get_list { adress_list ->
            run {
                autoAddressTextAdapter.clear()
                autoAddressTextAdapter.setData(adress_list)
                autoAddressTextAdapter.notifyDataSetChanged()
            }
        })

        RxTextView.textChangeEvents(chooseaddress_seacher_autocomplatetextview)
                .filter { e -> e.text().toString().length > 4 }
                .subscribe { e -> getAddressQuary.load(e.text().toString()) }

    }

    private fun moveMyPossitionMarker(possition: LatLng) {
        if (myPossitionMaker == null) {
            val markerOption = MarkerOptions()
                    .position(possition)
                    .title("")
            myPossitionMaker = mMap.addMarker(markerOption)
        }
        myPossitionMaker.position = possition
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(possition, 14.0f))
    }


}

package com.avion.app.fragment

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.action.Address
import com.avion.app.action.Geo
import com.avion.app.adapter.AutoSuggestAdapterNew
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.fragment.viewmodel.PickUpAddressViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_pick_up_address.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class PickUpAddressFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        if (pickUpAddressViewModel == null) {
            pickUpAddressViewModel = ViewModelProviders.of(activity!!).get(PickUpAddressViewModel::class.java)
        }
        return pickUpAddressViewModel!!
    }

    override fun onStart() {
        super.onStart()
        pickUpAddressViewModel!!.nekFragment = this
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
    }

    override fun onStartProgress() {
    }

    companion object {
        val FROM = 0
        val TO = 1
        var wasChoosePoint = false
    }

    private lateinit var mViewModel: MakeOrderViewModel
    private var pickUpAddressViewModel: PickUpAddressViewModel? = null

    private var ADDRESS_TYPE: Int = FROM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.avion.app.R.layout.fragment_pick_up_address, container, false)
    }

    private val handler = Handler(Handler.Callback { msg ->
        if (msg.what == 100) {
            if (pickupaddress_address_autoCompleteTextView == null)
                return@Callback false
            if (!TextUtils.isEmpty(pickupaddress_address_autoCompleteTextView.text)) {
                pickUpAddressViewModel!!.quaryAddress(pickupaddress_address_autoCompleteTextView.text.toString()) { address_list ->
                    MainActivity.logstr("ADDRESSES COUNT: " + address_list.size)
                    quaryAddressAdapter.setData(address_list)
                    quaryAddressAdapter.notifyDataSetChanged()
                }
            }
        }
        return@Callback false
    })

    private lateinit var quaryAddressAdapter: AutoSuggestAdapterNew

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ADDRESS_TYPE = arguments!!.getInt("address_type")
        mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)

        quaryAddressAdapter = AutoSuggestAdapterNew(context!!, R.layout.item_region)


        pickupaddress_address_autoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (pickupaddress_address_autoCompleteTextView.text.length > 0) {
                    pickupaddress_list_recycleview.visibility = View.VISIBLE
                    handler.removeMessages(100)
                    handler.sendEmptyMessageDelayed(100, 300)
                } else {
                    pickupaddress_list_recycleview.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        pickupaddress_airport_textView.setOnClickListener {
            arguments!!.putInt("pointType", ChooseTimeFragment.TYPE_AIRPORT)
            Navigation.findNavController(view).navigate(R.id.action_pickUpAddressFragment_to_choosePointAddressFragment, arguments!!)
        }

        pickupaddress_train_textview.setOnClickListener {
            arguments!!.putInt("pointType", ChooseTimeFragment.TYPE_TRAIN)
            Navigation.findNavController(view).navigate(R.id.action_pickUpAddressFragment_to_choosePointAddressFragment, arguments!!)
        }
        pickupaddress_faworit_txtview.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_pickUpAddressFragment_to_favoriteAddressListFragment, arguments)
        }

        val uiHandler = Handler()
        uiHandler.post {
            if (wasChoosePoint) {
                activity!!.onBackPressed()
                wasChoosePoint = false
            }
        }

        pickupaddress_clear_search_imageview.visibility = View.INVISIBLE
        pickupaddress_clear_search_imageview.setOnClickListener {
            pickupaddress_address_autoCompleteTextView.text.clear()
        }

        if (ADDRESS_TYPE == FROM) {
            if (mViewModel.makeOrderLiveData.value!!.from != null && mViewModel.makeOrderLiveData.value!!.from!!.type == ChooseTimeFragment.intAddressTypeToString(ChooseTimeFragment.TYPE_ADDRESS)) {
                pickupaddress_address_autoCompleteTextView.setText(mViewModel.makeOrderLiveData.value!!.from!!.fullAddress)
                pickupaddress_clear_search_imageview.visibility = View.VISIBLE
            }
        } else {
            if (mViewModel.makeOrderLiveData.value!!.to != null) {
                pickupaddress_address_autoCompleteTextView.setText(mViewModel.makeOrderLiveData.value!!.to!!.fullAddress)
                pickupaddress_clear_search_imageview.visibility = View.VISIBLE
            }
        }

        pickupaddress_list_recycleview.adapter = quaryAddressAdapter
        pickupaddress_list_recycleview.setOnItemClickListener { _, _, possition, _ ->
            pickupaddress_address_autoCompleteTextView.setText(quaryAddressAdapter.getItem(possition))
            val txt_ad = pickupaddress_address_autoCompleteTextView.text.toString()
            var url_nek = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDMkIGTf-e5K5wdC5OejnYsqV9civVRMzI&input=" + txt_ad.replace(" ,", ",")
            MainActivity.logstr("ADDRESS:\r\n" + pickupaddress_address_autoCompleteTextView.text.toString())
            val queue = Volley.newRequestQueue(context)
            val jsonObjectRequest1: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url_nek, null,
                    Response.Listener { response: JSONObject ->
                        try {
                            val suggestions = response.getJSONArray("predictions")
                            for (i in 0 until suggestions.length()) {
                                val jsonObject = suggestions.getJSONObject(i)
                                val value = jsonObject.getString("description")
                                val place_id = jsonObject.getString("place_id")
                                MainActivity.logstr("PLACE_ID:\r\n" + place_id)
                                url_nek = "https://maps.googleapis.com/maps/api/place/details/json?placeid=$place_id&key=AIzaSyDMkIGTf-e5K5wdC5OejnYsqV9civVRMzI"
                                val jsonObjectRequest2: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url_nek, null,
                                        Response.Listener { response: JSONObject ->
                                            try {
                                                val res = response.getJSONObject("result")
                                                val loc = res.getJSONObject("geometry").getJSONObject("location")
                                                MainActivity.logstr("RESULT:\r\n" + loc.toString() + " " + loc.get("lat"))
                                                val address = Address(
                                                        txt_ad,
                                                        ChooseTimeFragment.intAddressTypeToString(ChooseTimeFragment.TYPE_ADDRESS),
                                                        Geo(loc.getDouble("lng"), loc.getDouble("lat")))
                                                if (ADDRESS_TYPE == FROM) {
                                                    mViewModel.setAddressFrom(address)

                                                } else {
                                                    mViewModel.setAddressTo(address)
                                                }

                                            } catch (e: JSONException) {
                                                e.printStackTrace()
                                            }
                                        }, Response.ErrorListener { error: VolleyError? -> }) {
                                    @Throws(AuthFailureError::class)
                                    override fun getHeaders(): Map<String, String> {
                                        val headers: MutableMap<String, String> = HashMap()
                                        headers["Authorization"] = "Token c45f9d716f320f1d803ead88aef6ce87346504e5"
                                        headers["Content-Type"] = "application/json"
                                        headers["Accept"] = "application/json"
                                        return headers
                                    }
                                }

                                queue.add(jsonObjectRequest2)

                                break
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { error: VolleyError? -> MainActivity.logstr("ERROR_ADDRESS:\r\n" + error.toString()) }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token c45f9d716f320f1d803ead88aef6ce87346504e5"
                    headers["Content-Type"] = "application/json"
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            queue.add(jsonObjectRequest1)

            activity!!.onBackPressed()
        }


    }

    private fun <T, R> List<T>.nek(transform: (T) -> R, predicate: (T) -> Boolean): List<R> {
        val list: ArrayList<R> = ArrayList()
        for (item in this) {
            if (predicate(item))
                list.add(transform(item))
        }
        return list
    }

}

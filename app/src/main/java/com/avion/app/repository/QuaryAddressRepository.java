package com.avion.app.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avion.app.MainActivity;
import com.avion.app.database.MyDB;
import com.avion.app.database.MyDao;
import com.avion.app.web.DadataService;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuaryAddressRepository {

    private MyDao myDao;
    private DadataService dadataService;

    public QuaryAddressRepository(Application application, LifecycleOwner lifecycleOwner, Observer<Boolean> onNetworErrorObserv) {
        myDao = MyDB.getDatabase(application.getApplicationContext()).myDao();
        queue = Volley.newRequestQueue(application.getApplicationContext());
    }

    public QuaryAddressRepository(Application application) {
        myDao = MyDB.getDatabase(application.getApplicationContext()).myDao();

        queue = Volley.newRequestQueue(application.getApplicationContext());
    }

    public QuaryAddressRepository(Context context) {
//        super();
        myDao = MyDB.getDatabase(context).myDao();

        queue = Volley.newRequestQueue(context);
    }

    private RequestQueue queue;

    public interface OnCallList {
        void done(List<String> address_list);
    }

    public synchronized void getAdressList(String address, OnCallList onCallList) {
        //final MutableLiveData<List<String>> list = new MutableLiveData<>();

        Logger.d("getAdressList " + address);

        List<String> address_list = new ArrayList<>();

        JSONObject params = new JSONObject();
        try {
            params.put("query", address);
            params.put("count", 10);
            JSONArray locations = new JSONArray();
            JSONObject region = new JSONObject();
            region.put("region", MainActivity.chooseRegionLiveData.getValue().getName());
            //locations.put(region);
            JSONObject countr = new JSONObject();
            countr.put("country", "*");
            locations.put(countr);
            params.put("locations", locations);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url_nek = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDMkIGTf-e5K5wdC5OejnYsqV9civVRMzI&language=ru&input=" + address;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_nek, null,
                response -> {
                    try {
                        JSONArray suggestions = response.getJSONArray("predictions");
                        for (int i = 0; i < suggestions.length(); i++) {
                            JSONObject jsonObject = suggestions.getJSONObject(i);
                            String value = jsonObject.getString("description");
                            address_list.add(value);
                        }
                        //list.setValue(address_list);
                        onCallList.done(address_list);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token c45f9d716f320f1d803ead88aef6ce87346504e5");
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);

        //return list;
    }

}

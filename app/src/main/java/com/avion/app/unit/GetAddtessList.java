package com.avion.app.unit;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avion.app.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAddtessList {

    public interface Get_list {
        void data(List<String> adress_list);
    }

    private Context context;
    private Get_list get_list;
    private RequestQueue queue;

    public GetAddtessList(Context context, Get_list get_list) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        this.get_list = get_list;
    }

    private List<String> address_list = new ArrayList<>();

    public void loadFullAddress(String quary) {
        address_list = new ArrayList<>();
        String url_nek = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address";
        try {
            JSONObject params = new JSONObject();
            params.put("query", quary);
            params.put("count", 10);
            JSONArray locat = new JSONArray();
            JSONObject countr = new JSONObject();
            countr.put("country", "*");
            locat.put(countr);
            params.put("locations", locat);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_nek, params,
                    response -> {

                        Log.d("NekAddress", response.toString());

                        try {
                            MainActivity.logstr("DADATA:\r\n" + params.toString());
                            JSONArray suggestions = response.getJSONArray("suggestions");

                            for (int i = 0; i < suggestions.length(); i++) {
                                JSONObject jsonObject = suggestions.getJSONObject(i);
                                String value = jsonObject.getString("value");
                                address_list.add(value);
                            }

                            get_list.data(address_list);

                        } catch (JSONException e) {
                            Log.d("NekAddress", "json error " + e.getMessage());
                            e.printStackTrace();
                            get_list.data(address_list);
                        }


                    }, error -> {
                Log.d("NekAddress", "conncet error " + error.getMessage());
                get_list.data(address_list);
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();

                    headers.put("Authorization", "Token 0accb9d5c777577b75b52e566b2cc727409854ef");
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");

                    return headers;
                }
            };

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void load(String quary) {

        address_list = new ArrayList<>();

        String url_nek = "http://kladr-api.ru/api.php?contentType=street&oneString=1&limit=50&query=" + quary;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_nek, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    int size = jsonArray.length();

                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fullName = jsonObject.getString("fullName");

                        fullName = fullName.replace("Город", "г.");
                        fullName = fullName.replace("Улица", "ул.");
                        fullName = fullName.replace("Республика", "р.");
                        fullName = fullName.replace("Село", "c.");

                        address_list.add(fullName);
                    }

                    get_list.data(address_list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                get_list.data(address_list);
            }
        });

        queue.add(jsonObjectRequest);

    }
}

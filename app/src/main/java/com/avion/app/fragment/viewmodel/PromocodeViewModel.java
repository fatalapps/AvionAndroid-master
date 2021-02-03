package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avion.app.MainActivity;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PromocodeViewModel extends NekViewModel {
    public OkHttpClient client;
    public MutableLiveData<String> livePromoBalance = new MutableLiveData<>();
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public PromocodeViewModel(@NonNull Application application) {

        super(application);
    }

    public MutableLiveData<String> livePromo = new MutableLiveData<>();
    public MutableLiveData<JSONObject> liveActivatePromo = new MutableLiveData<>();

    @Override
    public void onReDoTask() {

    }

    public LiveData<String> getLivePromo() {
        client = new OkHttpClient();
        String resp = null;
        try {
            JSONObject json = new JSONObject()
                    .put("action", "generatePromo")
                    .put("phone", NekConfigs.getUserPgone().replace("+", ""))
                    .put("name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                    .put("source", "android");
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    MainActivity.logstr("PROMO_REQ_ERROR: " + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String resp = response.body().string();
                            MainActivity.logstr("PROMO_RESP: " + resp);
                            livePromo.postValue(new JSONObject(resp).getString("promo"));
                        } catch (Exception e) {
                            MainActivity.logstr("PROMO_JSON_ERROR: " + e.toString());
                        }
                    } else {
                        MainActivity.logstr("PROMO_RESP_ERROR: " + response.body().string());

                    }
                }
            });
        } catch (Exception e) {
            MainActivity.logstr("INIT_ERROR");
        }
        return livePromo;
    }

    public LiveData<JSONObject> getLiveActivatePromo(String promo) {
        client = new OkHttpClient();
        String resp = null;
        try {
            JSONObject json = new JSONObject()
                    .put("action", "activatePromo")
                    .put("phone", NekConfigs.getUserPgone().replace("+", ""))
                    .put("promo", promo);
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    MainActivity.logstr("PROMO_REQ_ERROR: " + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    // MainActivity.updateBalance();
                    if (response.isSuccessful()) {
                        try {
                            String resp = response.body().string();
                            MainActivity.logstr("PROMO_RESP: " + resp);
                            liveActivatePromo.postValue(new JSONObject(resp));

                        } catch (Exception e) {
                            MainActivity.logstr("PROMO_JSON_ERROR: " + e.toString());
                        }
                    } else {
                        MainActivity.logstr("PROMO_RESP_ERROR: " + response.body().string());

                    }
                }
            });


        } catch (Exception e) {
            MainActivity.logstr("INIT_ERROR");
        }
        return liveActivatePromo;
    }

    public LiveData<String> getLivePromoBalance() {
        OkHttpClient client = new OkHttpClient();
        String resp = null;
        try {
            JSONObject json = new JSONObject()
                    .put("action", "getClientBonus")
                    .put("phone", NekConfigs.getUserPgone().replace("+", ""));
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    MainActivity.logstr("PROMO_REQ_ERROR: " + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String resp = response.body().string();
                            MainActivity.logstr("PROMO_RESP: " + resp);
                            livePromoBalance.postValue(new JSONObject(resp).getString("bonus"));
                        } catch (Exception e) {
                            MainActivity.logstr("PROMO_JSON_ERROR: " + e.toString());
                        }
                    } else {
                        MainActivity.logstr("PROMO_RESP_ERROR: " + response.body().string());

                    }
                }
            });
        } catch (Exception e) {
            MainActivity.logstr("INIT_ERROR");
        }
        return livePromoBalance;
    }
}

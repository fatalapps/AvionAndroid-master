package com.avion.app.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avion.app.MainActivity;
import com.avion.app.models.User;
import com.avion.app.unit.NekConfigs;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserRepository {

    private MutableLiveData<User> userLiveData;
    private FirebaseFirestore db;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public MutableLiveData<String> livePromoBalance = new MutableLiveData<>();

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
    }

    public MutableLiveData<User> getUser(String id) {
        userLiveData = new MutableLiveData<>();
        db.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener(task -> {
                    if (task != null)
                        userLiveData.setValue(task.toObject(User.class));
                    else
                        userLiveData = addUser(id);

                })
                .addOnFailureListener(e -> {
                    MainActivity.logstr("LOW_CONNECTION_SPEED " + e.toString());
                    db.terminate();
                    db = FirebaseFirestore.getInstance();
                    db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                            .setPersistenceEnabled(true)
                            .build());
                    getUser(id);
                });

        return userLiveData;
    }

    private MutableLiveData<User> addUser(String id) {
        final User user = new User();
        user.setPromocode(NekConfigs.getAlphaNumericString(5));
        userLiveData = new MutableLiveData<>();
        db.collection("users")
                .document(id)
                .set(user)
                .addOnCompleteListener(task -> {
                    userLiveData = getUser(id);
                })
                .addOnFailureListener(e -> {
                });
        return userLiveData;
    }

    public void refisterFirebase(String token) {
        Map<String, Object> mapN = new HashMap<>();
        mapN.put("token", token);
        db.collection("users_tokens")
                .document(NekConfigs.getUserPgone())
                .set(mapN)
                .addOnCompleteListener(task -> {
                    Log.d("NekFCM", "Add to db ok");
                })
                .addOnFailureListener(e -> {
                    Log.e("NekFCM", "Add to db problem " + e.getLocalizedMessage());
                });
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
                            String bonuses = new JSONObject(resp).getString("bonus");
                            livePromoBalance.postValue((bonuses.equals("null") || bonuses == null || bonuses.length() == 0) ? "0" : bonuses);
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

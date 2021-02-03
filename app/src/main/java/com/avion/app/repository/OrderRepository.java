package com.avion.app.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avion.app.MainActivity;
import com.avion.app.action.MakeOrder;
import com.avion.app.models.NewOrderResponse;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;
import com.avion.app.web.OrderService;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class OrderRepository extends NekRepository {

    private OrderService orderService;

    SharedPreferences prefs;

    public OrderRepository(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NekConfigs.apiURL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        orderService = retrofit.create(OrderService.class);
        prefs = application.getApplicationContext().getSharedPreferences("Neka", MODE_PRIVATE);
    }

    public LiveData<String> makeOrder(MakeOrder makeOrder) {
        final MutableLiveData orderID = new MutableLiveData();
        startProgress();
        orderService.addNewOrder(makeOrder).enqueue(new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(Call<NewOrderResponse> call, Response<NewOrderResponse> response) {

                if (response.body().getError() == null) {
                    if (response.body() == null) {
                        showNetworkerror();
                        return;
                    }
                    if (!response.body().isSuccess()) {
                        showNetworkerror();
                        return;
                    }
                    makeOrder.setId(response.body().getId());
                    try {
                        db.collection("orders")
                                .document(makeOrder.getId())
                                .set(makeOrder)
                                .addOnSuccessListener(task -> {
                                    Logger.d("addOnSuccessListener");
                                    stopPrograss();
                                    orderID.setValue(makeOrder.getId());
                                })
                                .addOnFailureListener(e -> {
                                    Logger.d("addOnFailureListener");
                                    showNetworkerror();
                                });
                    } catch (Exception e) {
                        MainActivity.logstr("MAKE_O_EXC: " + e.toString());
                    }
                } else {
                    MainActivity.logstr("Error: " + response.body().getError());
                    orderID.setValue(response.body().getError());
                }

            }

            @Override
            public void onFailure(Call<NewOrderResponse> call, Throwable t) {
                showNetworkerror();
            }
        });
        return orderID;
    }

    //@Exc
    @SuppressLint("SimpleDateFormat")
    public LiveData<List<MakeOrder>> getList() throws Exception {
        final MutableLiveData<List<MakeOrder>> data = new MutableLiveData<>();
        startProgress();
        db.collection("orders")
                //.whereEqualTo("user_phone", NekConfigs.getUserPgone())
                .get()
                .addOnCompleteListener(task -> {
                    List<MakeOrder> makeOrderList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Logger.d(documentSnapshot.getData().toString());
                        MakeOrder makeOrder = documentSnapshot.toObject(MakeOrder.class);
                        Logger.d("MAKEORDER: " + makeOrder.toString());
                        makeOrder.setId(documentSnapshot.getId());
                        if (makeOrder.getUser_phone().equals(NekConfigs.getUserPgone())) {
                            makeOrderList.add(makeOrder);
                        }
                    }
                    Logger.d("orders size " + makeOrderList.size());
                    Collections.sort(makeOrderList, (o1, o2) -> {
                        try {
                            Logger.d("o1:" + o1.getId() + " o2: " + o2.getId());
                            if (o1.getDate() != null && o2.getDate() != null) {
                                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate());
                                Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate());
                                return date1.compareTo(date2);
                            } else return 1;
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    });
                    data.setValue(makeOrderList);
                    stopPrograss();
                })
                .addOnFailureListener(e -> showNetworkerror());

        return data;
    }

    public LiveData<MakeOrder> getOrder(String id) {
        final MutableLiveData<MakeOrder> data = new MutableLiveData<>();
        startProgress();
        db.collection("orders")
                .document(id)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        showNetworkerror();
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        MakeOrder makeOrder = documentSnapshot.toObject(MakeOrder.class);
                        makeOrder.setId(documentSnapshot.getId());
                        data.setValue(makeOrder);
                        stopPrograss();
                    } else
                        showNetworkerror();
                });
        return data;
    }

    public void cancelOrder(String id) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", 2);
        startProgress();
        // TODO (Send POst to Avion Server)
        db.collection("orders")
                .document(id)
                .update(updateData)
                .addOnCompleteListener(task -> stopPrograss())
                .addOnFailureListener(e -> showNetworkerror());
    }

    public Float getDriverRate(String id) {
        return prefs.getFloat("driver_rate_" + id, 0);
    }

    public void setDriverRate(String id, Float rate) {
        prefs.edit().putFloat("driver_rate_" + id, rate).apply();
    }

    public Float getCareRate(String id) {
        return prefs.getFloat("car_rate_" + id, 0);
    }

    public void setCarRate(String id, Float rate) {
        prefs.edit().putFloat("car_rate_" + id, rate).apply();
    }

    public String getComment(String id) {
        return prefs.getString("comment_order_" + id, "");
    }

    public void setComment(String id, String rate) {
        prefs.edit().putString("comment_order_" + id, rate).apply();
    }

}

package com.avion.app.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avion.app.action.DriverRequest;
import com.avion.app.action.RateDriverRequsest;
import com.avion.app.action.Rating;
import com.avion.app.action.SimpleResponse;
import com.avion.app.entity.DriverEntity;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;
import com.avion.app.web.DriverService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverRepository extends NekRepository {

    private DriverService driverService;

    public DriverRepository(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NekConfigs.apiURL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        driverService = retrofit.create(DriverService.class);

    }

    public LiveData<DriverEntity> getDriver(String id) {
        startProgress();
        final MutableLiveData<DriverEntity> driverEntityMutableLiveData = new MutableLiveData<>();
        driverService.getDriver(new DriverRequest("getDriverInfo", id)).enqueue(new Callback<DriverEntity>() {
            @Override
            public void onResponse(Call<DriverEntity> call, Response<DriverEntity> response) {
                Log.d("Nek", "getDriver stop");
                //stopPrograss();
                if (response.body() != null) {
                    driverEntityMutableLiveData.setValue(response.body());
                } else {
                    showNetworkerror();
                }
            }

            @Override
            public void onFailure(Call<DriverEntity> call, Throwable t) {
                showNetworkerror();
            }
        });
        return driverEntityMutableLiveData;
    }

    public LiveData<Boolean> rateDriver(String order_id, String comment, Double rate) {
        startProgress();
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        driverService.reateDriver(new RateDriverRequsest("rate", order_id, new Rating(comment, rate))).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Log.d("Nek", "rateDriver done");
                //stopPrograss();
                data.setValue(true);
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                showNetworkerror();
            }
        });
        return data;
    }

}

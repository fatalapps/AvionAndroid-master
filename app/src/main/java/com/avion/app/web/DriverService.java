package com.avion.app.web;

import com.avion.app.action.DriverRequest;
import com.avion.app.action.RateDriverRequsest;
import com.avion.app.action.SimpleResponse;
import com.avion.app.entity.DriverEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DriverService {
    @POST(".")
    Call<DriverEntity> getDriver(@Body DriverRequest driverRequest);

    @POST(".")
    Call<SimpleResponse> reateDriver(@Body RateDriverRequsest rateDriverRequsest);
}

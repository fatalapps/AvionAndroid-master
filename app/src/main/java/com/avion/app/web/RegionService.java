package com.avion.app.web;

import com.avion.app.models.GetInfoRegion;
import com.avion.app.models.GetInfoReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegionService {
    @POST(".")
    Call<GetInfoRegion> region_info(@Body GetInfoReq getInfoReq);
}

package com.avion.app.web;

import com.avion.app.action.CalcPrice;
import com.avion.app.action.TarifPricesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TarifService {
    @POST(".")
    Call<TarifPricesResponse> tarifPrices(@Body CalcPrice calcPrice);
}

package com.avion.app.web;

import com.avion.app.action.MakeOrder;
import com.avion.app.models.NewOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderService {
    @POST(".")
    Call<NewOrderResponse> addNewOrder(@Body MakeOrder makeOrder);
}

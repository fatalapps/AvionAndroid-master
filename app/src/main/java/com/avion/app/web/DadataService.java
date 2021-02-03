package com.avion.app.web;

import com.avion.app.models.QuaryAddressRequest;
import com.avion.app.models.QuaryAddressResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DadataService {
    @Headers({
            "Authorization: Token c45f9d716f320f1d803ead88aef6ce87346504e5",
            "Content-Type: application/json"
    })
    @POST("/suggest/address")
    Call<QuaryAddressResponse> quaryAddress(@Body QuaryAddressRequest quaryAddressRequest);
}

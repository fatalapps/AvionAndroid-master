package com.avion.app.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avion.app.MainActivity;
import com.avion.app.action.CalcPrice;
import com.avion.app.action.TarifPriceObj;
import com.avion.app.action.TarifPricesResponse;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;
import com.avion.app.web.TarifService;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TarifRespository extends NekRepository {

    private TarifService tarifService;

    public TarifRespository(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NekConfigs.apiURL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tarifService = retrofit.create(TarifService.class);

    }

    public LiveData<TarifPriceObj> getPrices(CalcPrice calcPrice) {
        final MutableLiveData<TarifPriceObj> tarifPriceData = new MutableLiveData<>();
        Logger.json(new Gson().toJson(calcPrice));
        tarifService.tarifPrices(calcPrice).enqueue(new Callback<TarifPricesResponse>() {
            @Override
            public void onResponse(Call<TarifPricesResponse> call, Response<TarifPricesResponse> response) {
                Logger.d(response.message());
                if (response.body() != null) {
                    TarifPricesResponse bb = response.body();
                    Gson gson = new Gson();
                    MainActivity.logstr("TARIF_RESPONSE: \r\n" + gson.toJson(bb));
                    if (gson.toJson(bb).length() == 0) {
                        Toast.makeText(MainActivity.getAppContext(), "По данному маршруту нельзя оформить заказ", Toast.LENGTH_SHORT).show();

                    }
                    TarifPriceObj obbj = response.body().getPrice();
                    tarifPriceData.setValue(obbj);

                }
                stopPrograss();
            }

            @Override
            public void onFailure(Call<TarifPricesResponse> call, Throwable t) {
                Logger.e(t.getMessage());
                showNetworkerror();
            }
        });
        return tarifPriceData;
    }

}

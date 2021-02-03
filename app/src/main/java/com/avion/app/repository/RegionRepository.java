package com.avion.app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.android.volley.Request;
import com.avion.app.MainActivity;
import com.avion.app.entity.RegionEntity;
import com.avion.app.models.GetInfoRegion;
import com.avion.app.models.GetInfoReq;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;
import com.avion.app.web.RegionService;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegionRepository extends NekRepository {


    private LiveData<PagedList<RegionEntity>> regionsList;
    private MutableLiveData<GetInfoRegion> getInfoRegionLiveData = new MutableLiveData<>();
    private RegionService regionService;
    //private final MutableLiveData<Boolean> isNetworkErorr = new MutableLiveData<>();

    public RegionRepository(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NekConfigs.apiURL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        regionService = retrofit.create(RegionService.class);
    }

    private void reloadData(String nname) {
        startProgress();
        JSONObject params = new JSONObject();
        try {
            params.put("action", "getFullRegionList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(Request.Method.POST, NekConfigs.apiURL, params,
                response -> {
                    try {
                        List<RegionEntity> regionEntityList = new ArrayList<>();
                        JSONArray array = response;
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject region = array.getJSONObject(i);
                                RegionEntity reg_o = new RegionEntity();
                                reg_o.setName(region.getString("nameTerm"));
                                reg_o.order = i;
                                MainActivity.logstr("FROM_RESP: " + region.getString("nameTerm") + reg_o.order);
                                reg_o.setId(region.getInt("termID"));
                                //if(region.getString("nameTerm").toLowerCase().indexOf(nname.toLowerCase())!=-1)

                                if (nname.trim().length() >= 1) {
                                    Logger.e(nname + " " + "FILTER");
                                    if (region.getString("nameTerm").toLowerCase().indexOf(nname.toLowerCase()) != -1)
                                        regionEntityList.add(reg_o);
                                } else {
                                    regionEntityList.add(reg_o);
                                }
                            }
                        }

                        for (int f = 0; f < regionEntityList.size(); f++)
                            System.out.println("FROM_LIST: " + regionEntityList.get(f).getName() + " " + regionEntityList.get(f).order);
                        saveRegions(() -> myDao.regionsSaveAll(regionEntityList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    stopPrograss();
                },
                error -> {
                    showNetworkerror();
                    stopPrograss();
                });
        queue.add(jsonObjectRequest);
    }

    private void saveRegions(Action action) {
        Completable.fromAction(action)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.d("Start save region");
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("Done save region");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("Error save region " + e.getMessage());
                    }
                });
    }

    public LiveData<PagedList<RegionEntity>> getRegionsList(String name) {
        String old_name = name;
        name = "%" + name + "%";
        Logger.e("SEARCH:\r\n" + old_name);
        // myDao.
        reloadData(old_name);
        regionsList = new LivePagedListBuilder<>(myDao.regionList(name), 29).build();

        return regionsList;
    }

    public MutableLiveData<GetInfoRegion> getInfoRegionLiveData(int regionId) {
        startProgress();
        regionService.region_info(new GetInfoReq(regionId)).enqueue(new Callback<GetInfoRegion>() {
            @Override
            public void onResponse(Call<GetInfoRegion> call, Response<GetInfoRegion> response) {
                stopPrograss();
                Logger.d(regionId + " REGION_INFO:\r\n" + new Gson().toJson(response.body()) + response.raw());
                getInfoRegionLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GetInfoRegion> call, Throwable t) {
                Logger.e(t.getMessage());
                stopPrograss();
                showNetworkerror();
            }
        });


        return getInfoRegionLiveData;
    }

}

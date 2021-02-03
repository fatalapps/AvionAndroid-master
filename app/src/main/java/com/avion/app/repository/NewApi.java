package com.avion.app.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.avion.app.MainActivity;
import com.avion.app.entity.Currency;
import com.avion.app.entity.RegionEntity;
import com.avion.app.models.DopUslugi;
import com.avion.app.models.DopUslugiObjs;
import com.avion.app.models.GetInfoRegion;
import com.avion.app.models.Points;
import com.avion.app.models.PointsObj;
import com.avion.app.models.Tarif;
import com.avion.app.models.TarifObj;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewApi extends NekRepository implements LifecycleOwner {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final MutableLiveData<JSONArray> liveRegions = new MutableLiveData<>();
    final MutableLiveData<GetInfoRegion> liveDefaultTariffs = new MutableLiveData<>();
    LiveData<PagedList<RegionEntity>> liveRegionsPlist = new MutableLiveData<>();
    final MutableLiveData<List<RegionEntity>> liveList = new MutableLiveData<>();
    MutableLiveData<Points> livePoints = new MutableLiveData<>();
    public Currency liveCurrency;
    OkHttpClient client;
    Callback callback;

    public NewApi(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        client = new OkHttpClient();


    }

    public String doInit(String url, JSONObject json) {
        String resp = null;
        try {
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            MainActivity.logstr("INIT: " + response.body().string());
                        } catch (Exception e) {
                        }
                    } else {
                        // Request not successful
                    }
                }
            });
        } catch (Exception e) {
            MainActivity.logstr("INIT_ERROR");
        }
        return resp;
    }

    public void getPoints(int regionId, GetInfoRegion info) {

        List<PointsObj> airports = new ArrayList<>();
        List<PointsObj> railways = new ArrayList<>();
        Points points = new Points();
        JSONObject json = new JSONObject();
        try {
            json.put("action", "getMainZones");
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String body1 = response.body().string();
                            JSONObject zones_json = new JSONObject(body1);
                            JSONArray airport_json = zones_json.getJSONArray("airport");
                            JSONArray railway_json = zones_json.getJSONArray("railway");
                            for (int i = 0; i < airport_json.length(); i++) {
                                JSONObject j = airport_json.getJSONObject(i);
                                if (j.getString("region").equals(String.valueOf(regionId))) {
                                    PointsObj pobj = new PointsObj();
                                    pobj.setName(j.getString("nameTerm"));
                                    pobj.setPolygone(j.getString("polygone"));
                                    pobj.setTermId(j.getInt("termID"));
                                    pobj.setParking(j.getInt("parkingPrice"));
                                    pobj.setType("airport");
                                    Logger.e(pobj.getName() + " -" + pobj.getType() + " " + pobj.getTermId() + " " + pobj.getParking());
                                    airports.add(pobj);
                                }
                            }
                            for (int i = 0; i < railway_json.length(); i++) {
                                JSONObject j = railway_json.getJSONObject(i);
                                if (j.getString("region").equals(String.valueOf(regionId))) {
                                    PointsObj pobj = new PointsObj();
                                    pobj.setName(j.getString("nameTerm"));
                                    pobj.setPolygone(j.getString("polygone"));
                                    pobj.setTermId(j.getInt("termID"));
                                    pobj.setParking(j.getInt("parkingPrice"));
                                    pobj.setType("railway");
                                    Logger.e(pobj.getName() + " -" + pobj.getType() + " " + pobj.getTermId() + " " + pobj.getParking());
                                    railways.add(pobj);
                                }
                            }
                            points.setAirports(airports);
                            points.setRailways(railways);
                            info.setPoints(points);
                            liveDefaultTariffs.postValue(info);
                            livePoints.postValue(points);
                            Logger.e("MAIN_ZONES:\r\n" + body1);
                        } catch (Exception e) {
                            Logger.e("EXCEPTION_INFO_API \r\n" + e.toString());
                        }
                    } else {
                        // Request not successful
                    }
                }
            });
        } catch (Exception e) {
            Logger.e("MainZones exception\r\n" + e.toString());
        }

        // return livePoints;
    }

    public void getCurrency(int regionId) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", "getCurrency")
                    .put("id", regionId);

            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Logger.e("REG_FAIL\r\n" + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseStr = response.body().string();
                            JSONObject jsonCurrency = new JSONObject(responseStr);
                            Currency currency = new Currency(jsonCurrency.getInt("currency"), Float.parseFloat(jsonCurrency.getString("currencyValue").replace(",", ".")));
                            liveCurrency = (currency);
                            Logger.e("CURRENCY_RESPONSE\r\n" + responseStr);
                        } catch (Exception e) {
                            Logger.e("CURRENCY_SAVE_EXCEPTION\r\n" + e.toString());
                        }
                    } else {
                        // Request not successful
                    }
                }
            });
        } catch (Exception e) {
            Logger.e("CURRENCY_ERROR: " + e.toString());
        }
    }

    public MutableLiveData<GetInfoRegion> getOptions(int regionId) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", "getOptions")
                    .put("id", regionId);

            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            GetInfoRegion info = new GetInfoRegion();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseStr = response.body().string();
                            Logger.e(responseStr);
                            JSONObject json = new JSONObject(responseStr);
                            JSONObject class_o = json.getJSONArray("class").getJSONObject(0);
                            JSONObject options_o = json.getJSONArray("options").getJSONObject(0);
                            JSONObject optionsData = new JSONObject(options_o.getString("optionsData"));
                            JSONObject available_tarrifs = new JSONObject(class_o.getString("data"));
                            Tarif tariffs = new Tarif();
                            DopUslugi uslugi = new DopUslugi();
                            Logger.e("CLASS:\r\n" + class_o.toString());
                            Logger.e("OPTIONS:\r\n" + options_o.toString());
                            Logger.e("OPTIONS_DATA:\r\n" + optionsData.toString());
                            Iterator<String> keys = available_tarrifs.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                Logger.e(key + " " + available_tarrifs.getString(key));
                                if (available_tarrifs.getString(key).equals("1")) {
                                    TarifObj tarif = new TarifObj();
                                    JSONObject tarif_json = new JSONObject(class_o.getString(key));
                                    tarif.setCrm_name(key);
                                    tarif.setName(tarif_json.getString("classNameRu"));
                                    tarif.setPassengers(tarif_json.getString("passengers"));
                                    tarif.setBaggage(tarif_json.getString("baggage"));
                                    tarif.setMin(Integer.parseInt(tarif_json.getString("minPrice_0_10")));
                                    tarif.setPricePerMinute(Float.parseFloat(tarif_json.getString("payStopTime")));
                                    Logger.e(tarif_json.toString());
                                    switch (key) {
                                        case "econom":
                                            tariffs.setEconom(tarif);
                                            break;

                                        case "comfort":
                                            tariffs.setComfort(tarif);
                                            break;

                                        case "universal":
                                            tariffs.setUniversal(tarif);
                                            break;

                                        case "business":
                                            // MainActivity.logstr("SET_BUSINESS!!! "+tarif.getCrm_name());
                                            tariffs.setBusiness(tarif);
                                            break;

                                        case "businessp":
                                            tariffs.setBusinessPlus(tarif);
                                            break;

                                        case "miniven":
                                            tariffs.setMinivan(tarif);
                                            break;

                                        case "minibus":
                                            tariffs.setMinibus(tarif);
                                            break;

                                        case "minibusvip":
                                            tariffs.setMinibusVip(tarif);
                                            break;

                                        case "minibusp":
                                            tariffs.setMinibusPlus(tarif);
                                            break;

                                        case "comfortp":
                                            tariffs.setPremium(tarif);
                                            break;
                                    }
                                }
                            }
                            DopUslugiObjs lulka = new DopUslugiObjs();
                            lulka.setName(optionsData.getString("lulkaName"));
                            lulka.setPrice(Integer.parseInt(optionsData.getString("lulkaPrice")));
                            lulka.setActive(optionsData.getBoolean("lulkaActive"));
                            uslugi.setLulka(lulka);

                            DopUslugiObjs kreslo = new DopUslugiObjs();
                            kreslo.setName(optionsData.getString("kresloName"));
                            kreslo.setPrice(Integer.parseInt(optionsData.getString("kresloPrice")));
                            kreslo.setActive(optionsData.getBoolean("kresloActive"));
                            uslugi.setKreslo(kreslo);

                            DopUslugiObjs vstrecha = new DopUslugiObjs();
                            vstrecha.setName(optionsData.getString("vstrechaName"));
                            vstrecha.setPrice(Integer.parseInt(optionsData.getString("vstrechaPrice")));
                            vstrecha.setActive(optionsData.getBoolean("vstrechaActive"));
                            uslugi.setVstrecha(vstrecha);

                            DopUslugiObjs bso = new DopUslugiObjs();
                            bso.setName(optionsData.getString("bsoName"));
                            bso.setPrice(Integer.parseInt(optionsData.getString("bsoPrice")));
                            bso.setActive(optionsData.getBoolean("bsoActive"));
                            uslugi.setBso(bso);

                            DopUslugiObjs language = new DopUslugiObjs();
                            language.setName(optionsData.getString("languageName"));
                            language.setPrice(Integer.parseInt(optionsData.getString("languagePrice")));
                            language.setActive(optionsData.getBoolean("languageActive"));
                            uslugi.setLanguage(language);

                            DopUslugiObjs yellowN = new DopUslugiObjs();
                            yellowN.setName(optionsData.getString("yellowNumbersName"));
                            yellowN.setPrice(Integer.parseInt(optionsData.getString("yellowNumbersPrice")));
                            yellowN.setActive(optionsData.getBoolean("yellowNumbersActive"));
                            uslugi.setYellowNumbers(yellowN);

                            DopUslugiObjs animal = new DopUslugiObjs();
                            animal.setName(optionsData.getString("animalName"));
                            animal.setPrice(Integer.parseInt(optionsData.getString("animalPrice")));
                            animal.setActive(optionsData.getBoolean("animalActive"));
                            uslugi.setAnimal(animal);

                            DopUslugiObjs baggage = new DopUslugiObjs();
                            baggage.setName(optionsData.getString("baggageName"));
                            baggage.setPrice(Integer.parseInt(optionsData.getString("baggagePrice")));
                            baggage.setActive(optionsData.getBoolean("baggageActive"));
                            uslugi.setBaggage(baggage);

                            DopUslugiObjs smoking = new DopUslugiObjs();
                            smoking.setName(optionsData.getString("smokingName"));
                            smoking.setPrice(Integer.parseInt(optionsData.getString("smokingPrice")));
                            smoking.setActive(optionsData.getBoolean("smokingActive"));
                            uslugi.setSmoking(smoking);


                            info.setDopUslugi(uslugi);
                            info.setTarrifs(tariffs);
                            getPoints(regionId, info);
                            getCurrency(regionId);
                            //liveDefaultTariffs.postValue(info);
                            Logger.e("REG_RESPONSE\r\n" + responseStr);
                        } catch (Exception e) {
                            Logger.e("EXCEPTION_INFO_API \r\n" + e.toString());
                        }
                    } else {
                        // Request not successful
                    }
                }
            });

        } catch (Exception e) {
            Logger.e("EXCEPTION_OPT_API \r\n" + e.toString());
        }
        return liveDefaultTariffs;
    }

    public LiveData<JSONArray> getRegions() {

        Logger.e("NEWAPI_REG_START");
        try {
            Logger.e("NEWAPI_REG_START0");
            JSONObject json = new JSONObject();
            Logger.e("NEWAPI_REG_START1");
            json.put("action", "getFullRegionList");
            Logger.e("NEWAPI_REG_START2");
            RequestBody body = RequestBody.create(JSON, json.toString());
            Logger.e("NEWAPI_REG_START3");
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Logger.e("NEWAPI_REG_START4");
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Logger.e("REG_FAIL\r\n" + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseStr = response.body().string();
                            liveRegions.postValue(new JSONArray(responseStr));
                            Logger.e("REG_RESPONSE\r\n" + responseStr);
                        } catch (Exception e) {
                            Logger.e("REG_EXCEPTION\r\n" + e.toString());
                        }
                    } else {
                        // Request not successful
                    }
                }
            });
            Logger.e("NEWAPI_REG_START5");
        } catch (Exception e) {
            Logger.e("NEWAPI_REG_EXCEPTION\r\n" + e.toString());
        }
        return liveRegions;
    }

    public LiveData<PagedList<RegionEntity>> getRegionsAsList(String name) {
        List<RegionEntity> list = new ArrayList<RegionEntity>();
        Logger.e("NEWAPI_REG_START");
        try {
            Logger.e("NEWAPI_REG_START0");
            JSONObject json = new JSONObject();
            Logger.e("NEWAPI_REG_START1");
            json.put("action", "getFullRegionList");
            Logger.e("NEWAPI_REG_START2");
            RequestBody body = RequestBody.create(JSON, json.toString());
            Logger.e("NEWAPI_REG_START3");
            Request request = new Request.Builder()
                    .url(NekConfigs.apiURL)
                    .post(body)
                    .build();
            Logger.e("NEWAPI_REG_START4");
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Logger.e("REG_FAIL\r\n" + e.toString());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseStr = response.body().string();

                            try {

                                JSONArray array = new JSONArray(responseStr);
                                //MainActivity.logstr("VALUE: "+array.toString());
                                if (array != null) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject region = array.getJSONObject(i);
                                        RegionEntity reg_o = new RegionEntity();
                                        reg_o.setName(region.getString("nameTerm"));
//                                        MainActivity.logstr(region.getString("nameTerm")+region.toString());
                                        reg_o.setId(region.getInt("termID"));
                                        list.add(reg_o);
                                    }
                                }
                                MainActivity.logstr("LSIZE: " + list.size());
                                liveList.postValue(list);
                                liveRegionsPlist = new LivePagedListBuilder<>(myDao.regionList(name), list.size()).build();
                                saveRegions(() -> myDao.regionsSaveAll(list));
                            } catch (Exception e) {
                                MainActivity.logstr("API_REG_EXCEPT\r\n" + e.toString() + " " + e.fillInStackTrace());
                            }
                            Logger.e("REG_RESPONSE\r\n" + responseStr);
                        } catch (Exception e) {
                            Logger.e("REG_EXCEPTION\r\n" + e.toString());
                        }
                    } else {
                        // Request not successful
                    }
                }
            });
//            Logger.e("NEWAPI_REG_START5");
        } catch (Exception e) {
            Logger.e("NEWAPI_REG_EXCEPTION\r\n" + e.toString());
        }
        return liveRegionsPlist;
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

    public static class StringListProvider {

        private List<RegionEntity> list;

        public StringListProvider(List<RegionEntity> list) {
            this.list = list;
        }

        public List<RegionEntity> getStringList(int page, int pageSize) {
            int initialIndex = page * pageSize;
            int finalIndex = initialIndex + pageSize;

            //TODO manage out of range index

            return list.subList(initialIndex, finalIndex);
        }
    }

    public static class StringDataSource extends PageKeyedDataSource<Integer, RegionEntity> {

        public static final int PAGE_SIZE = 20;
        private StringListProvider provider;

        public StringDataSource(StringListProvider provider) {
            this.provider = provider;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, RegionEntity> callback) {
            List<RegionEntity> result = provider.getStringList(0, params.requestedLoadSize);
            callback.onResult(result, 1, 2);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RegionEntity> callback) {
            List<RegionEntity> result = provider.getStringList(params.key, params.requestedLoadSize);
            Integer nextIndex = null;

            if (params.key > 1) {
                nextIndex = params.key - 1;
            }
            callback.onResult(result, nextIndex);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RegionEntity> callback) {
            List<RegionEntity> result = provider.getStringList(params.key, params.requestedLoadSize);
            callback.onResult(result, params.key + 1);
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

}

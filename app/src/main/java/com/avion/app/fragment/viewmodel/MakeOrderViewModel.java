package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avion.app.MainActivity;
import com.avion.app.action.Address;
import com.avion.app.action.CalcPrice;
import com.avion.app.action.MakeOrder;
import com.avion.app.action.Option;
import com.avion.app.action.TarifPriceObj;
import com.avion.app.models.GetInfoRegion;
import com.avion.app.models.Payment;
import com.avion.app.models.TarifObj;
import com.avion.app.repository.NewApi;
import com.avion.app.repository.OrderRepository;
import com.avion.app.repository.RegionRepository;
import com.avion.app.repository.TarifRespository;
import com.avion.app.unit.NekFragment;
import com.avion.app.unit.NekViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;

import java.util.Objects;

public class MakeOrderViewModel extends NekViewModel {

    private final MutableLiveData<MakeOrder> makeOrderLiveData = new MutableLiveData<>();
    private MakeOrder value;

    private NekFragment nekFragment;

    private MutableLiveData<GetInfoRegion> getInfoRegionMutableLiveData = new MutableLiveData<>();
    private LiveData<TarifPriceObj> getTarifPrices = new MutableLiveData<>();
    private int regionInfoID = 0;

    public void setNekFragment(NekFragment nekFragment) {
        this.nekFragment = nekFragment;
    }

    private RegionRepository regionRepository;
    private TarifRespository tarifRespository;
    private OrderRepository orderRepository;
    public NewApi newApi;

    public MakeOrderViewModel(@NonNull Application application) {
        super(application);
        refrefData();
        regionRepository = new RegionRepository(application, this);
        tarifRespository = new TarifRespository(application, this);
        orderRepository = new OrderRepository(application, this);
        newApi = new NewApi(application, this);
    }

    public void refrefData() {
        makeOrderLiveData.setValue(new MakeOrder(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber())));
        value = makeOrderLiveData.getValue();
        //updateValue();
    }

    public void setCurrency() {

        MainActivity.currency = newApi.liveCurrency;
        Logger.e("FRESH_CURRENCY: " + MainActivity.currency.getId());
        //MainActivity.currency.
    }

    public void setPayment(Payment payment) {
        value.setPayment(payment);
        updateValue();
    }

    public LiveData<GetInfoRegion> getOptionsFromModel(int id) {
        return newApi.getOptions(id);
    }

    public LiveData<JSONArray> getRegionsFromModel() {
        return newApi.getRegions();
    }

    public LiveData<GetInfoRegion> getGetInfoRegion() {
        if (regionInfoID != MainActivity.chooseRegionLiveData.getValue().getId() || getInfoRegionMutableLiveData.getValue() == null) {
            regionInfoID = MainActivity.chooseRegionLiveData.getValue().getId();
            getInfoRegionMutableLiveData = newApi.getOptions(regionInfoID);
            //getInfoRegionMutableLiveData = newApi.getOptions(regionInfoID);
        }
        return getInfoRegionMutableLiveData;
    }

    public LiveData<TarifPriceObj> getTarifPrices() {
        //if (getTarifPrices.getValue() == null || regionInfoID != MainActivity.chooseRegionLiveData.getValue().getId()) {
        CalcPrice calcPrice = new CalcPrice();
        calcPrice.setFrom(value.getFrom());
        calcPrice.setTo(value.getTo());
        calcPrice.setRegion(regionInfoID);
        getTarifPrices = tarifRespository.getPrices(calcPrice);
        // }
        return getTarifPrices;
    }

    @Override
    public void onReDoTask() {

    }

    @Override
    public NekFragment getNekFragment() {
        return nekFragment;
    }

    public LiveData<MakeOrder> getMakeOrderLiveData() {
        return makeOrderLiveData;
    }

    public Integer getBonusP() {
        return value.getBonusPayment() ? 1 : 0;
    }

    public void setBonusP(Integer state) {
        if (value == null)
            return;
        value.setBonusPayment(state == 1);
        updateValue();
    }

    public void setUserPhone(String phone) {
        if (value == null)
            return;
        value.setUser_phone(phone);
        updateValue();
    }

    public void setRegion(String reg) {
        if (value == null)
            return;
        value.setRegion(reg);
        updateValue();
    }

    public void setMakeOrder(MakeOrder makeOrder) {
        value = makeOrder;
        updateValue();
    }

    public void setDate(String date) {
        if (value == null)
            return;
        value.setDate(date);
        updateValue();
    }

    public void setTime(String time) {
        if (value == null)
            return;
        value.setTime(time);
        updateValue();
    }

    public void setCarToTime(Boolean bool) {
        if (value == null)
            return;
        value.setCartotime(bool);
        updateValue();
    }

    public void setTrainNumber(String trainNumber) {
        if (value == null)
            return;
        value.setTrain_number(trainNumber);
        updateValue();
    }

    public void setCarriageNumber(String carriage_number) {
        if (value == null)
            return;
        value.setCarriage_number(carriage_number);
        updateValue();
    }

    public void setFlightNumber(String flight_number) {
        if (value == null)
            return;
        value.setFlight_number(flight_number);
        updateValue();
    }

    public void setDepartingFrom(String departing_from) {
        if (value == null)
            return;
        value.setDeparting_from(departing_from);
        updateValue();
    }

    public void setOptions(Option options) {
        value.setOptions(options);
        updateValue();
    }

    public void setComments(String txt) {
        value.setComments_to_the_driver(txt);
        updateValue();
    }

    public void setMeetWTableText(String txt) {
        value.getOptions().setMeeting_with_a_table(txt);
        updateValue();
    }

    public void setAddressFrom(Address addressFrom) {
        value.setFrom(addressFrom);
        updateValue();
    }

    public void setAddressTo(Address addressTo) {
        value.setTo(addressTo);
        updateValue();
    }

    public void setParkingPrice(double price) {
        value.setParking_price(price);
        updateValue();
    }

    public void setTarif(TarifObj tarifObj) {
        value.setTarif(tarifObj);
        updateValue();
    }

    public void setPrice(Double price) {
        value.setTotal_price(price);
        updateValue();
    }

    public void setPayType(Payment payment) {
        value.setPayment(payment);
        updateValue();
    }

    public void setUserName(String name) {
        value.setNameOrder(name);
        updateValue();
    }

    public LiveData<String> makeOrder() {
        return orderRepository.makeOrder(value);
    }

    private synchronized void updateValue() {
        makeOrderLiveData.setValue(value);
        value = makeOrderLiveData.getValue();
    }

}

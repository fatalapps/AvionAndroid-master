package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.avion.app.action.MakeOrder;
import com.avion.app.entity.DriverEntity;
import com.avion.app.repository.DriverRepository;
import com.avion.app.repository.OrderRepository;
import com.avion.app.unit.NekViewModel;

import java.util.Objects;

public class OrderInfoViewModel extends NekViewModel {

    private OrderRepository orderRepository;
    private DriverRepository driverRepository;

    LiveData<MakeOrder> order;
    LiveData<DriverEntity> driver;


    public OrderInfoViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application, this);
        driverRepository = new DriverRepository(application, this);
    }

    public LiveData<MakeOrder> getOrder(String id) {
        if (order == null)
            order = orderRepository.getOrder(id);
        return order;
    }

    public void cancelOrder(String id) {
        orderRepository.cancelOrder(id);
    }

    public LiveData<DriverEntity> getDriver(String id) {
        driver = null;
        driver = driverRepository.getDriver(id);
        return driver;
    }

    public String getOrderdate() {
        if (order == null || order.getValue() == null)
            return "";
        return order.getValue().getDate().replace("/", ".") + " " + order.getValue().getTime();
    }

    public LiveData<DriverEntity> getDriver() {
        if (driver == null)
            driver = driverRepository.getDriver(Objects.requireNonNull(order.getValue()).getDriver_id());
        return driver;
    }

    public LiveData<Boolean> rate(String comment, Double rate) {
        return driverRepository.rateDriver(order.getValue().getId(), comment, rate);
    }

    public Float getDriverRate() {
        return orderRepository.getDriverRate(order.getValue().getId());
    }

    public Float getAutoRate() {
        return orderRepository.getCareRate(order.getValue().getId());
    }

    public String getOrderComment() {
        return orderRepository.getComment(order.getValue().getId());
    }

    public void setDriverRate(Float rate) {
        orderRepository.setDriverRate(order.getValue().getId(), rate);
    }

    public void setAutoRate(Float rate) {
        orderRepository.setCarRate(order.getValue().getId(), rate);
    }

    public void setOrderComment(String com) {
        orderRepository.setComment(order.getValue().getId(), com);
    }

    @Override
    public void onReDoTask() {

    }
}

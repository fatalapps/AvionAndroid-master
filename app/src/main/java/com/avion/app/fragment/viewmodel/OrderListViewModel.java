package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.avion.app.action.MakeOrder;
import com.avion.app.repository.OrderRepository;
import com.avion.app.unit.NekViewModel;

import java.util.List;

public class OrderListViewModel extends NekViewModel {

    private OrderRepository orderRepository;

    public OrderListViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application, this);
    }

    public LiveData<List<MakeOrder>> getList() throws Exception {
        return orderRepository.getList();
    }

    @Override
    public void onReDoTask() {

    }
}

package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.avion.app.entity.BankCardEntity;
import com.avion.app.repository.BankCardRepository;
import com.avion.app.unit.NekFragment;
import com.avion.app.unit.NekViewModel;

public class BankCardViewModel extends NekViewModel {

    public BankCardRepository bankCardRepository;
    private LiveData<PagedList<BankCardEntity>> cardList;

    public BankCardViewModel(@NonNull Application application) {
        super(application);
        bankCardRepository = new BankCardRepository(application, this);
    }

    public void init() {

        cardList = bankCardRepository.getList();
    }

    public void reload() {
        bankCardRepository.refresh();
    }

    public LiveData<PagedList<BankCardEntity>> getCardList() {
        return cardList;
    }

    public void delete(BankCardEntity ent) {
        bankCardRepository.delete(ent, this);
    }

    public void addNew(BankCardEntity bankCardEntity) {
        bankCardRepository.addNew(bankCardEntity);
    }

    @Override
    public void onReDoTask() {

    }

    @Override
    public NekFragment getNekFragment() {
        return null;
    }
}

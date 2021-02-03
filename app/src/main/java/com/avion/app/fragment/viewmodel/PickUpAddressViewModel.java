package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.avion.app.repository.QuaryAddressRepository;
import com.avion.app.unit.NekFragment;
import com.avion.app.unit.NekViewModel;

public class PickUpAddressViewModel extends NekViewModel {

    private NekFragment nekFragment;
    private QuaryAddressRepository quaryAddressRepository;

    public PickUpAddressViewModel(@NonNull Application application) {
        super(application);
        quaryAddressRepository = new QuaryAddressRepository(application, nekFragment, aBoolean -> {
            if (aBoolean)
                openNetworkErrorMessage();
        });
    }

    public void setNekFragment(NekFragment nekFragment) {
        this.nekFragment = nekFragment;
    }

    @Override
    public void onReDoTask() {

    }

    public void quaryAddress(String quary, QuaryAddressRepository.OnCallList onCallList) {
        quaryAddressRepository.getAdressList(quary, onCallList);
    }

    @Override
    public NekFragment getNekFragment() {
        return nekFragment;
    }
}

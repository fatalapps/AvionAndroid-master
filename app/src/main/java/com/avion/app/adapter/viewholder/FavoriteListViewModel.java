package com.avion.app.adapter.viewholder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.avion.app.entity.FaworiteAddressEntity;
import com.avion.app.repository.FavoriteAddressRepository;
import com.avion.app.repository.QuaryAddressRepository;
import com.avion.app.unit.NekViewModel;

public class FavoriteListViewModel extends NekViewModel {

    private FavoriteAddressRepository favoriteAddressRepository;
    private QuaryAddressRepository quaryAddressRepository;

    public FavoriteListViewModel(@NonNull Application application) {
        super(application);
        favoriteAddressRepository = new FavoriteAddressRepository(application, this);
        quaryAddressRepository = new QuaryAddressRepository(application, nekFragment, aBoolean -> {
            if (aBoolean)
                openNetworkErrorMessage();
        });
    }

    public LiveData<PagedList<FaworiteAddressEntity>> getList() {
        return favoriteAddressRepository.getList();
    }

    public void add(FaworiteAddressEntity faworiteAddressEntity) {
        favoriteAddressRepository.addNew(faworiteAddressEntity);
    }

    public void delete(FaworiteAddressEntity faworiteAddressEntity) {
        favoriteAddressRepository.delete(faworiteAddressEntity);
    }

    public void quaryAddress(String quary, QuaryAddressRepository.OnCallList onCallList) {
        quaryAddressRepository.getAdressList(quary, onCallList);
    }

    @Override
    public void onReDoTask() {

    }
}

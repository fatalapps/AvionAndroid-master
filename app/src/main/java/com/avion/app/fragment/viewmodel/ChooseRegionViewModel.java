package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.avion.app.entity.RegionEntity;
import com.avion.app.repository.NewApi;
import com.avion.app.repository.RegionRepository;
import com.avion.app.unit.NekFragment;
import com.avion.app.unit.NekViewModel;

public class ChooseRegionViewModel extends NekViewModel {

    private NekFragment nekFragment;
    private RegionRepository regionRepository;
    private NewApi newApi;

    public void setNekFragment(NekFragment nekFragment) {
        this.nekFragment = nekFragment;
    }

    public ChooseRegionViewModel(@NonNull Application application) {
        super(application);
        regionRepository = new RegionRepository(application, this);
        newApi = new NewApi(application, this);
    }

    @Override
    public void onReDoTask() {

    }

    public LiveData<PagedList<RegionEntity>> list(String name) {
        return regionRepository.getRegionsList(name);
    }

    public LiveData<PagedList<RegionEntity>> getRegionsFromModel(String name) {
        name = "%" + name + "%";
        return newApi.getRegionsAsList(name);
    }

    @Override
    public NekFragment getNekFragment() {
        return nekFragment;
    }
}

package com.avion.app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.avion.app.entity.FaworiteAddressEntity;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteAddressRepository extends NekRepository {
    public FavoriteAddressRepository(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
    }

    public LiveData<PagedList<FaworiteAddressEntity>> getList() {
        return new LivePagedListBuilder<>(myDao.favorite_addressList(), 10).build();
    }

    public void addNew(FaworiteAddressEntity faworiteAddressEntity) {
        Completable.fromAction(() -> myDao.favorite_addressSave(faworiteAddressEntity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    public void delete(FaworiteAddressEntity faworiteAddressEntity) {
        Completable.fromAction(() -> myDao.favorite_addressDelete(faworiteAddressEntity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}

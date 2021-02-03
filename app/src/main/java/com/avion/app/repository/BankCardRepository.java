package com.avion.app.repository;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.avion.app.MainActivity;
import com.avion.app.entity.BankCardEntity;
import com.avion.app.fragment.viewmodel.BankCardViewModel;
import com.avion.app.unit.NekConfigs;
import com.avion.app.unit.NekRepository;
import com.avion.app.unit.NekViewModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import ru.tinkoff.acquiring.sdk.AcquiringSdk;
import ru.tinkoff.acquiring.sdk.Card;
import ru.tinkoff.acquiring.sdk.CardManager;

public class BankCardRepository extends NekRepository {

    private CardManager cardManager;

    public BankCardRepository(Application application, NekViewModel nekViewModel) {
        super(application, nekViewModel);
        AcquiringSdk acquiringSdk = new AcquiringSdk(NekConfigs.tinkoftTerminalKey, NekConfigs.tinkoftPassword, NekConfigs.tinkoftPublicKey);
        cardManager = new CardManager(acquiringSdk);
    }

    public LiveData<PagedList<BankCardEntity>> getList() {
        refresh();
        DataSource.Factory l = myDao.bacnkcard_list();

        return new LivePagedListBuilder<>(l, 5).build();
    }

    Card[] cards;

    @SuppressLint({"StaticFieldLeak", "CheckResult"})
    public void refresh1(BankCardViewModel model) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                cardManager.clear(NekConfigs.getUserID());
                cards = cardManager.getActiveCards(NekConfigs.getUserID());
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Logger.d("Done work " + cards.length);
                        List<BankCardEntity> bankCardEntityList = new ArrayList<>();
                        BankCardEntity cash = new BankCardEntity();
                        cash.setId("cash");
                        cash.setCard_num("cash");
                        bankCardEntityList.add(cash);
                        for (Card activeCard : cards) {
                            BankCardEntity bankCardEntity = new BankCardEntity();
                            bankCardEntity.setCard_num(activeCard.getPan());
                            bankCardEntity.setId(activeCard.getCardId());
                            bankCardEntityList.add(bankCardEntity);
                        }

                        myDao.bankcard_saveSAll(bankCardEntityList);
                        model.init();
//                        MainActivity.logstr("(REPOSITORY) PAGEDLIST SIZE list-model: "+bankCardEntityList.size()+"-"+model.getCardList().getValue().size());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }
                });
    }

    @SuppressLint({"StaticFieldLeak", "CheckResult"})
    public void refresh() {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                cardManager.clear(NekConfigs.getUserID());
                cards = cardManager.getActiveCards(NekConfigs.getUserID());
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Logger.d("Done work " + cards.length);
                        List<BankCardEntity> bankCardEntityList = new ArrayList<>();
                        BankCardEntity cash = new BankCardEntity();
                        cash.setId("cash"); // cash
                        cash.setCard_num("cash");
                        BankCardEntity gpay = new BankCardEntity();
                        gpay.setId("gpay"); //google pay
                        gpay.setCard_num("gpay");
                        BankCardEntity bonusp = new BankCardEntity();
                        bonusp.setId("bonusp"); //bonus
                        bonusp.setCard_num("bonusp");
                        bankCardEntityList.add(cash);
                        bankCardEntityList.add(bonusp);
                        bankCardEntityList.add(gpay);
                        for (Card activeCard : cards) {
                            BankCardEntity bankCardEntity = new BankCardEntity();
                            bankCardEntity.setCard_num(activeCard.getPan());
                            bankCardEntity.setId(activeCard.getCardId());
                            bankCardEntityList.add(bankCardEntity);
                        }
                        MainActivity.logstr("DB_NAME: " + myDao.getClass().getName());
                        myDao.bankcard_saveSAll(bankCardEntityList);

                    }

                    @Override
                    public void onError(Throwable e) {
                        MainActivity.logstr(e.toString());
                        Logger.d("Done work new customer ");
                        List<BankCardEntity> bankCardEntityList = new ArrayList<>();
                        BankCardEntity cash = new BankCardEntity();
                        cash.setId("cash"); // cash
                        cash.setCard_num("cash");
                        BankCardEntity gpay = new BankCardEntity();
                        gpay.setId("gpay"); //google pay
                        gpay.setCard_num("gpay");
                        BankCardEntity bonusp = new BankCardEntity();
                        bonusp.setId("bonusp"); //bonus
                        bonusp.setCard_num("bonusp");
                        bankCardEntityList.add(cash);
                        bankCardEntityList.add(gpay);
                        bankCardEntityList.add(bonusp);
                        MainActivity.logstr("DB_NAME: " + myDao.getClass().getName());
                        myDao.bankcard_saveSAll(bankCardEntityList);
                    }
                });
    }

    public void addNew(BankCardEntity bankCardEntity) {
        refresh();
    }

    public void delete(BankCardEntity faworiteAddressEntity, BankCardViewModel model) {
        Completable.fromAction(() -> myDao.deleteCard(faworiteAddressEntity))
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        MainActivity.logstr("CARD_WAS_REMOVED_FROM_DAO");
                        refresh1(model);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }
}

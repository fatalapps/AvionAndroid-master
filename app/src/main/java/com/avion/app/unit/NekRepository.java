package com.avion.app.unit;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.avion.app.database.MyDB;
import com.avion.app.database.MyDao;
import com.avion.app.fragment.ChoosePaymentFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public abstract class NekRepository {

    protected NekViewModel nekViewModel;
    public MyDao myDao;
    protected RequestQueue queue;
    protected FirebaseFirestore db;

    public NekRepository(Application application, NekViewModel nekViewModel) {
        this.nekViewModel = nekViewModel;
        myDao = MyDB.getDatabase(application.getApplicationContext()).myDao();
        queue = Volley.newRequestQueue(application.getApplicationContext());
        new ChoosePaymentFragment.doAsync(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                myDao.clearCards();
                return null;
            }
        }).execute();
        db = FirebaseFirestore.getInstance();
    }

    protected void startProgress() {
        if (nekViewModel == null)
            return;
        try {
            nekViewModel.startProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void stopPrograss() {
        if (nekViewModel == null)
            return;
        try {
            nekViewModel.stopProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showNetworkerror() {
        if (nekViewModel == null)
            return;
        try {
            stopPrograss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nekViewModel.openNetworkErrorMessage();
    }

}

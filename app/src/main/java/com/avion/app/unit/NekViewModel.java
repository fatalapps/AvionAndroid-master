package com.avion.app.unit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class NekViewModel extends AndroidViewModel {

    public NekViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public abstract void onReDoTask();


    protected NekFragment nekFragment;

    public void setNekFragment(NekFragment nekFragment) {
        this.nekFragment = nekFragment;
    }

    public NekFragment getNekFragment() {
        return nekFragment;
    }

    protected Application application;

    protected void openNetworkErrorMessage() {
        if (getNekFragment() == null)
            return;
        getNekFragment().openErrorMessage();
    }

    protected void startProgress() {
        if (getNekFragment() == null)
            return;
        getNekFragment().onStartProgress();
    }

    protected void stopProgress() {
        if (getNekFragment() == null)
            return;
        getNekFragment().onStopProgress();
    }

}

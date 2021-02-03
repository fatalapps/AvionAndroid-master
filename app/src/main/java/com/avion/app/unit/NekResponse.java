package com.avion.app.unit;

import androidx.lifecycle.MutableLiveData;

public abstract class NekResponse {
    protected MutableLiveData<Boolean> isNetworkError = new MutableLiveData<>();

    public NekResponse() {
        isNetworkError.setValue(false);
    }
}

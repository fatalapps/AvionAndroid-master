package com.avion.app.fragment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.avion.app.models.User;
import com.avion.app.repository.UserRepository;
import com.avion.app.unit.NekViewModel;

public class SigInViewModel extends NekViewModel {

    private UserRepository userRepository;

    public SigInViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public LiveData<User> getUser(String id) {
        return userRepository.getUser(id);
    }

    @Override
    public void onReDoTask() {

    }
}

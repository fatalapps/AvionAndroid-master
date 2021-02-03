package com.avion.app.unit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class NekFragment extends Fragment implements NekViewModelInterfaces {

    private NetworkErrorBottomSheetFragment networkErrorBottomSheetFragment;
    private NekViewModel nekViewModel;

    protected abstract NekViewModel onCreateViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkErrorBottomSheetFragment = new NetworkErrorBottomSheetFragment();
        if (networkErrorBottomSheetFragment.isAdded()) return;
        networkErrorBottomSheetFragment.setNekFragment(this);
        nekViewModel = onCreateViewModel();
    }

    @Override
    public void onNetWorkError() {
        openErrorMessage();
    }

    public abstract void onReDoNetworkAction();

    protected void openErrorMessage() {
        assert getFragmentManager() != null;
        networkErrorBottomSheetFragment.show(getFragmentManager(), networkErrorBottomSheetFragment.getTag());
    }

    public NekViewModel getNekViewModel() {
        return nekViewModel;
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = getActivity().getCurrentFocus();

        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

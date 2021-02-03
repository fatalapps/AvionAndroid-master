package com.avion.app.unit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avion.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NetworkErrorBottomSheetFragment extends BottomSheetDialogFragment {

    private NekFragment nekFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.networkerror_botomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root_view, @Nullable Bundle savedInstanceState) {
        final Button network_repeat_btn = root_view.findViewById(R.id.network_repeat_btn);
        network_repeat_btn.setOnClickListener(v -> {
            if (nekFragment == null)
                return;
            nekFragment.onReDoNetworkAction();
            nekFragment.getNekViewModel().onReDoTask();
            this.dismiss();
        });
    }

    public void setNekFragment(NekFragment nekFragment) {
        this.nekFragment = nekFragment;
    }
}

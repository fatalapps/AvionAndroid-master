package com.avion.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.avion.app.R;
import com.avion.app.adapter.TarifAdapter;
import com.avion.app.adapter.TariffRecyclerAdapter;
import com.avion.app.models.TarifObj;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class TarifRecyclerInfo extends BottomSheetDialogFragment {
    public ArrayList<TarifObj> TariList;
    public int position;
    public LinearLayoutManager lm;
    public RecyclerView.SmoothScroller smoothScroller;
    public RecyclerView recyclerView;
    public TarifAdapter tAd;

    public TarifRecyclerInfo(ArrayList<TarifObj> obj, int pos, LinearLayoutManager lm, RecyclerView.SmoothScroller smoothScroller, RecyclerView recyclerView, TarifAdapter ta) {
        this.TariList = obj;
        this.position = pos;
        this.lm = lm;
        this.smoothScroller = smoothScroller;
        this.recyclerView = recyclerView;
        this.tAd = ta;
    }

    public static TarifRecyclerInfo newInstance(ArrayList<TarifObj> tarif, int pos, LinearLayoutManager lm, RecyclerView.SmoothScroller smoothsc, RecyclerView recyclerView, TarifAdapter ta) {
        //MainActivity.logstr(tarif.get(0).getCarname());
        return new TarifRecyclerInfo(tarif, pos, lm, smoothsc, recyclerView, ta);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_recyclerview, container,
                false);

        // get the views and attach the listener

        return view;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.botom_tarif_recycle);
        TariffRecyclerAdapter adapter = new TariffRecyclerAdapter();
        adapter.setBottom(this);
        adapter.setLm(lm);
        adapter.setAd(tAd);
        adapter.setSmoothSc(smoothScroller);
        adapter.setRecycle(recyclerView);
        adapter.setTarifObjList(TariList);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.scrollToPosition(position);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        //ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        //mItemTouchHelper.attachToRecyclerView(recyclerView);
        //itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

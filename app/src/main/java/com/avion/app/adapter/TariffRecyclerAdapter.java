package com.avion.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.adapter.viewholder.TarifRecyclerHolder;
import com.avion.app.fragment.TarifRecyclerInfo;
import com.avion.app.models.TarifObj;

import java.util.ArrayList;
import java.util.List;

public class TariffRecyclerAdapter extends RecyclerView.Adapter<TarifRecyclerHolder> {

    private List<TarifObj> tarifObjList = new ArrayList<>();
    private int selectedTarifIcon = R.drawable.car_comfort;
    LinearLayoutManager lm;
    View rootv;
    TarifRecyclerInfo bottom;
    RecyclerView recycleView;
    TarifAdapter tAd;
    RecyclerView.SmoothScroller smoothScroller;

    public interface OnTafif {
        void onSelect(TarifObj tarifObj);

        void onInfo(TarifObj tarifObj);
    }

    private TarifAdapter.OnTafif onTafif;

    public void setOnTafif(TarifAdapter.OnTafif onTafif) {
        this.onTafif = onTafif;
    }

    public void setSelectedTarifIcon(int selectedTarifIcon) {
        this.selectedTarifIcon = selectedTarifIcon;
        notifyDataSetChanged();
    }

    public void setTarifObjList(List<TarifObj> tarifObjList) {
        this.tarifObjList = tarifObjList;
        notifyDataSetChanged();
    }

    public void setBottom(TarifRecyclerInfo tri) {
        this.bottom = tri;
        notifyDataSetChanged();
    }

    public void setLm(LinearLayoutManager lm) {
        this.lm = lm;
        notifyDataSetChanged();
    }

    public void setSmoothSc(RecyclerView.SmoothScroller smc) {
        this.smoothScroller = smc;
        notifyDataSetChanged();
    }

    public void setRecycle(RecyclerView smc) {
        this.recycleView = smc;
        notifyDataSetChanged();
    }

    public void setAd(TarifAdapter ta) {
        this.tAd = ta;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarifRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bottom_fragment_tarif_info, viewGroup, false);
        rootv = root_view;
        return new TarifRecyclerHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarifRecyclerHolder tarifViewHolder, int i) {
        TarifObj item = getItem(i);

        MainActivity.logstr(item.getName() + " NEW " + item.getMin());
        //((TextView) rootv.findViewById(R.id.trafic_name_txtview)).setText(item.getName());
        tarifViewHolder.settAd(tAd);
        tarifViewHolder.setData(item);
        tarifViewHolder.setDismiss(bottom);
        tarifViewHolder.scrollToPos(i, lm, smoothScroller, recycleView);

    }

    @Override
    public int getItemCount() {
        return tarifObjList.size();
    }

    private TarifObj getItem(int possition) {
        return tarifObjList.get(possition);
    }
}

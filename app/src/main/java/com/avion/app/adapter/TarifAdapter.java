package com.avion.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.adapter.viewholder.TarifViewHolder;
import com.avion.app.models.TarifObj;

import java.util.ArrayList;
import java.util.List;

public class TarifAdapter extends RecyclerView.Adapter<TarifViewHolder> {

    private List<TarifObj> tarifObjList = new ArrayList<>();
    private int selectedTarifIcon = R.drawable.car_comfort;

    public interface OnTafif {
        void onSelect(TarifObj tarifObj);

        void onInfo(TarifObj tarifObj);
    }

    private OnTafif onTafif;

    public void setOnTafif(OnTafif onTafif) {
        this.onTafif = onTafif;
    }

    public void setSelectedTarifIcon(int selectedTarifIcon) {
        this.selectedTarifIcon = selectedTarifIcon;
        notifyDataSetChanged();
    }

    public void setTarifObjList(List<TarifObj> tarifObjList) {
        this.tarifObjList = tarifObjList;
        MainActivity.logstr("TOTAL_TARRIFS: " + tarifObjList.size());
        if (tarifObjList.size() == 1) setSelectedTarifIcon(R.drawable.car_econom);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardtype, viewGroup, false);

        return new TarifViewHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarifViewHolder tarifViewHolder, int i) {

        TarifObj item = getItem(i);
        tarifViewHolder.setData(item);

        if (item.getIcon() == selectedTarifIcon) {
            tarifViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                MainActivity.logstr("HERE ADAP2");
                                                                onTafif.onInfo(item);
                                                            }
                                                        }
            );
            tarifViewHolder.Select();
            MainActivity.logstr("HERE ADAP");
            onTafif.onSelect(item);
        } else {
            tarifViewHolder.Unselect();
            tarifViewHolder.itemView.setOnClickListener(v -> {
                setSelectedTarifIcon(item.getIcon());

            });
        }

        tarifViewHolder.car_info_imageview.setOnClickListener(v -> {

            onTafif.onInfo(item);
        });

    }

    @Override
    public int getItemCount() {
        return tarifObjList.size();
    }

    private TarifObj getItem(int possition) {
        return tarifObjList.get(possition);
    }
}

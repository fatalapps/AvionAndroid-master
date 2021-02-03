package com.avion.app.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.adapter.viewholder.TxtViewViewHolder;
import com.avion.app.entity.RegionEntity;

public class RegionsAdapter extends PagedListAdapter<RegionEntity, TxtViewViewHolder> {
    public Activity act;

    public RegionsAdapter(Activity a) {
        super(DIFF);
        act = a;
        if (MainActivity.chooseRegionLiveData.getValue() != null)
            currentSelectedID = MainActivity.chooseRegionLiveData.getValue().getId();
    }

    public interface GetRegion {
        void get(RegionEntity regionEntity);
    }

    private GetRegion getRegion;

    public void setGetRegion(GetRegion getRegion) {
        this.getRegion = getRegion;
    }

    private int currentSelectedID = 0;
    private Resources resources;

    @NonNull
    @Override
    public TxtViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        resources = viewGroup.getResources();
        TextView view = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_region, viewGroup, false);
        return new TxtViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TxtViewViewHolder txtViewViewHolder, int i) {
        txtViewViewHolder.setIsRecyclable(false);
        txtViewViewHolder.setData(getItem(i).getName());
        if (getItem(i).getId() == currentSelectedID) {
            txtViewViewHolder.getNameTxtView().setTextColor(resources.getColor(R.color.colorPrimary));
            txtViewViewHolder.getNameTxtView().setCompoundDrawables(null, null, resources.getDrawable(R.drawable.ic_checked), null);
        } else {
            txtViewViewHolder.getNameTxtView().setTextColor(resources.getColor(R.color.colorAccent));
            txtViewViewHolder.getNameTxtView().setCompoundDrawables(null, null, null, null);
        }
        MainActivity.logstr("REG_ADAPTER:\r\n" + i + " : " + getItem(i).getName());

        txtViewViewHolder.itemView.setOnClickListener(v -> {

            getRegion.get(getItem(i));
            currentSelectedID = getItem(i).getId();
            notifyDataSetChanged();

            MainActivity.chooseRegionLiveData.setValue(getItem(i));
            act.onBackPressed();
        });
    }

    private static DiffUtil.ItemCallback<RegionEntity> DIFF = new DiffUtil.ItemCallback<RegionEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull RegionEntity regionEntity, @NonNull RegionEntity t1) {
            return regionEntity.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RegionEntity regionEntity, @NonNull RegionEntity t1) {
            return regionEntity.getName().equals(t1.getName());
        }
    };

}

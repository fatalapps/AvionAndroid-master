package com.avion.app.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.R;
import com.avion.app.adapter.viewholder.TxtViewViewHolder;
import com.avion.app.fragment.PickUpAddressFragment;
import com.avion.app.models.PointsObj;

import java.util.ArrayList;
import java.util.List;

public class AddressPointAdapter extends RecyclerView.Adapter<TxtViewViewHolder> {

    private List<PointsObj> pointsObjList = new ArrayList<>();
    public Activity act;
    public PickUpAddressFragment.Companion PickUpAddressFragment;

    public AddressPointAdapter(Activity ac, PickUpAddressFragment.Companion PickUpAddressFragment) {
        act = ac;
        this.PickUpAddressFragment = PickUpAddressFragment;
    }

    public void setPointsObjList(List<PointsObj> pointsObjList) {
        this.pointsObjList = pointsObjList;
        notifyDataSetChanged();
    }

    public interface OnPointChoosen {
        void choosen(PointsObj point);
    }

    private String choosenPointName = "";
    private OnPointChoosen onPointChoosen;

    public void setOnPointChoosen(OnPointChoosen onPointChoosen) {
        this.onPointChoosen = onPointChoosen;
    }

    public void setChoosenPointName(String choosenPointName) {
        this.choosenPointName = choosenPointName;
        notifyDataSetChanged();
    }

    private Resources resources;


    @NonNull
    @Override
    public TxtViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        resources = viewGroup.getResources();
        TextView root_view = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_region, viewGroup, false);
        return new TxtViewViewHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull TxtViewViewHolder txtViewViewHolder, int i) {
        PointsObj item = getItem(i);
        txtViewViewHolder.setData(item.getName());
        if (item.getName().equals(choosenPointName)) {
            txtViewViewHolder.getNameTxtView().setTextColor(resources.getColor(R.color.colorPrimary));
            txtViewViewHolder.getNameTxtView().setCompoundDrawables(null, null, resources.getDrawable(R.drawable.ic_checked), null);
        } else {
            txtViewViewHolder.getNameTxtView().setTextColor(resources.getColor(R.color.colorAccent));
            txtViewViewHolder.getNameTxtView().setCompoundDrawables(null, null, null, null);
        }
        txtViewViewHolder.itemView.setOnClickListener(v -> {
            setChoosenPointName(item.getName());
            onPointChoosen.choosen(item);
            PickUpAddressFragment.setWasChoosePoint(true);
            act.onBackPressed();
        });
    }

    @Override
    public int getItemCount() {
        return pointsObjList.size();
    }

    private PointsObj getItem(int possition) {
        return pointsObjList.get(possition);
    }

}

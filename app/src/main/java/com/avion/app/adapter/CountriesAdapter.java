package com.avion.app.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.adapter.viewholder.CountryViewHolder;
import com.avion.app.entity.CountryEntity;

public class CountriesAdapter extends PagedListAdapter<CountryEntity, CountryViewHolder> {
    public FragmentActivity context;

    public CountriesAdapter(FragmentActivity context) {
        super(DIFF);
        this.context = context;
        if (MainActivity.countryData.getValue() != null) {
            currentSelectedID = MainActivity.countryData.getValue().getName();
            MainActivity.logstr("CURR_COUNTRY: " + currentSelectedID);
        }
    }

    public interface GetRegion {
        void get(CountryEntity regionEntity);
    }

    private GetRegion getRegion;

    public void setGetRegion(GetRegion getRegion) {
        this.getRegion = getRegion;
    }

    private String currentSelectedID = "";
    private Resources resources;

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        resources = viewGroup.getResources();
        View root_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_country, viewGroup, false);
        return new CountryViewHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder txtViewViewHolder, int i) {
        txtViewViewHolder.setName(getItem(i).getName());
        txtViewViewHolder.setCode(getItem(i).getCode());

        if (getItem(i).getName().equals(currentSelectedID)) {


            txtViewViewHolder.getNameView().setTextColor(resources.getColor(R.color.colorPrimary));
            txtViewViewHolder.getNameView().setCompoundDrawables(null, null, resources.getDrawable(R.drawable.ic_checked), null);
            txtViewViewHolder.getCodeView().setTextColor(resources.getColor(R.color.colorPrimary));
            txtViewViewHolder.getCodeView().setCompoundDrawables(null, null, resources.getDrawable(R.drawable.ic_checked), null);
        } else {
            txtViewViewHolder.getNameView().setTextColor(resources.getColor(R.color.colorAccent));
            txtViewViewHolder.getNameView().setCompoundDrawables(null, null, null, null);
            txtViewViewHolder.getCodeView().setTextColor(resources.getColor(R.color.colorAccent));
            txtViewViewHolder.getCodeView().setCompoundDrawables(null, null, null, null);
        }
        MainActivity.logstr("COUNTRY_ADAPTER:\r\n" + i + " : " + getItem(i).getName());

        txtViewViewHolder.itemView.setOnClickListener(v -> {

            getRegion.get(getItem(i));
            currentSelectedID = getItem(i).getName();
            notifyDataSetChanged();
            MainActivity.countryData.setValue(getItem(i));
            context.onBackPressed();
        });
    }

    private static DiffUtil.ItemCallback<CountryEntity> DIFF = new DiffUtil.ItemCallback<CountryEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CountryEntity regionEntity, @NonNull CountryEntity t1) {
            return regionEntity.getName() == t1.getName();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CountryEntity regionEntity, @NonNull CountryEntity t1) {
            return regionEntity.getName().equals(t1.getName());
        }
    };

}

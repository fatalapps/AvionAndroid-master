package com.avion.app.unit;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.avion.app.repository.QuaryAddressRepository;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class AutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {

    List<String> shippers;

    public AutoSuggestAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        shippers = new ArrayList<String>();
    }

    public void setData(List<String> stringList) {

    }

    @Override
    public int getCount() {
        return shippers.size();
    }

    @Override
    public String getItem(int index) {
        return shippers.get(index);
    }


    @Override
    public Filter getFilter() {

        Filter myFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    if (!shippers.contains(constraint.toString())) {
                        Logger.d("Start load address");
                        new QuaryAddressRepository(getContext()).getAdressList(constraint.toString(), address_list -> {
                            shippers = address_list;
                            Logger.d("Done");
                        });
                    }

                    filterResults.values = shippers;
                    filterResults.count = shippers.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };

        return myFilter;

    }
}
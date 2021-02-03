package com.avion.app.adapter.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.R;

public class CountryViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView code;
    private Resources resources;
    private Context context;

    public CountryViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.countrynametxt);
        code = itemView.findViewById(R.id.countrycodetxt);
        resources = itemView.getResources();
        context = itemView.getContext();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setCode(String code) {
        this.code.setText(code);
    }

    public TextView getNameView() {
        return name;
    }

    public TextView getCodeView() {
        return code;
    }
}

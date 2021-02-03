package com.avion.app.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TxtViewViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTxtView;

    public TextView getNameTxtView() {
        return nameTxtView;
    }

    public TxtViewViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTxtView = (TextView) itemView;
    }

    public void setData(String name) {
        nameTxtView.setText(name);
    }

}

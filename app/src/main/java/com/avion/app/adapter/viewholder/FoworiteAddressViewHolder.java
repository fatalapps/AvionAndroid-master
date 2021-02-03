package com.avion.app.adapter.viewholder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.R;
import com.avion.app.entity.FaworiteAddressEntity;

public class FoworiteAddressViewHolder extends RecyclerView.ViewHolder {

    private TextView address_name_txtview;
    private TextView address_txtview;
    public ImageView address_delete_imageview;
    private Resources resources;

    public FoworiteAddressViewHolder(@NonNull View itemView) {
        super(itemView);
        address_name_txtview = itemView.findViewById(R.id.address_name_txtview);
        address_txtview = itemView.findViewById(R.id.address_txtview);
        address_delete_imageview = itemView.findViewById(R.id.address_delete_imageview);
        resources = itemView.getResources();
    }

    public void setData(FaworiteAddressEntity faworiteAddressEntity) {
        address_name_txtview.setText(faworiteAddressEntity.getAddress_name());
        Drawable drawable = null;
        Log.d("Nek", String.valueOf(faworiteAddressEntity.getAddress_name_icon()));
        if (faworiteAddressEntity.getAddress_name_icon() > 0)
            drawable = resources.getDrawable(faworiteAddressEntity.getAddress_name_icon());
        address_name_txtview.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        address_txtview.setText(faworiteAddressEntity.getAddress());
    }

}

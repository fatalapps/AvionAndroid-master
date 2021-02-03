package com.avion.app.adapter.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.models.TarifObj;

public class TarifViewHolder extends RecyclerView.ViewHolder {

    private CardView cartype_cardview;
    private ImageView car_image;
    private TextView car_name_txtview;
    private TextView car_price_txtview;
    public ImageView car_info_imageview;

    private Resources resources;
    private Context context;

    public TarifViewHolder(@NonNull View itemView) {
        super(itemView);
        cartype_cardview = itemView.findViewById(R.id.cartype_cardview);
        car_image = itemView.findViewById(R.id.car_image);
        car_name_txtview = itemView.findViewById(R.id.car_name_txtview);
        car_price_txtview = itemView.findViewById(R.id.car_price_txtview);
        car_info_imageview = itemView.findViewById(R.id.car_info_imageview);
        resources = itemView.getResources();
        context = itemView.getContext();
    }

    @SuppressLint("SetTextI18n")
    public void setData(TarifObj tarifObj) {

        car_name_txtview.setText(tarifObj.getName());
        if (tarifObj.getPrice() == null) {
            car_price_txtview.setText(resources.getString(R.string.from_price) + " " + tarifObj.getMin() + " " + MainActivity.getCurrencySign(MainActivity.currency.getId()));
        } else {
            if (tarifObj.getPrice().equals("-1")) car_price_txtview.setText("");
            else
                car_price_txtview.setText(tarifObj.getPrice() + " " + MainActivity.getCurrencySign(MainActivity.currency.getId()));
        }
        if (tarifObj.getIcon() != 0)
            car_image.setImageDrawable(resources.getDrawable(tarifObj.getIcon()));

    }

    public void Select() {
        MainActivity.logstr("SELECT \r\n " + car_name_txtview.getText().toString());
        MainActivity.curr_itemm = car_name_txtview.getText().toString();
        cartype_cardview.setCardBackgroundColor(resources.getColor(R.color.white));
        cartype_cardview.setElevation(12);
        car_info_imageview.setVisibility(View.VISIBLE);
        car_price_txtview.setVisibility(View.VISIBLE);
        car_price_txtview.setTextColor(resources.getColor(R.color.colorPrimary));
        car_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void Unselect() {
        cartype_cardview.setCardBackgroundColor(resources.getColor(R.color.transperetn));
        cartype_cardview.setElevation(0);
        car_info_imageview.setVisibility(View.INVISIBLE);

        car_price_txtview.setTextColor(resources.getColor(R.color.desc_txt_color));
        car_image.setColorFilter(ContextCompat.getColor(context, R.color.desc_txt_color), android.graphics.PorterDuff.Mode.SRC_IN);
    }

}

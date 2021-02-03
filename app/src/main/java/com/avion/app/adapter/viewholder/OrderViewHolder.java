package com.avion.app.adapter.viewholder;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.action.MakeOrder;

import java.util.Objects;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private TextView order_date_textview;
    private TextView order_addressfrom_txtview;
    private TextView order_payedtype_txtxview;
    private TextView order_price_txtview;
    public ImageView order_edit_imageview;
    public Button order_cancel_btn;
    private TextView order_status_txtview;
    private Resources resources;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        order_date_textview = itemView.findViewById(R.id.order_date_textview);
        order_addressfrom_txtview = itemView.findViewById(R.id.order_addressfrom_txtview);
        order_payedtype_txtxview = itemView.findViewById(R.id.order_payedtype_txtxview);
        order_price_txtview = itemView.findViewById(R.id.order_price_txtview);
        order_edit_imageview = itemView.findViewById(R.id.order_edit_imageview);
        order_cancel_btn = itemView.findViewById(R.id.order_cancel_btn);
        order_status_txtview = itemView.findViewById(R.id.order_status_txtview);
        resources = itemView.getResources();
    }

    @SuppressLint("SetTextI18n")
    public void setData(MakeOrder makeOrder) {
        if (makeOrder.getDate() != null)
            order_date_textview.setText(makeOrder.getDate().replace("/", ".") + " " + makeOrder.getTime());
        order_addressfrom_txtview.setText(Objects.requireNonNull(makeOrder.getFrom()).getFullAddress());
        order_price_txtview.setText(makeOrder.getTotal_price().toString().replace(".0", "") + " " + MainActivity.getCurrencySign(makeOrder.getPayment() != null ? Integer.parseInt(makeOrder.getPayment().getCurrency() != null ? makeOrder.getPayment().getCurrency() : "1") : 1));
        switch (makeOrder.getStatus()) {
            case 0:
                order_status_txtview.setVisibility(View.INVISIBLE);
                order_cancel_btn.setVisibility(View.VISIBLE);
                break;
            case 1:
                order_status_txtview.setVisibility(View.VISIBLE);
                order_cancel_btn.setVisibility(View.INVISIBLE);
                order_status_txtview.setTextColor(Color.parseColor("#FFFFD335"));
                order_status_txtview.setText(resources.getString(R.string.done_order));
                order_edit_imageview.setVisibility(View.INVISIBLE);
                break;
            case 2:
                order_status_txtview.setVisibility(View.VISIBLE);
                order_cancel_btn.setVisibility(View.INVISIBLE);
                order_status_txtview.setText(resources.getString(R.string.canceled));
                order_status_txtview.setTextColor(resources.getColor(R.color.red));
                order_edit_imageview.setVisibility(View.INVISIBLE);
                break;
        }
    }

}

package com.avion.app.adapter.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.adapter.TarifAdapter;
import com.avion.app.fragment.ChooseTimeFragment;
import com.avion.app.fragment.MakeOrderFragment;
import com.avion.app.fragment.TarifRecyclerInfo;
import com.avion.app.models.TarifObj;

public class TarifRecyclerHolder extends RecyclerView.ViewHolder {

    private CardView cartype_cardview;
    private ImageView car_image;
    private TextView car_name_txtview;
    private TextView trafic_fixed_price_txtview, trafic_passeger_num_txtcview;
    private TextView trafic_car_name_txtview, trafic_luggage_space_txtview, trafic_waiting_time_txtview, trafic_paid_waiting_time_txtview;
    public ImageView car_info_imageview, trafic_down_imageview;
    public TarifObj tarO;
    public TarifAdapter tAd;
    private Resources resources;
    private Context context;
    Button trafic_select_btn;

    public TarifRecyclerHolder(@NonNull View itemView) {
        super(itemView);
        cartype_cardview = itemView.findViewById(R.id.cartype_cardview);
        car_image = itemView.findViewById(R.id.trafic_pic_imageview);
        car_name_txtview = itemView.findViewById(R.id.trafic_name_txtview);
        trafic_fixed_price_txtview = itemView.findViewById(R.id.trafic_fixed_price_txtview);
        trafic_car_name_txtview = itemView.findViewById(R.id.trafic_car_name_txtview);
        trafic_passeger_num_txtcview = itemView.findViewById(R.id.trafic_passeger_num_txtcview);
        trafic_luggage_space_txtview = itemView.findViewById(R.id.trafic_luggage_space_txtview);
        trafic_waiting_time_txtview = itemView.findViewById(R.id.trafic_waiting_time_txtview);
        car_info_imageview = itemView.findViewById(R.id.car_info_imageview);
        trafic_down_imageview = itemView.findViewById(R.id.trafic_down_imageview);
        resources = itemView.getResources();
        context = itemView.getContext();
        trafic_select_btn = itemView.findViewById(R.id.trafic_select_btn);
        trafic_paid_waiting_time_txtview = itemView.findViewById(R.id.trafic_paid_waiting_time_txtview);

    }

    public void scrollToPos(int position, LinearLayoutManager lm, RecyclerView.SmoothScroller sm, RecyclerView recyclerView) {
        trafic_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.setTargetPosition(position);
                trafic_down_imageview.callOnClick();
                tAd.setSelectedTarifIcon(tarO.getIcon());
                lm.startSmoothScroll(sm);
                MainActivity.curr_itemm = car_name_txtview.getText().toString();
                TarifViewHolder tr = new TarifViewHolder(recyclerView.findViewHolderForAdapterPosition(position).itemView);
                // if(recyclerView.findViewHolderForAdapterPosition(position)!=null) tr.Select();

                //Toast.makeText(context,MainActivity.curr_itemm, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void settAd(TarifAdapter ta) {
        this.tAd = ta;
    }

    public void setDismiss(TarifRecyclerInfo bt) {
        trafic_down_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setData(TarifObj tarifObj) {
        tarO = tarifObj;
        car_name_txtview.setText(tarifObj.getName());
        trafic_car_name_txtview.setText(tarifObj.getCarname());
        trafic_passeger_num_txtcview.setText(tarifObj.getPassengers());
        trafic_luggage_space_txtview.setText(tarifObj.getBaggage());
        trafic_paid_waiting_time_txtview.setText(tarifObj.getPricePerMinute().toString().replace(".0", "") + " " + MainActivity.getCurrencySign(MainActivity.currency.getId()) + "/мин");
        if (tarifObj.getPrice() == null) {
            trafic_fixed_price_txtview.setText(resources.getString(R.string.from_price) + " " + tarifObj.getMin() + " " + MainActivity.getCurrencySign(MainActivity.currency.getId()));
        } else {
            trafic_fixed_price_txtview.setText(tarifObj.getPrice() + " " + MainActivity.getCurrencySign(MainActivity.currency.getId()));
        }
        if (tarifObj.getPicture() != 0)
            car_image.setImageDrawable(resources.getDrawable(tarifObj.getPicture()));

        if (MakeOrderFragment.Companion.getGlobalOrderEntity() != null) {
            String freeTime = "10";


            if (MakeOrderFragment.Companion.getGlobalOrderEntity().getFrom() == null) {
                freeTime = "10";
            } else if (ChooseTimeFragment.Companion.txtAddressTypeToInt(MakeOrderFragment.Companion.getGlobalOrderEntity().getFrom().getType()) == ChooseTimeFragment.Companion.getTYPE_ADDRESS()) {
                freeTime = "10";
            } else if (ChooseTimeFragment.Companion.txtAddressTypeToInt(MakeOrderFragment.Companion.getGlobalOrderEntity().getFrom().getType()) == ChooseTimeFragment.Companion.getTYPE_TRAIN()) {
                freeTime = "20";
                if (MakeOrderFragment.Companion.getGlobalOrderEntity().getOptions().getMeeting_with_a_table() != null) {
                    freeTime = "20";
                }
            } else if (ChooseTimeFragment.Companion.txtAddressTypeToInt(MakeOrderFragment.Companion.getGlobalOrderEntity().getFrom().getType()) == ChooseTimeFragment.Companion.getTYPE_AIRPORT()) {
                freeTime = "60";
                if (MakeOrderFragment.Companion.getGlobalOrderEntity().getCartotime() == true) {
                    freeTime = "10";
                }
            }
            trafic_waiting_time_txtview.setText(freeTime + " мин");
        }

    }

    public void Select() {

    }


    public void Unselect() {

    }

}

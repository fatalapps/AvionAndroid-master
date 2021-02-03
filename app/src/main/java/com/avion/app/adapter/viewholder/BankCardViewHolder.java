package com.avion.app.adapter.viewholder;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.entity.BankCardEntity;

public class BankCardViewHolder extends RecyclerView.ViewHolder {

    private Resources resources;
    private ImageView bankcard_icon_imageview;
    public TextView bankcard_num_txtview;
    public RadioButton bankcard_checkBox;
    public ConstraintLayout lay;
    public BankCardEntity ent;
    public ImageView bankcard_edit_imageview;
    public View itemv;

    public BankCardViewHolder(@NonNull View itemView) {
        super(itemView);
        resources = itemView.getResources();
        bankcard_icon_imageview = itemView.findViewById(R.id.bankcard_icon_imageview);
        bankcard_num_txtview = itemView.findViewById(R.id.bankcard_num_txtview);
        bankcard_checkBox = itemView.findViewById(R.id.bankcard_checkBox);
        lay = itemView.findViewById(R.id.bcard);
        itemv = itemView;
    }

    @SuppressLint("SetTextI18n")
    public void setData(BankCardEntity bankCardEntity) {


        ent = bankCardEntity;
        if (bankCardEntity.getCard_num().equals("cash")) {
            bankcard_num_txtview.setText(resources.getString(R.string.nalicknie));
            bankcard_icon_imageview.setImageDrawable(resources.getDrawable(R.drawable.cash_ico));
        } else {
            if (bankCardEntity.getCard_num().equals("gpay")) {
                bankcard_num_txtview.setText(("Google Pay"));
                bankcard_icon_imageview.setImageDrawable(resources.getDrawable(R.drawable.gpay));
            } else {
                if (bankCardEntity.getCard_num().equals("bonusp")) {
                    bankcard_num_txtview.setText(resources.getString(R.string.payBonus));
                    bankcard_icon_imageview.setImageDrawable(resources.getDrawable(R.drawable.icon_bonuses));
                } else {
                    String num = "XXXX XXXX XXXX " + bankCardEntity.getCard_num().substring(bankCardEntity.getCard_num().length() - 4);
                    MainActivity.logstr(num);
                    bankcard_num_txtview.setText(num);
                    bankcard_icon_imageview.setImageDrawable(getBankCardIcon(bankCardEntity.getCard_num()));
                }
            }
        }
    }

    private Drawable getBankCardIcon(String num) {
        if (num.equals("cash")) return resources.getDrawable(R.drawable.cash_ico);
        if (num.equals("gpay")) return resources.getDrawable(R.drawable.gpay);
        if (num.equals("bonusp")) return resources.getDrawable(R.drawable.icon_bonuses);
        if (num.startsWith("4"))
            return resources.getDrawable(R.drawable.visa_ico);
        else {
            if (num.startsWith("51") || num.startsWith("52") || num.startsWith("53") || num.startsWith("54") || num.startsWith("55")) {
                return resources.getDrawable(R.drawable.master_ico);
            } else return resources.getDrawable(R.drawable.def_card);
        }

    }

}

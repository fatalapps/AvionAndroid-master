package com.avion.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.avion.app.R;
import com.avion.app.adapter.viewholder.BankCardViewHolder;
import com.avion.app.entity.BankCardEntity;
import com.avion.app.unit.NekConfigs;

public class BankCardAdapter extends PagedListAdapter<BankCardEntity, BankCardViewHolder> {
    public FragmentActivity context;

    public BankCardAdapter(FragmentActivity cont) {
        super(DIFF);
        context = cont;

    }

    private String selectedBankCardID = "";
    private boolean cash = false;

    public String getSelectedBankCardID() {
        return selectedBankCardID;
    }

    public interface BankCardCallBack {
        void onEdit(BankCardEntity bankCardEntity);
    }

    private BankCardCallBack bankCardCallBack;

    public void setBankCardCallBack(BankCardCallBack bankCardCallBack) {
        this.bankCardCallBack = bankCardCallBack;
    }

    @Override
    public void submitList(PagedList<BankCardEntity> pagedList) {
        if (pagedList.size() > 0)
            selectedBankCardID = NekConfigs.paymethod;
        super.submitList(pagedList);
    }

    @NonNull
    @Override
    public BankCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bankcard, viewGroup, false);
        return new BankCardViewHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankCardViewHolder bankCardViewHolder, int i) {

        BankCardEntity item = getItem(i);
        bankCardViewHolder.setData(item);
        if (selectedBankCardID.equals(item.getId()))
            bankCardViewHolder.bankcard_checkBox.setChecked(true);
        else
            bankCardViewHolder.bankcard_checkBox.setChecked(false);
        bankCardViewHolder.bankcard_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedBankCardID = item.getId();
                NekConfigs.paymethod = selectedBankCardID;
                NekConfigs.last4 = getPrefix(item.getCard_num()) + ((item.getCard_num().equals("gpay") || item.getCard_num().equals("bonusp")) ? "" : item.getCard_num().substring(item.getCard_num().length() - 4));
                NekConfigs.paylogo = getScaledBankCardIcon(item.getCard_num());
                notifyDataSetChanged();
                context.onBackPressed();
            }
        });
        bankCardViewHolder.lay.setOnClickListener(v -> {
            selectedBankCardID = item.getId();
            NekConfigs.paymethod = selectedBankCardID;
            NekConfigs.last4 = getPrefix(item.getCard_num()) + ((item.getCard_num().equals("gpay") || item.getCard_num().equals("bonusp")) ? "" : item.getCard_num().substring(item.getCard_num().length() - 4));
            NekConfigs.paylogo = getScaledBankCardIcon(item.getCard_num());
            notifyDataSetChanged();
            context.onBackPressed();
        });

    }

    private static DiffUtil.ItemCallback<BankCardEntity> DIFF = new DiffUtil.ItemCallback<BankCardEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull BankCardEntity bankCardEntity, @NonNull BankCardEntity t1) {
            return bankCardEntity.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BankCardEntity bankCardEntity, @NonNull BankCardEntity t1) {
            return bankCardEntity.getCard_num().equals(t1.getCard_num());
        }
    };

    private int getBankCardIcon(String num) {
        if (num.equals("cash")) return (R.drawable.cash_ico);
        if (num.startsWith("4"))
            return (R.drawable.visa_ico);
        else {
            if (num.startsWith("51") || num.startsWith("52") || num.startsWith("53") || num.startsWith("54") || num.startsWith("55")) {
                return (R.drawable.master_ico);
            } else return R.drawable.def_card;
        }

    }

    private String getPrefix(String num) {
        if (num.equals("cash")) return "";
        if (num.startsWith("4"))
            return "VISA";
        else {
            if (num.equals("gpay")) return "Google Pay";
            else {
                if (num.equals("bonusp")) return "Оплата бонусами";
                else {
                    if (num.startsWith("51") || num.startsWith("52") || num.startsWith("53") || num.startsWith("54") || num.startsWith("55")) {
                        return "MASTER";
                    } else return "CARD";
                }
            }
        }
    }

    private int getScaledBankCardIcon(String num) {
        if (num.equals("cash")) return (R.drawable.cash_ico);
        if (num.equals("gpay")) return (R.drawable.gscaled);
        if (num.equals("bonusp")) return (R.drawable.scaled_bonuses);
        if (num.startsWith("4"))
            return (R.drawable.scaled_visa);
        else {
            if (num.startsWith("51") || num.startsWith("52") || num.startsWith("53") || num.startsWith("54") || num.startsWith("55")) {
                return (R.drawable.scaled_master);
            } else return R.drawable.def_card;
        }

    }
}

package com.avion.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avion.app.MainActivity;
import com.avion.app.R;
import com.avion.app.action.MakeOrder;
import com.avion.app.adapter.viewholder.OrderViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    public enum SelectedList {
        upcomming, complated
    }

    private List<MakeOrder> makeOrderList = new ArrayList<>();
    private List<MakeOrder> makeOrderListFull = new ArrayList<>();

    private SelectedList selectedList = SelectedList.upcomming;

    public void setSelectedList(SelectedList selectedList) {
        this.selectedList = selectedList;
        this.updateData();
    }

    public interface OnOrderCallBack {
        void onCancel(MakeOrder makeOrder);

        void onEdit(MakeOrder makeOrder);

        void onClick(MakeOrder makeOrder);
    }

    private OnOrderCallBack onOrderCallBack;

    public void setOnOrderCallBack(OnOrderCallBack onOrderCallBack) {
        this.onOrderCallBack = onOrderCallBack;
        updateData();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
        return new OrderViewHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {
        final MakeOrder item = getItem(i);
        MainActivity.logstr("i:" + i);
        orderViewHolder.setData(item);
        orderViewHolder.itemView.setOnClickListener(v -> onOrderCallBack.onClick(item));
        orderViewHolder.order_cancel_btn.setOnClickListener(v -> onOrderCallBack.onCancel(item));
        orderViewHolder.order_edit_imageview.setOnClickListener(v -> onOrderCallBack.onEdit(item));
    }

    @Override
    public int getItemCount() {
        return makeOrderList.size();
    }

    public void submitList(List<MakeOrder> makeOrderList) {
        this.makeOrderListFull = makeOrderList;
        updateData();
    }

    private void updateData() {
        makeOrderList.clear();
        for (MakeOrder makeOrder : makeOrderListFull) {
            if (selectedList == SelectedList.upcomming && makeOrder.getStatus() == 0)
                makeOrderList.add(makeOrder);
            else if (selectedList == SelectedList.complated && (makeOrder.getStatus() == 1 || makeOrder.getStatus() == 2))
                makeOrderList.add(makeOrder);
//            Log.e("DATE",makeOrder.getDate());

        }
        makeOrderList.sort(new Comparator<MakeOrder>() {
            @Override
            public int compare(MakeOrder makeOrder, MakeOrder makeOrder2) {
                int comparee = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    comparee = sdf.parse(makeOrder2.getDate()).compareTo(sdf.parse(makeOrder.getDate()));
                } catch (Exception e) {
                }
                return comparee;
            }
        });
        notifyDataSetChanged();
    }

    private MakeOrder getItem(int position) {
        return makeOrderList.get(position);
    }

}

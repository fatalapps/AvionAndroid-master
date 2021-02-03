package com.avion.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.avion.app.R;
import com.avion.app.adapter.viewholder.FoworiteAddressViewHolder;
import com.avion.app.entity.FaworiteAddressEntity;

import java.util.Objects;

public class FavoritesAdapter extends PagedListAdapter<FaworiteAddressEntity, FoworiteAddressViewHolder> {

    public FavoritesAdapter() {
        super(DIFF);
    }

    public interface OnFavoriteCallBack {
        void onDelete(FaworiteAddressEntity faworiteAddressEntity);

        void onChoose(FaworiteAddressEntity faworiteAddressEntity);
    }

    private OnFavoriteCallBack onFavoriteCallBack;

    public void setOnFavoriteCallBack(OnFavoriteCallBack onFavoriteCallBack) {
        this.onFavoriteCallBack = onFavoriteCallBack;
    }

    @NonNull
    @Override
    public FoworiteAddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_faworite_address, viewGroup, false);
        return new FoworiteAddressViewHolder(root_view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoworiteAddressViewHolder foworiteAddressViewHolder, int i) {
        foworiteAddressViewHolder.setData(getItem(i));
        try {
            foworiteAddressViewHolder.address_delete_imageview.setOnClickListener(v -> onFavoriteCallBack.onDelete(getItem(i)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onFavoriteCallBack != null && Objects.requireNonNull(getCurrentList()).size() > i) {
            try {
                FaworiteAddressEntity item = getItem(i);
                foworiteAddressViewHolder.itemView.setOnClickListener(v -> onFavoriteCallBack.onChoose(item));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static DiffUtil.ItemCallback<FaworiteAddressEntity> DIFF = new DiffUtil.ItemCallback<FaworiteAddressEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull FaworiteAddressEntity faworiteAddressEntity, @NonNull FaworiteAddressEntity t1) {
            return faworiteAddressEntity.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FaworiteAddressEntity faworiteAddressEntity, @NonNull FaworiteAddressEntity t1) {
            return faworiteAddressEntity.getAddress().equals(faworiteAddressEntity.getAddress());
        }
    };

}

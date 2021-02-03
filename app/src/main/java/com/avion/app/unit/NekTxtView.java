package com.avion.app.unit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.avion.app.R;

public class NekTxtView extends FrameLayout {
    private ImageView nektxt_icon_imageview;
    private TextView nektxt_label_txtview;
    private TextView nektxt_data_txtview;
    private ImageView nektxt_nav_icon;
    private TextView options_size;

    public NekTxtView(Context context) {
        super(context);
        initView();
    }

    public NekTxtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NekTxtView,
                0, 0);
        String data = a.getString(R.styleable.NekTxtView_data);
        if (data != null) setData(data);
        Drawable icon = a.getDrawable(R.styleable.NekTxtView_img);
        if (icon != null) setIcon(icon);
        String label = a.getString(R.styleable.NekTxtView_label);
        if (label != null) setLabel(label);
        Boolean showNav = a.getBoolean(R.styleable.NekTxtView_showNav, true);
        setShowNav(showNav);
        a.recycle();
    }

    public NekTxtView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public NekTxtView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View root_view = inflate(getContext(), R.layout.txtview_with_label_icon, null);
        nektxt_icon_imageview = root_view.findViewById(R.id.nektxt_icon_imageview);
        nektxt_label_txtview = root_view.findViewById(R.id.nektxt_label_txtview);
        nektxt_data_txtview = root_view.findViewById(R.id.nektxt_data_txtview);
        nektxt_nav_icon = root_view.findViewById(R.id.nektxt_nav_icon);
        options_size = root_view.findViewById(R.id.options_size);
        options_size.setVisibility(View.GONE);
        addView(root_view);
    }

    public void setIcon(Drawable icon) {
        nektxt_icon_imageview.setImageDrawable(icon);
    }

    public void setLabel(String label) {
        nektxt_label_txtview.setText(label);
    }

    public void setData(String data) {
        nektxt_data_txtview.setText(data);
    }

    public void hideLabel() {
        nektxt_label_txtview.setVisibility(GONE);
    }

    public void hideOptions() {
        options_size.setVisibility(View.GONE);
    }

    public void setOptions(int s) {
        options_size.setVisibility(View.VISIBLE);
        options_size.setText(String.valueOf(s));
    }

    public void setShowNav(Boolean nek) {
        if (nek)
            nektxt_nav_icon.setVisibility(VISIBLE);
        else
            nektxt_nav_icon.setVisibility(INVISIBLE);
    }

}

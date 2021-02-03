package com.avion.app.unit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avion.app.MainActivity;
import com.avion.app.R;

public class OptionView extends FrameLayout {

    private TextView option_name;
    private TextView option_price_txtview;
    private CheckBox option_checkBox;
    private ImageView option_next_imageView;

    public interface OnCallBack {
        void onNext();

        void onCkecked(boolean check);
    }

    private OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public OptionView(Context context) {
        super(context);
        initView();
    }

    public OptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OptionView,
                0, 0);
        String name = a.getString(R.styleable.OptionView_name);
        if (name != null)
            setName(name);
        useCheckBox(a.getBoolean(R.styleable.OptionView_useCheckbox, false));
        a.recycle();
    }

    public OptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View root_view = inflate(getContext(), R.layout.option_view, null);
        option_name = root_view.findViewById(R.id.option_name);
        option_price_txtview = root_view.findViewById(R.id.option_price_txtview);
        option_checkBox = root_view.findViewById(R.id.option_checkBox);
        option_next_imageView = root_view.findViewById(R.id.option_next_imageView);
        addView(root_view);
        this.setOnClickListener(v -> {
            if (onCallBack == null)
                return;
            onCallBack.onNext();
        });
        option_checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onCallBack == null)
                return;
            onCallBack.onCkecked(isChecked);
        });
    }

    public void setName(String name) {
        option_name.setText(name);
    }

    @SuppressLint("SetTextI18n")
    public void setPrice(int price) {
        if (price == 0) {
            option_price_txtview.setText(getContext().getString(R.string.free));

            return;
        }
        option_price_txtview.setText("+ " + price + MainActivity.getCurrencySign(MainActivity.currency.getId()));
    }

    public Double getPrice() {
        try {
            return Double.parseDouble(option_price_txtview.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public void useCheckBox(boolean nek) {
        if (nek) {
            option_checkBox.setVisibility(VISIBLE);
            option_next_imageView.setVisibility(INVISIBLE);
        } else {
            option_checkBox.setVisibility(INVISIBLE);
            option_next_imageView.setVisibility(VISIBLE);
        }
    }

    public void setCkecked(boolean nek) {
        option_checkBox.setChecked(nek);
    }

}

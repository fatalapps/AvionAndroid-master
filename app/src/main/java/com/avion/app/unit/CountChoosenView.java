package com.avion.app.unit;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avion.app.R;

public class CountChoosenView extends FrameLayout {

    private TextView countchoose_title_txtview;
    private Button countchoose_minus_btn;
    private TextView countchoose_num_txtview;
    private Button count_plus_btn;

    public CountChoosenView(Context context) {
        super(context);
        initView();
    }

    public CountChoosenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CountChoosenView,
                0, 0
        );
        setTitle(a.getString(R.styleable.CountChoosenView_title));
        setCount(a.getInteger(R.styleable.CountChoosenView_count, 0));
    }

    public CountChoosenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CountChoosenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View root_view = inflate(getContext(), R.layout.count_choose_view, null);
        countchoose_title_txtview = root_view.findViewById(R.id.countchoose_title_txtview);
        countchoose_minus_btn = root_view.findViewById(R.id.countchoose_minus_btn);
        countchoose_num_txtview = root_view.findViewById(R.id.countchoose_num_txtview);
        count_plus_btn = root_view.findViewById(R.id.count_plus_btn);

        countchoose_minus_btn.setOnClickListener(v -> {
            int count = getCount();
            if (count == 0)
                return;
            count -= 1;
            setCount(count);
        });

        count_plus_btn.setOnClickListener(v -> {
            int count = getCount();
            count += 1;
            setCount(count);
        });

        addView(root_view);
    }

    public int getCount() {
        return Integer.parseInt(countchoose_num_txtview.getText().toString());
    }

    public void setTitle(String title) {
        if (title == null)
            return;
        countchoose_title_txtview.setText(title);
    }

    public void setCount(int count) {
        if (count < 0)
            return;
        countchoose_num_txtview.setText(String.valueOf(count));
    }

}

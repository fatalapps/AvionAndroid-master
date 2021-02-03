package com.avion.app.unit;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avion.app.R;

public class ChooseLanguageView extends FrameLayout {

    private TextView languagechoose_name_txtview;
    private CheckBox languagechoose_checkbox;

    public ChooseLanguageView(Context context) {
        super(context);
        initView();
    }

    public ChooseLanguageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ChooseLanguageView,
                0, 0);
        setName(a.getString(R.styleable.ChooseLanguageView_name_lang));
        setCahecked(a.getBoolean(R.styleable.ChooseLanguageView_isChecked, false));
    }

    public ChooseLanguageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ChooseLanguageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View root_view = inflate(getContext(), R.layout.choose_language_option, null);
        languagechoose_name_txtview = root_view.findViewById(R.id.languagechoose_name_txtview);
        languagechoose_checkbox = root_view.findViewById(R.id.languagechoose_checkbox);
        addView(root_view);
    }

    public void setName(String name) {
        if (name == null)
            return;
        languagechoose_name_txtview.setText(name);
    }

    public void setCahecked(boolean isChecked) {
        languagechoose_checkbox.setChecked(isChecked);
    }

    public boolean getChecked() {
        return languagechoose_checkbox.isChecked();
    }

}

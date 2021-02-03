package com.avion.app.unit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avion.app.R;

public class NekTextField extends LinearLayout {

    private ImageView nek_textfield_icon_imageview;
    private EditText nek_textfield_edittext;

    public NekTextField(@NonNull Context context) {
        super(context);
        initView();
    }

    public NekTextField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NekTextField,
                0, 0);
        String hint = a.getString(R.styleable.NekTextField_hint);
        if (hint != null) setHint(hint);


        String text = a.getString(R.styleable.NekTextField_text);
        if (text != null) setText(text);
        a.recycle();
    }

    public NekTextField(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.nek_textfield, null);
        nek_textfield_icon_imageview = view.findViewById(R.id.nek_textfield_icon_imageview);
        nek_textfield_edittext = view.findViewById(R.id.nek_textfield_edittext);
        addView(view);
    }

    public void setIcon(Drawable icon) {
        nek_textfield_icon_imageview.setImageDrawable(icon);
    }

    public void setHint(String hint) {
        nek_textfield_edittext.setHint(hint);
    }

    public void setText(String txt) {
        nek_textfield_edittext.setText(txt);
    }

    public String getText() {
        return nek_textfield_edittext.getText().toString();
    }
}

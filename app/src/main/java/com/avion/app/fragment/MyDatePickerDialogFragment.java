package com.avion.app.fragment;

import com.avion.app.R;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;


public class MyDatePickerDialogFragment extends DatePickerDialogFragment {

    @Override
    protected void initChild() {
        super.initChild();
        mCancelButton.setText(R.string.cancel_date);
        mDecideButton.setText(R.string.accept_date);
        //mDatePicker.setShowCurtain(false);
    }
}
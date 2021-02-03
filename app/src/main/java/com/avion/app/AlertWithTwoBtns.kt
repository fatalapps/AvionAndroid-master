package com.avion.app

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class AlertWithTwoBtns(
        context: Context,
        title: String,
        message: String,
        possiveBtnTitle: String,
        negativeBtnTitle: String,
        onClickPossitiveBtn: DialogInterface.OnClickListener,
        onClickNegativeBtn: DialogInterface.OnClickListener
) {

    private var alert: AlertDialog.Builder = AlertDialog.Builder(context)

    init {
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton(possiveBtnTitle, onClickPossitiveBtn)
        alert.setNegativeButton(negativeBtnTitle, onClickNegativeBtn)
        alert.create()
    }

    fun show() {
        alert.show()
    }
}
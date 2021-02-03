package com.avion.app.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.fragment.viewmodel.OrderInfoViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import io.fabric.sdk.android.services.common.CommonUtils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_feed_back.*


class FeedBackFragment : NekFragment() {
    override fun onStartProgress() {
        progressBar2.visibility = View.VISIBLE
        button2.visibility = View.INVISIBLE
    }

    override fun onCreateViewModel(): NekViewModel {
        mViewModel.nekFragment = this
        return mViewModel
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
        progressBar2.visibility = View.INVISIBLE
        button2.visibility = View.VISIBLE
    }

    private val mViewModel: OrderInfoViewModel by lazy { ViewModelProviders.of(activity!!).get(OrderInfoViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.avion.app.R.layout.fragment_feed_back, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        onStopProgress()
        setupUI(frameLayout14)
        button2.setOnClickListener {
            if (editText.text.toString().isEmpty() || ratingBar2.rating.toDouble() == 0.0 || ratingBar.rating.toDouble() == 0.0) {
                Toast.makeText(context!!, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                mViewModel.rate(editText.text.toString(), ratingBar.rating.toDouble()).observe(this, Observer {
                    if (it) {
                        onStopProgress()
                        mViewModel.autoRate = ratingBar2.rating
                        mViewModel.driverRate = ratingBar.rating
                        mViewModel.orderComment = editText.text.toString()
                        activity!!.onBackPressed()
                    }
                })
            }
        }

        ratingBar.rating = mViewModel.driverRate
        ratingBar2.rating = mViewModel.autoRate
        editText.setText(mViewModel.orderComment)



        textView7.text = mViewModel.orderdate

        mViewModel.driver.observe(this, Observer {
            textView10.text = "Водитель: " + it.family!!.split(" ")[1] + " " + it.family.split(" ")[0][0] + "."   //name_slitted[1] + " " + name_slitted[0][0]+"."
            textView11.text = "${it.abrand} ${it.amodel} (${it.autoColor}, ${it.autoNumber})"
        })

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideKeyboard(this.context, view)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

}

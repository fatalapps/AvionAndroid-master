package com.avion.app.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.avion.app.R
import com.avion.app.fragment.viewmodel.BankCardViewModel
import kotlinx.android.synthetic.main.fragment_add_payment.*

class AddPaymentFragment : Fragment() {

    private val bankCardViewMode: BankCardViewModel by lazy { ViewModelProviders.of(activity!!).get(BankCardViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //addpayment_num_editTextWShadowView.makeForCardNumber()
        //_addpayment_ex_editTextWShadowView.makeDorCardDate()

        addpayment_button.setOnClickListener {


            activity!!.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 22) {

        }
    }


}

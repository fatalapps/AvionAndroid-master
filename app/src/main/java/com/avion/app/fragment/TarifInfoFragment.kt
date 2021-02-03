package com.avion.app.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.models.TarifObj
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_fragment_tarif_info.*


class TarifInfoFragment : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root_view = inflater.inflate(R.layout.bottom_fragment_tarif_info, container, false)
        // trafic_name_txtview = root_view.findViewById(R.id.trafic_name_txtview)
        return root_view
    }

    private var tarifObj: TarifObj? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        assert(tarifObj != null)
        trafic_name_txtview.text = tarifObj!!.name
        trafic_car_name_txtview.text = tarifObj!!.carname
        trafic_pic_imageview.setImageDrawable(resources.getDrawable(tarifObj!!.picture))
        if (tarifObj!!.price != null) {
            trafic_fixed_price_txtview.text = tarifObj!!.price.toString() + " " + resources.getString(R.string.rub)
        } else {
            trafic_fixed_price_txtview.text = resources.getString(R.string.from_price) + " " + tarifObj!!.min.toString() + " " + resources.getString(R.string.rub)
        }

        MainActivity.logstr(tarifObj!!.pricePerMinute.toString() + " PER_MIN_PRICE")
        trafic_paid_waiting_time_txtview.text = tarifObj!!.pricePerMinute.toString() + "р/мин"
        trafic_passeger_num_txtcview.text = tarifObj!!.passengers.toString()
        trafic_luggage_space_txtview.text = tarifObj!!.baggage
        MainActivity.logstr("PAY_TIME: " + tarifObj!!.pricePerMinute.toString())
        trafic_paid_waiting_time_txtview.text = tarifObj!!.pricePerMinute.toString()
        //trafic_bonus_milles_txtview.text    = tarifObj!!.bonnus_miles.toString()

        trafic_down_imageview.setOnClickListener { this.dismiss() }
        trafic_select_btn.setOnClickListener { this.dismiss() }

        this.dialog!!.setOnShowListener { dialog ->
            val bottomSheet = this.dialog!!.window
                    .findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val coordinatorLayout = bottomSheet.parent as CoordinatorLayout
            val bottomSheetBehavior = BottomSheetBehavior.from<FrameLayout>(bottomSheet)
            bottomSheetBehavior.peekHeight = bottomSheet.height
            coordinatorLayout.parent.requestLayout()
        }

        if (MakeOrderFragment.GlobalOrderEntity != null) {
            var freeTime = "10"


            if (MakeOrderFragment.GlobalOrderEntity?.from == null) {
                freeTime = "10"
            } else if (ChooseTimeFragment.txtAddressTypeToInt(MakeOrderFragment.GlobalOrderEntity!!.from!!.type) == ChooseTimeFragment.TYPE_ADDRESS) {
                freeTime = "10"
            } else if (ChooseTimeFragment.txtAddressTypeToInt(MakeOrderFragment.GlobalOrderEntity!!.from!!.type) == ChooseTimeFragment.TYPE_TRAIN) {
                freeTime = "20"
                if (MakeOrderFragment.GlobalOrderEntity?.options?.meeting_with_a_table != null) {
                    freeTime = "20"
                }
            } else if (ChooseTimeFragment.txtAddressTypeToInt(MakeOrderFragment.GlobalOrderEntity!!.from!!.type) == ChooseTimeFragment.TYPE_AIRPORT) {
                freeTime = "60"
                if (MakeOrderFragment.GlobalOrderEntity?.cartotime == true) {
                    freeTime = "10"
                }
            }
            trafic_waiting_time_txtview.text = "$freeTime мин"
        }

    }


    companion object {
        var fragmentTransaction: FragmentTransaction? = null
        var fragmentManager: FragmentManager? = null
        fun getInstance(transaction: FragmentTransaction): TarifInfoFragment {
            fragmentTransaction = transaction
            return TarifInfoFragment()
        }

        fun getInstance(manager: FragmentManager): TarifInfoFragment {
            fragmentManager = manager
            return TarifInfoFragment()
        }
    }

    @SuppressLint("SetTextI18n")
    fun show(tarifObj: TarifObj, manager: FragmentManager) {
        this.tarifObj = tarifObj
        MainActivity.logstr(tarifObj.name + " OLD")
        show(manager, this.tag)
    }

}
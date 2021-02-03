package com.avion.app.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.avion.app.R
import kotlinx.android.synthetic.main.fragment_car.*

class CarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_car, container, false)
    }

    var cwidth = 0
    override fun onViewCreated(root_view: View, savedInstanceState: Bundle?) {
        val carType = arguments!!.getInt("carType")
        val isSelected = arguments!!.getBoolean("isSelected")
        if (isSelected) {
            car_cardview.setCardBackgroundColor(resources.getColor(R.color.white))
            car_icon_imageview.setColorFilter(resources.getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN)
            car_name_txtview.setTextColor(resources.getColor(R.color.colorPrimary))
            car_price_txtview.visibility = View.VISIBLE
            car_cardview.cardElevation = 22f
            card_info_imagebtn.visibility = View.VISIBLE
        } else {
            car_cardview.setCardBackgroundColor(resources.getColor(R.color.transperetn))
            car_icon_imageview.setColorFilter(resources.getColor(R.color.desc_txt_color), android.graphics.PorterDuff.Mode.SRC_IN)
            car_name_txtview.setTextColor(resources.getColor(R.color.desc_txt_color))
            car_price_txtview.visibility = View.INVISIBLE
            car_cardview.cardElevation = 0f
            card_info_imagebtn.visibility = View.INVISIBLE
        }
        cwidth = car_cardview.width
        Log.e("CWIDTH", cwidth.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance(carType: Int, isSelected: Boolean): CarFragment {
            var arg = Bundle()
            arg.putInt("carType", carType)
            arg.putBoolean("isSelected", isSelected)
            val carFragment = CarFragment()
            carFragment.arguments = arg
            return carFragment
        }

    }


}

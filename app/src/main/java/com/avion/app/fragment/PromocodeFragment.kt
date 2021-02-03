package com.avion.app.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.MainActivity
import com.avion.app.R
import com.avion.app.fragment.viewmodel.PromocodeViewModel
import com.avion.app.unit.NekConfigs
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_promocode.*
import java.util.*

class PromocodeFragment : NekFragment() {

    override fun onCreateViewModel(): NekViewModel {
        promocodeViewMode.nekFragment = this
        return promocodeViewMode
    }

    override fun onStartProgress() {
    }

    override fun onReDoNetworkAction() {
    }

    override fun onStopProgress() {
    }

    private val promocodeViewMode: PromocodeViewModel by lazy { ViewModelProviders.of(this).get(PromocodeViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promocode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        promocodeViewMode.getLivePromo().observe(this, Observer {
            if (it != null) promocode_user_code_txtview.text = it
        })
        copy_promo.setOnClickListener {

            var clipboard: ClipboardManager = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip: ClipData = ClipData.newPlainText("promocode", promocode_user_code_txtview.text.toString())
            clipboard.primaryClip = clip
            Toast.makeText(context!!, getString(R.string.copied), Toast.LENGTH_SHORT).show()
        }

        promocode_share_btn.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Date().time.toString())
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "android_share_promo")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "android_share_promo")
            NekConfigs.analytics = FirebaseAnalytics.getInstance(context!!)
            NekConfigs.analytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Установи приложение для заказа трансферов AVION! Введи промокод " + promocode_user_code_txtview.text.toString() + " и получи 300 бонусных рублей сразу после первой поездки: https://avion.taxi/mobile_application")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        })

        promocode_active_btn.setOnClickListener(View.OnClickListener {
            var promo_input: String = promocode_code_edittext.text.toString().replace(" ", "")
            hideKeyboardFrom(context!!, it)
            if (promo_input.isNotEmpty()) {
                promocodeViewMode.getLiveActivatePromo(promo_input).observe(this, Observer {
                    if (it != null) {
                        promocodeViewMode.getLivePromoBalance().observe(this, Observer {
                            if (it.length > 0) MainActivity.updateBalance(it)
                        })
                        if (it.has("error")) {
                            Toast.makeText(context, it.getString("error"), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Промокод успешно активирован", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        })

    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

package com.avion.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avion.app.R
import com.avion.app.action.Option
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import kotlinx.android.synthetic.main.fragment_choose_language.*

class ChooseLanguageFragment : NekFragment() {
    override fun onReDoNetworkAction() {
    }

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
            mViewModel!!.nekFragment = this
        }
        return mViewModel!!
    }

    override fun onStartProgress() {
    }

    override fun onStopProgress() {
    }

    private var mViewModel: MakeOrderViewModel? = null
    private lateinit var option: Option

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel!!.makeOrderLiveData.observe(this, Observer {
            option = it!!.options!!
            engilsh_chooselanguageview.setCahecked(option.driver_language.english)
            german_chooselanguageview.setCahecked(option.driver_language.german)
            spanish_chooselanguageview.setCahecked(option.driver_language.spain)
        })
        chooselanguage_btn.setOnClickListener {
            option.driver_language.english = engilsh_chooselanguageview.checked
            option.driver_language.german = german_chooselanguageview.checked
            option.driver_language.spain = spanish_chooselanguageview.checked
            mViewModel!!.setOptions(option)
            activity!!.onBackPressed()
        }
    }


}

package com.avion.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.avion.app.R
import com.avion.app.action.Option
import com.avion.app.fragment.viewmodel.MakeOrderViewModel
import com.avion.app.unit.NekFragment
import com.avion.app.unit.NekViewModel
import com.avion.app.unit.OptionView
import kotlinx.android.synthetic.main.fragment_choose_options.*

class ChooseOptionsFragment : NekFragment() {

    override fun onReDoNetworkAction() {
        getOptionPrice()
    }

    override fun onStartProgress() {
        chooseoptions_progressBar.visibility = View.VISIBLE
    }

    override fun onStopProgress() {
        chooseoptions_progressBar.visibility = View.GONE
    }

    override fun onCreateViewModel(): NekViewModel {
        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(activity!!).get(MakeOrderViewModel::class.java)
        }
        return mViewModel!!
    }

    override fun onStart() {
        super.onStart()
        mViewModel!!.nekFragment = this
    }

    private var mViewModel: MakeOrderViewModel? = null
    private lateinit var mOptions: Option

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        chooseoptions_progressBar.visibility = View.GONE

        childrenseat_optionview.setOnCallBack(childerSeetCallBack)
        meetwtable_optionview.setOnCallBack(meetWTableCallBack)
        invoiceforcompany_optionview.setOnCallBack(invoiceforcompanyCallBack)
        carwithyellowplates_optionview.setOnCallBack(carwithyellowplatesCallBack)
        englishspeakingdriver_optionview.setOnCallBack(englishspeakingdriverCallBack)
        smokingsalon_optionview.setOnCallBack(smokingsalonCallBack)
        pettransport_optionview.setOnCallBack(pettransportCallBack)
        oversizedbagged_optionview.setOnCallBack(oversizedbaggedCallBack)
        drivercomments_optionview.setOnCallBack(drivercommentsCallBack)
        lulka_optionview.setOnCallBack(lulkaCallBack)

        drivercomments_optionview.setPrice(0)

        mViewModel!!.makeOrderLiveData.observe(this, Observer {
            val options = it!!.options ?: return@Observer
            mOptions = options

            invoiceforcompany_optionview.setCkecked(options.invoice_for_company)
            carwithyellowplates_optionview.setCkecked(options.car_with_yellow_plates)
            smokingsalon_optionview.setCkecked(options.smoking_salon)
            pettransport_optionview.setCkecked(options.pet_transportation)
            oversizedbagged_optionview.setCkecked(options.oversized_baggage)
        })

        onReDoNetworkAction()

    }

    private fun getOptionPrice() {
        mViewModel!!.getInfoRegion.observe(this, Observer { it ->
            if (it == null)
                return@Observer
            val dopUslugi = it.dopUslugi
            if (!dopUslugi.kreslo.active) childrenseat_optionview.visibility = View.GONE
            if (!dopUslugi.vstrecha.active) meetwtable_optionview.visibility = View.GONE
            if (!dopUslugi.bso.active) invoiceforcompany_optionview.visibility = View.GONE
            if (!dopUslugi.yellowNumbers.active) carwithyellowplates_optionview.visibility = View.GONE
            if (!dopUslugi.language.active) englishspeakingdriver_optionview.visibility = View.GONE
            if (!dopUslugi.smoking.active) smokingsalon_optionview.visibility = View.GONE
            if (!dopUslugi.animal.active) pettransport_optionview.visibility = View.GONE
            if (!dopUslugi.baggage.active) oversizedbagged_optionview.visibility = View.GONE
            if (!dopUslugi.lulka.active) lulka_optionview.visibility = View.GONE

            childrenseat_optionview.setPrice(dopUslugi.kreslo.price)
            meetwtable_optionview.setPrice(dopUslugi.vstrecha.price)
            invoiceforcompany_optionview.setPrice(dopUslugi.bso.price)
            carwithyellowplates_optionview.setPrice(dopUslugi.yellowNumbers.price)
            englishspeakingdriver_optionview.setPrice(dopUslugi.language.price)
            smokingsalon_optionview.setPrice(dopUslugi.smoking.price)
            pettransport_optionview.setPrice(dopUslugi.animal.price)
            oversizedbagged_optionview.setPrice(dopUslugi.baggage.price)
            lulka_optionview.setPrice(dopUslugi.lulka.price)

        })
    }

    private val childerSeetCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {
            Navigation.findNavController(view!!).navigate(R.id.action_chooseOptionsFragment_to_cheldrenSeatsFragment)
        }

        override fun onCkecked(check: Boolean) {

        }
    }
    private val lulkaCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {
            Navigation.findNavController(view!!).navigate(R.id.action_chooseOptionsFragment_to_lulkaFragment)
        }

        override fun onCkecked(check: Boolean) {

        }
    }
    private val meetWTableCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {
            Navigation.findNavController(view!!).navigate(R.id.action_chooseOptionsFragment_to_meetWTableFragment)
        }

        override fun onCkecked(check: Boolean) {

        }
    }

    private val invoiceforcompanyCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {

        }

        override fun onCkecked(check: Boolean) {
            mOptions.invoice_for_company = check
            updateOption()
        }
    }

    private val carwithyellowplatesCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {

        }

        override fun onCkecked(check: Boolean) {
            mOptions.car_with_yellow_plates = check
            updateOption()
        }
    }

    private val englishspeakingdriverCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {
            Navigation.findNavController(view!!).navigate(R.id.action_chooseOptionsFragment_to_chooseLanguageFragment)
        }

        override fun onCkecked(check: Boolean) {

        }
    }

    private val smokingsalonCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {

        }

        override fun onCkecked(check: Boolean) {
            mOptions.smoking_salon = check
            updateOption()
        }
    }

    private val pettransportCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {

        }

        override fun onCkecked(check: Boolean) {
            mOptions.pet_transportation = check
            updateOption()
        }
    }

    private val oversizedbaggedCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {

        }

        override fun onCkecked(check: Boolean) {
            mOptions.oversized_baggage = check
            updateOption()
        }
    }

    private val drivercommentsCallBack: OptionView.OnCallBack = object : OptionView.OnCallBack {
        override fun onNext() {
            Navigation.findNavController(view!!).navigate(R.id.action_chooseOptionsFragment_to_driverCommentFragment)
        }

        override fun onCkecked(check: Boolean) {

        }
    }

    private fun updateOption() {
        mViewModel!!.setOptions(mOptions)
    }

}

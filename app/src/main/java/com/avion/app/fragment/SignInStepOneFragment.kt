package com.avion.app.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.LinkMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import butterknife.ButterKnife
import com.avion.app.MainActivity
import com.avion.app.PhoneCodesMaster
import com.avion.app.R
import com.avion.app.entity.CountryEntity
import com.avion.app.fragment.viewmodel.PromocodeViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.jakewharton.rxbinding.widget.RxTextView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.dlg.*
import kotlinx.android.synthetic.main.fragment_sign_in_step_one.*
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.concurrent.TimeUnit


class SignInStepOneFragment : Fragment() {
    var curr_country: CountryEntity? = null
    private fun showProgress() {
        signin_progressBar.visibility = View.VISIBLE
        signin_next_btn.visibility = View.GONE
    }

    private fun hideProgress() {
        signin_progressBar.visibility = View.GONE
        signin_next_btn.visibility = View.VISIBLE
    }

    private lateinit var signin_hone_editext: EditText
    private lateinit var phone_confirm_title: TextView
    private lateinit var signin_next_btn: Button
    private lateinit var storedVerificationId: String
    private lateinit var sign_in_txt: TextView
    private var phone: String = ""
    private lateinit var dialog: Dialog
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private val resendCodeTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            signin_next_btn.text = getString(R.string.no_code) + " " + (millisUntilFinished / 1000).toString() + " " + getString(R.string.sec)
        }

        override fun onFinish() {
            signin_next_btn.setText(R.string.resend)
            signin_next_btn.setOnClickListener(onResendClick)
            signin_next_btn.isEnabled = true
        }
    }

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (curr_country == null) curr_country = CountryEntity(getString(R.string.Russia), "+7")
        return inflater.inflate(R.layout.fragment_sign_in_step_one, container, false)
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        dialog = Dialog(this.activity!!, android.R.style.Theme_Dialog)
        dialog.setContentView(R.layout.dlg)
        signin_hone_editext = rootView.findViewById(R.id.signin_hone_editext)
        signin_next_btn = rootView.findViewById(R.id.signin_next_btn)
        sign_in_txt = rootView.findViewById(R.id.signin_txt1)
        mAuth = FirebaseAuth.getInstance()
        var density: Float = resources.displayMetrics.density
        var width: Int = Math.round(27 * density)
        var height: Int = Math.round(27 * density)
        var drawable: Drawable = resources.getDrawable(resources.getIdentifier("russia", "drawable", context!!.packageName))
        drawable.setBounds(0, 0, width, height)
        signin_hone_editext.setCompoundDrawables(drawable, null, null, null)
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher = MaskFormatWatcher(mask)
        watcher.installOn(signin_hone_editext)
        signin_next_btn.setOnClickListener(onNextClick)

        MainActivity.countryData.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.code.equals("+7")) {
                    signin_hone_editext.setText("")
                    watcher.installOn(signin_hone_editext)
                    var phm = PhoneCodesMaster(context, this.activity!!.application, PromocodeViewModel(this.activity!!.application))
                    var flag_name = phm.getId(it.name).toLowerCase()

                    var did: Int? = null
                    //var cname:String =
                    try {
                        did = resources.getIdentifier(flag_name, "drawable", context!!.packageName)
                    } catch (e: Exception) {
                        did = null
                    }
                    MainActivity.logstr("did: " + did.toString())
                    if (did == null || did == 0) {
                        did = R.drawable.defflaf
                        width = Math.round(35 * density)
                    } else width = height
                    var drawable: Drawable = resources.getDrawable(did)
                    drawable.setBounds(0, 0, width, height)
                    signin_hone_editext.setCompoundDrawables(drawable, null, null, null)

                    MainActivity.logstr("KEY ACTIVE")
                    var imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    signin_hone_editext.postDelayed(
                            Runnable {
                                signin_hone_editext.requestFocus()
                                imm.showSoftInput(signin_hone_editext, 0)

                            }, 100
                    )

                } else {
                    watcher.removeFromTextView()
                    signin_hone_editext.setText(it.code)
                    signin_hone_editext.setSelection(signin_hone_editext.text.length)

                    //Toast.makeText(context,it.name,Toast.LENGTH_LONG).show()
                    var phm = PhoneCodesMaster(context, this.activity!!.application, PromocodeViewModel(this.activity!!.application))
                    var flag_name = phm.getId(it.name).toLowerCase()

                    var did: Int? = null
                    //var cname:String =
                    try {
                        did = resources.getIdentifier(flag_name, "drawable", context!!.packageName)
                    } catch (e: Exception) {
                        did = null
                    }
                    MainActivity.logstr("did: " + did.toString())
                    if (did == null || did == 0) {
                        did = R.drawable.defflaf
                        width = Math.round(35 * density)
                    } else width = height
                    var drawable: Drawable = resources.getDrawable(did)
                    drawable.setBounds(0, 0, width, height)
                    signin_hone_editext.setCompoundDrawables(drawable, null, null, null)

                    MainActivity.logstr("KEY ACTIVE")
                    var imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    signin_hone_editext.postDelayed(
                            Runnable {
                                signin_hone_editext.requestFocus()
                                imm.showSoftInput(signin_hone_editext, 0)

                            }, 100
                    )


                }
            }
        })




        signin_hone_editext.setOnTouchListener(
                object : View.OnTouchListener {
                    override fun onTouch(v: View, m: MotionEvent): Boolean {
                        if (m.action == MotionEvent.ACTION_UP) {
                            if (m.x <= (signin_hone_editext.compoundDrawables[0].bounds.width() * 2)) {
                                Navigation.findNavController(v).navigate(R.id.action_SignIn_to_chooseCountryFragment)
                                return true
                            }
                        }
                        return false
                    }
                }
        )



        ButterKnife.bind(this, rootView)
        sign_in_txt.movementMethod = LinkMovementMethod.getInstance()
        hideProgress()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(context!!)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser ?: return
        updateUI(currentUser)
    }

    override fun onResume() {
        super.onResume()
        if (resendToken != null)
            resendCodeTimer.start()
    }

    private fun updateUI(currentUser: FirebaseUser) {
        if (currentUser != null) {
            startActivity(Intent(activity!!, MainActivity::class.java))
            activity!!.finish()
        }
    }

    private val onNextClick = View.OnClickListener {
        view
        continueExpDialog()
    }
    private val onResendClick = View.OnClickListener {
        view
        resendExpDialog()
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Logger.d("onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
            hideProgress()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            //Log.w(TAG, "onVerificationFailed", e)
            Logger.d("onVerificationFailed:${e.message}")
            if (e is FirebaseAuthInvalidCredentialsException) {
                signin_hone_editext.error = getString(R.string.wrongnumber)
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }
            hideProgress()
        }

        override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
            changeUIForSmsCode()
            hideProgress()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showProgress()
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.activity!!) { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        this.updateUI(user!!)
                    } else {
                        signin_code_editext.error = getString(R.string.wrongcode)
                        // Sign in failed, display a message and update the UI
                        // Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                    hideProgress()
                }
    }

    private fun continueExpDialog() {
        val title = TextView(this.context!!)
        title.text = getString(R.string.woring)
        title.setBackgroundColor(Color.WHITE)
        title.setTextColor(Color.BLACK)
        title.textSize = (18).toFloat()
        title.setTypeface(null, Typeface.BOLD)
        title.setPadding(10, 15, 15, 5)
        title.gravity = Gravity.CENTER

        dialog.setCanceledOnTouchOutside(true)

        dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.changeBtn.setOnClickListener {
            //signin_hone_editext.text.clear()
            var imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            signin_hone_editext.postDelayed(
                    Runnable {
                        signin_hone_editext.requestFocus()
                        imm.showSoftInput(signin_hone_editext, 0)

                    }, 100
            )
            dialog.hide()
        }
        dialog.sendBtn.setOnClickListener {
            sendCode()
            dialog.hide()
        }
        dialog.show()
        phone_confirm_title = dialog.findViewById(R.id.phone_confirm)
        phone_confirm_title.text = getString(R.string.your_phone_number_is) + "\n" + signin_hone_editext.text.toString() + "\n"

        val alert = AlertDialog.Builder(this.activity!!)

        alert.setCustomTitle(title)
        alert.setMessage(getString(R.string.your_phone_number_is) + "\n" + signin_hone_editext.text.toString())
        alert.setPositiveButton(R.string.change) { _, _ ->
            signin_hone_editext.text.clear()

        }
        alert.setNegativeButton(R.string.acceprt) { _, _ ->
            sendCode()
        }
        //alert.create()
        //  alert.show()
    }

    private fun resendExpDialog() {
        val title = TextView(this.context!!)
        title.text = getString(R.string.woring)
        title.setBackgroundColor(Color.WHITE)
        title.setTextColor(Color.BLACK)
        title.textSize = (18).toFloat()
        title.setTypeface(null, Typeface.BOLD)
        title.setPadding(10, 15, 15, 5)
        title.gravity = Gravity.CENTER

        dialog.setCanceledOnTouchOutside(true)

        dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.changeBtn.setOnClickListener {
            changeUIForChangePhone()
            //signin_hone_editext.text.clear()
            var imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            signin_hone_editext.postDelayed(
                    Runnable {
                        signin_hone_editext.requestFocus()
                        imm.showSoftInput(signin_hone_editext, 0)

                    }, 100
            )
            dialog.hide()
        }
        dialog.sendBtn.setOnClickListener {
            sendCode()
            dialog.hide()
        }
        dialog.show()
        phone_confirm_title = dialog.findViewById(R.id.phone_confirm)
        phone_confirm_title.text = getString(R.string.your_phone_number_is) + "\n" + signin_hone_editext.text.toString() + "\n"

        val alert = AlertDialog.Builder(this.activity!!)

        alert.setCustomTitle(title)
        alert.setMessage(getString(R.string.your_phone_number_is) + "\n" + signin_hone_editext.text.toString())
        alert.setPositiveButton(R.string.change) { _, _ ->
            signin_hone_editext.text.clear()

        }
        alert.setNegativeButton(R.string.acceprt) { _, _ ->
            sendCode()
        }
        //alert.create()
        //  alert.show()
    }

    private fun sendCode() {
        showProgress()
        var phoneNumber = signin_hone_editext.text.toString()
        phone = phoneNumber
        phoneNumber = phoneNumber.replace(" ", "")
        phoneNumber = phoneNumber.replace("(", "")
        phoneNumber = phoneNumber.replace(")", "")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                120,
                TimeUnit.SECONDS,
                this.activity!!,
                callbacks)
    }

    private fun changeUIForSmsCode() {
        signIn_txt.text = getString(R.string.wassent) + " " + phone
        signin_txt1.visibility = View.GONE
        resendCodeTimer.start()
        signin_next_btn.isEnabled = false
        signin_next_btn.setOnClickListener {
            view
            sendCode()
        }
        card2.visibility = View.VISIBLE
        card1.visibility = View.GONE
        signin_code_editext.gravity = Gravity.CENTER
        RxTextView.textChangeEvents(signin_code_editext)
                .filter { e -> e.text().length == 6 }
                .subscribe { e ->
                    val credential = PhoneAuthProvider.getCredential(storedVerificationId, e.text().toString())
                    signInWithPhoneAuthCredential(credential)
                }
    }

    private fun changeUIForChangePhone() {
        signIn_txt.text = getString(R.string.accept_country_code_and_enter_phone_number)
        signin_txt1.visibility = View.VISIBLE
        //resendCodeTimer.start()
        signin_next_btn.isEnabled = true
        signin_next_btn.text = getString(R.string.accept_and_next)
        signin_next_btn.setOnClickListener(onNextClick)
        card2.visibility = View.GONE
        card1.visibility = View.VISIBLE
        // signin_code_editext.gravity = Gravity.CENTER


    }

    override fun onStop() {
        super.onStop()
        resendCodeTimer.cancel()
    }

}

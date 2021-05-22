@file:Suppress("PrivatePropertyName", "unused", "PropertyName", "RemoveExplicitTypeArguments", "RedundantOverride", "UNUSED_VARIABLE", "UnnecessaryVariable", "SimplifyBooleanWithConstants", "SENSELESS_COMPARISON", "SpellCheckingInspection", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_PARAMETER", "UNUSED_ANONYMOUS_PARAMETER",
    "LocalVariableName", "ImplicitNullableNothingType", "ClassName", "RemoveRedundantQualifierName", "SameParameterValue", "UseExpressionBody", "FunctionName"
)
package com.example.myapplication45
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import java.util.concurrent.TimeUnit.*


    class PhoneActivity : AppCompatActivity() {
    private var chrono: Chronometer? = null
    private lateinit var CCP: CountryCodePicker
    private lateinit var verificationCode: String
    private val TAG = "PhoneActivity"
    private lateinit var Mob: EditText
    private var RequestOTP: Button? = null
    private lateinit var mauth: FirebaseAuth
    private lateinit var mUser: FirebaseUser
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        Mob = findViewById<EditText>(R.id.et_mob_no)
        RequestOTP = findViewById<Button>(R.id.btn_request_otp)
        textView1 = findViewById<TextView>(R.id.text_view3)
        textView2 = findViewById<TextView>(R.id.text_view1)
        CCP = findViewById<CountryCodePicker>(R.id.ccp)
        CCP.registerPhoneNumberTextView(Mob)
        mauth = FirebaseAuth.getInstance()
        RequestOTP?.setOnClickListener {
            if ( Mob.text.isNotEmpty()  ) {
                updateUI()
                chrono?.base = SystemClock.elapsedRealtime()
                chrono?.start()
            } else {
                hideKeyboard()
                Toast.makeText(this@PhoneActivity, "Please Type Phone Number", Toast.LENGTH_SHORT).show()

            }


        }


    }



        private fun updateUI() {

        val intent = Intent(this@PhoneActivity, VerifyOTPActivity::class.java)
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("MOB_KEY",CCP.fullNumberWithPlus.replace("",""))
        startActivity(intent)


    }








    private fun hideKeyboard() {

    }


    private fun Intent.putExtra(s: String, mob: EditText) {

    }



}

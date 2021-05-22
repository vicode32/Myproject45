@file:Suppress("unused", "PrivatePropertyName", "RemoveExplicitTypeArguments", "UNUSED_PARAMETER", "UNNECESSARY_NOT_NULL_ASSERTION", "ImplicitThis", "ImplicitThis", "RedundantOverride", "UNUSED_VARIABLE", "USELESS_CAST", "SENSELESS_COMPARISON", "ClassName", "LocalVariableName", "RemoveRedundantQualifierName", "ImplicitThis", "ImplicitThis")


package com.example.myapplication45
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var mauth: FirebaseAuth
    private var verificationId: String? = null
    private val TAG = "VerifyOTPActivity"
    private lateinit var otp: OtpTextView
    private lateinit var progressBar:ProgressBar
    private lateinit var verifyOTP: Button
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var PhoneNumber: String
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var ResendOTP:Button
    private lateinit var Callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_o_t_p)
       if(verificationId == null && savedInstanceState != null){
           onRestoreInstanceState(savedInstanceState)
       }
      PhoneNumber = intent.getStringExtra("MOB_KEY").toString()
        verificationId = String()
        ResendOTP = findViewById(R.id.otp_resend)
       verifyOTP = findViewById<Button>(R.id.btn_verify_otp)
       textView3 = findViewById<TextView>(R.id.text_view3)
       textView4 = findViewById<TextView>(R.id.textView2)
       otp = findViewById<OtpTextView>(R.id.et_otp)
       OTP_Receiver().setOtpTextView(otp.otp)
        progressBar = findViewById<ProgressBar>(R.id.et_progress)
        mauth = FirebaseAuth.getInstance()
       sendVerificationCodeToUser(PhoneNumber)
       requestSMSPermission()
       ResendOTP.setOnClickListener {
       resendOTP(PhoneNumber,resendToken!!)
       progressBar.visibility = View.VISIBLE
   }
          verifyOTP.setOnClickListener {
              val otp: String = otp.otp.toString().trim()
              if (otp.isNotEmpty()) {
                  verifyVerificationCode(otp)
                  progressBar.visibility = View.VISIBLE
              } else{
                  Toast.makeText(this@VerifyOTPActivity,"Please Type Phone Number",Toast.LENGTH_SHORT).show()
              }
          }
   }

    private fun requestSMSPermission() {
        val permission = android.Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this,permission)
        if(grant != PackageManager.PERMISSION_GRANTED) {
            val permission_list = arrayOfNulls<String>(1)
            permission_list[0] = permission
            ActivityCompat.requestPermissions(this,permission_list,1)
        }
    }

    private fun resendOTP(phoneNumber: String, resendToken: PhoneAuthProvider.ForceResendingToken) {
        val options = PhoneAuthOptions.newBuilder(mauth)
                .setPhoneNumber(PhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                       val code: String? = credential.smsCode
                        if(code!=null){
                            otp.otp = code
                        }
                        signInWithPhoneAuthCredential(credential)
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        if (e is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this@VerifyOTPActivity,"Invalid Phone Number",Toast.LENGTH_SHORT).show()
                        } else if (e is FirebaseTooManyRequestsException) {
                            Toast.makeText(this@VerifyOTPActivity,"Too many request try again after some time",Toast.LENGTH_SHORT).show()
                        }
                        updateUserUI()

                    }
                    override fun onCodeSent(verificationId: String,token: PhoneAuthProvider.ForceResendingToken) {
                        Toast.makeText(this@VerifyOTPActivity, "Code sent", Toast.LENGTH_SHORT).show()
                        storedVerificationId = verificationId
                        this@VerifyOTPActivity.resendToken = token
                        progressBar.visibility = View.INVISIBLE

                    }
                }
                )
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }




    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_VERIFICATION_ID,verificationId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID)
    }









    private fun sendVerificationCodeToUser(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mauth)
                .setPhoneNumber(PhoneNumber)
                .setTimeout(60,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        val code: String? = credential.smsCode
                        if(code!=null){
                            otp.otp = code
                        }
                        signInWithPhoneAuthCredential(credential)
                    }


                    override fun onVerificationFailed(e: FirebaseException) {
                        if (e is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this@VerifyOTPActivity, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                        } else if (e is FirebaseTooManyRequestsException) {
                            Toast.makeText(this@VerifyOTPActivity, "Too many request try again after some time", Toast.LENGTH_SHORT).show()
                        }
                        updateUserUI()

                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                        Toast.makeText(this@VerifyOTPActivity, "Code sent", Toast.LENGTH_SHORT).show()
                        storedVerificationId = verificationId
                        resendToken = token
                        progressBar.visibility = View.INVISIBLE
                    }
                }
                )
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun verifyVerificationCode(code: String){
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun updateUserUI() {
        val intent = Intent(this@VerifyOTPActivity,PhoneActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        updateMainUI()
                    } else {
                        Toast.makeText(this@VerifyOTPActivity, "Invalid OTP", Toast.LENGTH_SHORT).show()
                           progressBar.visibility = View.INVISIBLE

                    }
                }


    }

    private fun updateMainUI() {
        val intent = Intent(this@VerifyOTPActivity,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    companion object {
        var otpTextView: OtpTextView? = null
        private const val KEY_VERIFICATION_ID ="key_verification_id"
    }



}

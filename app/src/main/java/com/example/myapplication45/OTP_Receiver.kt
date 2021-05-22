@file:Suppress("ClassName", "unused", "UNUSED_VARIABLE", "RedundantCompanionReference", "UNUSED_PARAMETER")

package com.example.myapplication45

import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi

class OTP_Receiver: BroadcastReceiver() {
    fun setOtpTextView(otpTextView: OtpTextView?){
        VerifyOTPActivity.Companion.otpTextView = otpTextView
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in message) {
            val message = sms.messageBody
        }
    }

    fun setOtpTextView(otpTextView: String?) {
    }

    companion object {
            private var otpTextView: OtpTextView? = null
        }

}

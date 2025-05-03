package tz.co.asoft.overwatch.registration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceivedBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        TODO("Finish implementing this shit please")
    }
}
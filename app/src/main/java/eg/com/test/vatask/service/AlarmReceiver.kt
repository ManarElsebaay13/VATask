package eg.com.test.vatask.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("AlarmReceiver", "Start CalculateService")
        if (intent?.action == "Calculate") {
            val bundle = intent!!.extras
            var intent1 = Intent(context, CalculateService::class.java)
            intent1.putExtras(bundle!!)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(intent1)
            } else {
                context?.startService(intent1)
            }
        }
    }
}
package io.github.delokoseni.clockcloneapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmClock", "Будильник сработал!")
    }
}

package io.github.delokoseni.clockcloneapplication

import android.Manifest
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import java.util.Calendar

class AlarmService : Service() {

    private var player: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {
            ACTION_STOP -> {
                stopAlarm()
                return START_NOT_STICKY
            }

            ACTION_SNOOZE -> {
                stopAlarm()
                val snoozeTime = Calendar.getInstance().apply {
                    add(Calendar.MINUTE, 5)
                }
                AlarmScheduler.scheduleAlarm(
                    this,
                    snoozeTime.get(Calendar.HOUR_OF_DAY),
                    snoozeTime.get(Calendar.MINUTE)
                )
                return START_NOT_STICKY
            }
        }

        startSound()
        startVibration()

        return START_NOT_STICKY
    }

    private fun startSound() {
        if (player != null) return

        player = MediaPlayer.create(this, R.raw.lilpeep_beamerboy)?.apply {
            isLooping = true
            start()
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun startVibration() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(VibratorManager::class.java)
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        val pattern = longArrayOf(0, 700, 400)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createWaveform(pattern, 0)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, 0)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun stopAlarm() {
        player?.stop()
        player?.release()
        player = null

        vibrator?.cancel()
        vibrator = null

        stopSelf()
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onDestroy() {
        player?.release()
        player = null
        vibrator?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_STOP =
            "io.github.delokoseni.clockcloneapplication.STOP_ALARM"

        const val ACTION_SNOOZE =
            "io.github.delokoseni.clockcloneapplication.SNOOZE_ALARM"
    }
}

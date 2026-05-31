package edu.josakapp.proyectoJosakapp.ui.navigation

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import android.media.RingtoneManager
import android.media.AudioAttributes
import edu.josakapp.proyectoJosakapp.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habito = intent.getStringExtra("habito") ?: "Recordatorio"
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habitos_alarm"
        
        // Crear canal de notificación con sonido
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            
            val channel = android.app.NotificationChannel(
                channelId,
                "Alarmas de Hábitos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    audioAttributes
                )
                enableVibration(true)
                setVibrationPattern(longArrayOf(0, 500, 250, 500))
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        // Crear notificación con sonido y vibración
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.fire)
            .setContentTitle("Recordatorio: $habito")
            .setContentText("¡Es hora de completar tu hábito!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .setAutoCancel(true)
        
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

package edu.josakapp.proyectoJosakapp.ui.navigation

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import edu.josakapp.proyectoJosakapp.R
import java.util.Calendar

/**
 * BroadcastReceiver que se activa cuando la alarma programada se dispara.
 * Muestra una notificación local y reprograma la alarma para el día siguiente.
 */
class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "josak_reminder_channel"
        const val NOTIFICATION_ID = 2001
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("REMINDER", " Alarma de recordatorio recibida a las ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}")

        // Crear canal de notificación (necesario para Android 8+)
        createNotificationChannel(context)

        // Mostrar notificación local
        val prefs = context.getSharedPreferences("recordatorio_prefs", Context.MODE_PRIVATE)
        val movilEnabled = prefs.getBoolean("movil_enabled", true)

        if (movilEnabled) {
            showNotification(context)
        }

        // Reprogramar para mañana a la misma hora (ya que usamos setExact en lugar de setRepeating)
        val horaGuardada = prefs.getString("hora_recordatorio", "08:30") ?: "08:30"
        scheduleNextReminder(context, horaGuardada)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios JosakApp",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios diarios para completar hábitos"
                enableVibration(true)
                enableLights(true)
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("⏰ JosakApp - Recordatorio")
            .setContentText("¡Es hora de completar tus hábitos! 🐧")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("¡No olvides completar tus hábitos de hoy! Mantén tu racha y sigue subiendo de nivel 🚀")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
        Log.d("REMINDER", " Notificación mostrada correctamente")
    }

    /**
     * Reprograma la alarma para mañana a la misma hora.
     * Se llama después de cada notificación para mantener la cadena diaria.
     */
    private fun scheduleNextReminder(context: Context, time: String) {
        val parts = time.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 30

        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1) // Siempre mañana
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val intentAlarm = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intentAlarm,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    // Fallback: alarma inexacta si no tiene permiso
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("REMINDER", " Próxima alarma reprogramada para mañana a las $time")
        } catch (e: Exception) {
            Log.e("REMINDER", "Error reprogramando alarma: ${e.message}")
        }
    }
}

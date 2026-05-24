package edu.josakapp.proyectoJosakapp.ui.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.josakapp.proyectoJosakapp.ui.components.SettingsScaffold
import edu.josakapp.proyectoJosakapp.ui.navigation.ReminderReceiver
import java.util.Calendar

@Composable
fun RecordatorioScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("recordatorio_prefs", Context.MODE_PRIVATE)

    // Cargar estado guardado de SharedPreferences
    var recordatorioMovil by remember { mutableStateOf(prefs.getBoolean("movil_enabled", true)) }
    var horaRecordatorio by remember {
        mutableStateOf(prefs.getString("hora_recordatorio", "08:30") ?: "08:30")
    }

    val colorCeleste = Color(0xFF03A9F4)

    SettingsScaffold(title = "RECORDATORIOS", onBackClick = onBack) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // --- SECCIÓN: Activar/Desactivar ---
            Text(
                text = "Notificación de recordatorio",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                // Opción Teléfono (Notificación Push)
                ListItem(
                    headlineContent = { Text("Notificación en el móvil", fontWeight = FontWeight.Bold) },
                    supportingContent = {
                        Text(
                            "Recibirás una notificación push a la hora que elijas",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = colorCeleste
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = recordatorioMovil,
                            onCheckedChange = {
                                recordatorioMovil = it
                                prefs.edit().putBoolean("movil_enabled", it).apply()
                                if (it) {
                                    scheduleReminder(context, horaRecordatorio)
                                    Toast.makeText(
                                        context,
                                        "Recordatorio activado a las $horaRecordatorio",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    cancelReminder(context)
                                    Toast.makeText(
                                        context,
                                        "Recordatorio desactivado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = colorCeleste
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN: Hora de recordatorio ---
            Text(
                text = "Hora de recordatorio diaria",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Abrir TimePickerDialog
                        val parts = horaRecordatorio.split(":")
                        val currentHour = parts.getOrNull(0)?.toIntOrNull() ?: 8
                        val currentMinute = parts.getOrNull(1)?.toIntOrNull() ?: 30

                        TimePickerDialog(
                            context,
                            { _, selectedHour, selectedMinute ->
                                val newTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                                horaRecordatorio = newTime

                                // Guardar en SharedPreferences
                                prefs.edit().putString("hora_recordatorio", newTime).apply()

                                // Reprogramar la alarma si está activada
                                if (recordatorioMovil) {
                                    scheduleReminder(context, newTime)
                                    Toast.makeText(
                                        context,
                                        "Recordatorio programado para las $newTime",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            currentHour,
                            currentMinute,
                            true // Formato 24h
                        ).show()
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Alarm,
                            contentDescription = null,
                            tint = colorCeleste,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text("Hora establecida", fontWeight = FontWeight.Bold)
                            Text(
                                "Toca para cambiar",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Text(
                        text = horaRecordatorio,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorCeleste
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Información adicional ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorCeleste.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = "💡 Los recordatorios te ayudan a mantener tu racha diaria. " +
                            "Configura una hora que se adapte a tu rutina y no olvides tus hábitos.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

/**
 * Programa una alarma exacta usando AlarmManager.
 * Usa setExactAndAllowWhileIdle() que funciona incluso en Doze mode (Android 6+).
 * Para Android 12+ verifica el permiso de alarmas exactas.
 */
private fun scheduleReminder(context: Context, time: String) {
    val parts = time.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 8
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 30

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        // Si la hora ya pasó hoy, programar para mañana
        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ necesita verificar permiso de alarmas exactas
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Log.d("REMINDER", "✅ Alarma EXACTA programada para $time (millis=${calendar.timeInMillis})")
            } else {
                // No tiene permiso, pedir que lo active en ajustes
                Toast.makeText(
                    context,
                    "Activa los permisos de alarmas exactas en Ajustes",
                    Toast.LENGTH_LONG
                ).show()
                try {
                    val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(settingsIntent)
                } catch (e: Exception) {
                    Log.e("REMINDER", "No se pudo abrir ajustes de alarmas: ${e.message}")
                }
                // Fallback: alarma no exacta
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Log.d("REMINDER", "⚠️ Alarma NO-EXACTA programada (sin permiso exacto)")
            }
        } else {
            // Android 6-11: setExactAndAllowWhileIdle funciona directamente
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("REMINDER", "✅ Alarma EXACTA programada para $time")
        }
    } catch (e: Exception) {
        Log.e("REMINDER", "❌ Error al programar alarma: ${e.message}")
        // Último fallback: alarma simple
        try {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d("REMINDER", "⚠️ Alarma simple programada como fallback")
        } catch (e2: Exception) {
            Log.e("REMINDER", "❌ Error total al programar alarma: ${e2.message}")
        }
    }
}

/**
 * Cancela la alarma de recordatorio previamente programada.
 */
private fun cancelReminder(context: Context) {
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    Log.d("REMINDER", "🚫 Alarma de recordatorio cancelada")
}
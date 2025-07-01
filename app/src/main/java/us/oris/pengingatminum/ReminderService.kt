package us.oris.pengingatminum

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ReminderService : Service() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val db = FirebaseFirestore.getInstance()

        val channelId = "reminder_channel"
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Pengingat Minum Air", NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("⏰ Saatnya Minum Air!")
            .setContentText("Yuk, minum air sekarang biar sehat 💧")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setSound(alarmSound)
            .build()

        startForeground(1, notification)

        // Simpan log minum ke Firestore
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val docRef = db.collection("logs").document(date)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val current = snapshot.getLong("count") ?: 0
            transaction.set(docRef, mapOf("count" to current + 1))
        }.addOnSuccessListener {
            Log.d("ReminderService", "Berhasil update log")
        }.addOnFailureListener {
            Log.e("ReminderService", "Gagal update log", it)
        }

        stopForeground(true)
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

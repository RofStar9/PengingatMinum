package us.oris.pengingatminum

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var spinner: Spinner
    private lateinit var btnSetReminder: Button
    private lateinit var btnStopReminder: Button
    private lateinit var tvStatistik: TextView
    private lateinit var btnTambahMinum: Button
    private lateinit var prefs: SharedPreferences

    private val items = arrayOf("1 Menit (Tes)", "30 Menit", "1 Jam", "2 Jam", "4 Jam")
    private val values = arrayOf(1, 30, 60, 120, 240)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("statistik", Context.MODE_PRIVATE)

        tvStatus = findViewById(R.id.tvStatus)
        spinner = findViewById(R.id.spinnerInterval)
        btnSetReminder = findViewById(R.id.btnSetReminder)
        btnStopReminder = findViewById(R.id.btnStopReminder)
        tvStatistik = findViewById(R.id.tvStatistik)
        btnTambahMinum = findViewById(R.id.btnTambahMinum)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        btnSetReminder.setOnClickListener {
            val pos = spinner.selectedItemPosition
            val menit = values[pos]
            setReminder(menit)
            tvStatus.text = "Pengingat setiap ${items[pos]} aktif"
        }

        btnStopReminder.setOnClickListener {
            stopReminder()
            tvStatus.text = "Pengingat dimatikan"
        }

        btnTambahMinum.setOnClickListener {
            tambahJumlahMinum()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            tampilkanStatistikHariIni()
        }, 500)
    }

    private fun setReminder(menit: Int) {
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            action = "REMINDER_ACTION"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intervalMillis = menit * 60 * 1000L
        val triggerTime = System.currentTimeMillis() + intervalMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            intervalMillis,
            pendingIntent
        )
    }

    private fun stopReminder() {
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun getTodayKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun tampilkanStatistikHariIni() {
        val count = prefs.getInt(getTodayKey(), 0)
        tvStatistik.text = "Hari ini kamu sudah minum $count kali 💧"
    }

    private fun tambahJumlahMinum() {
        val key = getTodayKey()
        val current = prefs.getInt(key, 0)
        prefs.edit().putInt(key, current + 1).apply()
        tampilkanStatistikHariIni()
        Toast.makeText(this, "Dicatat: sudah minum 💧", Toast.LENGTH_SHORT).show()
    }
}

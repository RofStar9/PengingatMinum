// File: SudahMinumReceiver.kt
package us.oris.pengingatminum

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class SudahMinumReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        FirebaseApp.initializeApp(context)
        val db = FirebaseFirestore.getInstance()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val logRef = db.collection("logs").document(today)
        logRef.get().addOnSuccessListener {
            val current = it.getLong("count") ?: 0
            logRef.set(mapOf("count" to (current + 1)))
        }

        Toast.makeText(context, "👍 Dicatat: Sudah minum", Toast.LENGTH_SHORT).show()
    }
}

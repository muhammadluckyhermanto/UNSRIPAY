package com.example.unsripay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConfirmTopUpActivity : AppCompatActivity() {

    private lateinit var paymentAmountText: TextView
    private lateinit var confirmButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_top_up)

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("unsripay").child("transaksi")

        // Inisialisasi Views
        paymentAmountText = findViewById(R.id.payment_amount_value)
        confirmButton = findViewById(R.id.confirm_button)

        // Ambil data dari Intent
        val nominal = intent.getIntExtra("nominal", 0)
        val metode = intent.getStringExtra("metode") ?: "TOP UP"

        paymentAmountText.text = "Rp$nominal"

        // Listener untuk Tombol Konfirmasi
        confirmButton.setOnClickListener {
            // Ambil ID User dari SharedPreferences
            val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", -1)

            if (userId == -1) {
                Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil waktu saat ini untuk timestamp
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            // Buat data transaksi
            val newTransaction = hashMapOf(
                "iduser" to userId,        // ID user yang login
                "metode" to metode,        // Metode pembayaran
                "nominal" to nominal,      // Nominal top-up
                "nomortujuan" to "$userId", // Isi nomor tujuan dengan ID user
                "timestamp" to timestamp   // Waktu transaksi
            )

            // Push data transaksi ke Firebase
            database.push().setValue(newTransaction).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Ambil saldo lama dari pengguna
                    val userRef = FirebaseDatabase.getInstance().getReference("unsripay/user")
                    userRef.child(userId.toString()).get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val userName = snapshot.child("nama").value.toString()
                            val oldBalance = snapshot.child("saldo").value.toString().toInt()

                            // Hitung saldo baru setelah top-up
                            val newBalance = oldBalance + nominal

                            // Update saldo pengguna di Firebase
                            userRef.child(userId.toString()).child("saldo").setValue(newBalance).addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    // Kirim saldo terbaru dan nama pengguna ke MainActivity
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra("userSaldo", newBalance)
                                    intent.putExtra("userName", userName)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish() // Menutup halaman Confirm Top Up
                                } else {
                                    Toast.makeText(this, "Gagal memperbarui saldo: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Terjadi kesalahan saat mengakses data pengguna", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Gagal menambahkan transaksi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

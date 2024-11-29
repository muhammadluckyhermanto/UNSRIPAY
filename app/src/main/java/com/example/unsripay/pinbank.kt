package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class pinbank : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var transaksiRef: DatabaseReference
    private lateinit var view01: View
    private lateinit var view02: View
    private lateinit var view03: View
    private lateinit var view04: View
    private lateinit var view05: View
    private lateinit var view06: View

    private val numbersList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        // Membuat status bar transparan
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true // Teks status bar terang


        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("unsripay/user")
        transaksiRef = database.getReference("unsripay/transaksi")

        initializeComponents()

        val destinationNumber = intent.getStringExtra("destinationNumber")
        val amount = intent.getIntExtra("amount", 0)
        val selectedBank = intent.getStringExtra("selectedBank")

        // Ambil userId dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Data pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Tombol OK untuk validasi PIN
        findViewById<View>(R.id.btn_Ok).setOnClickListener {
            if (numbersList.size == 6) {
                val enteredPin = numbersList.joinToString("")
                validatePin(userId, enteredPin, destinationNumber ?: "", amount, selectedBank ?: "")
            } else {
                Toast.makeText(this, "Masukkan 6 digit PIN", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeComponents() {
        findViewById<View>(R.id.btn_01).setOnClickListener(this)
        findViewById<View>(R.id.btn_02).setOnClickListener(this)
        findViewById<View>(R.id.btn_03).setOnClickListener(this)
        findViewById<View>(R.id.btn_04).setOnClickListener(this)
        findViewById<View>(R.id.btn_05).setOnClickListener(this)
        findViewById<View>(R.id.btn_06).setOnClickListener(this)
        findViewById<View>(R.id.btn_07).setOnClickListener(this)
        findViewById<View>(R.id.btn_08).setOnClickListener(this)
        findViewById<View>(R.id.btn_09).setOnClickListener(this)
        findViewById<View>(R.id.btn_00).setOnClickListener(this)
        findViewById<View>(R.id.btn_Hapus).setOnClickListener(this)

        // Inisialisasi tampilan untuk kolom PIN
        view01 = findViewById(R.id.view_01)
        view02 = findViewById(R.id.view_02)
        view03 = findViewById(R.id.view_03)
        view04 = findViewById(R.id.view_04)
        view05 = findViewById(R.id.view_05)
        view06 = findViewById(R.id.view_06)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_01 -> handleNumberClick("1")
            R.id.btn_02 -> handleNumberClick("2")
            R.id.btn_03 -> handleNumberClick("3")
            R.id.btn_04 -> handleNumberClick("4")
            R.id.btn_05 -> handleNumberClick("5")
            R.id.btn_06 -> handleNumberClick("6")
            R.id.btn_07 -> handleNumberClick("7")
            R.id.btn_08 -> handleNumberClick("8")
            R.id.btn_09 -> handleNumberClick("9")
            R.id.btn_00 -> handleNumberClick("0")
            R.id.btn_Hapus -> handleDeleteClick()
        }
    }

    private fun handleNumberClick(number: String) {
        if (numbersList.size < 6) {
            numbersList.add(number)
            updatePassCodeView() // Memperbarui tampilan kolom PIN
        }
    }

    private fun handleDeleteClick() {
        if (numbersList.isNotEmpty()) {
            numbersList.removeAt(numbersList.size - 1)
            updatePassCodeView() // Memperbarui tampilan kolom PIN
        }
    }

    private fun updatePassCodeView() {
        val dots = listOf(view01, view02, view03, view04, view05, view06)
        for (i in dots.indices) {
            if (i < numbersList.size) {
                dots[i].setBackgroundResource(R.drawable.bg_view_blue_oval) // Warna saat PIN dimasukkan
            } else {
                dots[i].setBackgroundResource(R.drawable.bg_view_grey_oval) // Warna untuk kolom kosong
            }
        }
    }

    private fun validatePin(userId: Int, enteredPin: String, destinationNumber: String, amount: Int, selectedBank: String) {
        userRef.child(userId.toString()).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val storedPin = snapshot.child("pin").value.toString()
                val currentBalance = snapshot.child("saldo").value.toString().toInt()

                if (enteredPin == storedPin) {
                    if (currentBalance >= amount) {
                        // Proses transfer
                        val newBalance = currentBalance - amount
                        userRef.child(userId.toString()).child("saldo").setValue(newBalance)

                        // Tambahkan riwayat transaksi
                        val transaksiId = transaksiRef.push().key
                        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                        val transaksiData = mapOf(
                            "iduser" to userId,
                            "metode" to selectedBank,
                            "nomortujuan" to destinationNumber,
                            "nominal" to amount,
                            "timestamp" to timestamp
                        )
                        transaksiId?.let {
                            transaksiRef.child(it).setValue(transaksiData).addOnSuccessListener {
                                // Setelah berhasil menyimpan transaksi, navigasi ke halaman detail transaksi
                                val intent = Intent(this, detailtransaksi::class.java)
                                intent.putExtra("transaksiId", transaksiId)
                                startActivity(intent)
                                finish() // Menutup activity transaksi
                            }
                        }

                        Toast.makeText(this, "Transfer berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Saldo tidak cukup!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "PIN salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Terjadi kesalahan saat mengakses database.", Toast.LENGTH_SHORT).show()
        }
    }
}
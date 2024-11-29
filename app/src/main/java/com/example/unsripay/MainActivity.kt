package com.example.unsripay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.unsripay.utils.CurrencyUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var listView: ListView
    private lateinit var sharedPreferences: SharedPreferences
    private val transactionsList = mutableListOf<Transaction>()
    private val filteredTransactions = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        // Membuat status bar transparan
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true // Teks status bar terang

        // Menangani window insets untuk tampilan edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mendapatkan referensi TextView untuk nama dan saldo
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val saldoTextView: TextView = findViewById(R.id.saldoTextView)

        // Mengamati perubahan data di ViewModel
        mainViewModel.userName.observe(this) { userName ->
            nameTextView.text = userName
        }

        mainViewModel.userSaldo.observe(this) { userSaldo ->
            saldoTextView.text = CurrencyUtils.formatRupiah(userSaldo)
        }

        // Mengambil data dari Intent atau sumber lain
        val userName = intent.getStringExtra("userName") ?: "Nama tidak ditemukan"
        val userSaldo = intent.getIntExtra("userSaldo", 0)

        // Memperbarui data pada ViewModel
        mainViewModel.updateUserData(userName, userSaldo)

        // Animasi tombol
        val scaleDown = ScaleAnimation(
            1.0f, 0.9f,  // Start and end scale for X axis
            1.0f, 0.9f,  // Start and end scale for Y axis
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point X axis
            Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point Y axis
        ).apply {
            duration = 100 // Durasi animasi dalam milidetik
        }

        val scaleUp = ScaleAnimation(
            0.9f, 1.0f,
            0.9f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 100
        }

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Ambil ID user dari SharedPreferences
        val userId = sharedPreferences.getInt("userId", -1)

        // Periksa apakah userId valid
        if (userId != -1) {
            loadTransactions(userId)
        }

        // Fungsi untuk mengatur animasi pada tombol dan berpindah aktivitas
        fun setButtonAnimation(button: View, targetActivity: Class<*>) {
            button.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.clearAnimation()
                        v.startAnimation(scaleDown)
                    }
                    MotionEvent.ACTION_UP -> {
                        v.clearAnimation()
                        v.startAnimation(scaleUp)
                        v.postDelayed({
                            v.setAnimation(null)
                            val intent = Intent(this, targetActivity)
                            startActivity(intent)
                        }, 100)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        v.clearAnimation()
                        v.startAnimation(scaleUp)
                        v.setAnimation(null)
                    }
                }
                true
            }
        }

        // Menambahkan animasi dan navigasi pada tombol
        setButtonAnimation(findViewById(R.id.button1), TransferActivity::class.java)
        setButtonAnimation(findViewById(R.id.button2), Topup::class.java)
        setButtonAnimation(findViewById(R.id.button3), ScanQris::class.java)
        setButtonAnimation(findViewById(R.id.button4), RiwayatActivity::class.java)
        setButtonAnimation(findViewById(R.id.button_payment), PembayaranActivity::class.java)
        setButtonAnimation(findViewById(R.id.transaksi), RiwayatActivity::class.java)
        setButtonAnimation(findViewById(R.id.button4gg), suliet::class.java)
        setButtonAnimation(findViewById(R.id.button3gg), ukt::class.java)
    }

    private fun loadTransactions(userId: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("unsripay/transaksi")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionsList.clear()
                for (data in snapshot.children) {
                    val transaction = data.getValue(Transaction::class.java)
                    transaction?.let {
                        if (it.iduser == userId) {
                            it.transaksiId = data.key ?: ""
                            transactionsList.add(it)
                        }
                    }
                }

                // Urutkan transaksi dari yang terbaru
                transactionsList.sortByDescending { it.timestamp }

                // Default tampilkan semua transaksi
                filteredTransactions.clear()
                filteredTransactions.addAll(transactionsList)
                updateListView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateListView() {
        val adapter = TransactionAdapter(this, filteredTransactions)
        listView.adapter = adapter

        // Klik item untuk melihat detail transaksi
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTransaction = filteredTransactions[position]
            val intent = Intent(this, detailtransaksi::class.java)
            intent.putExtra("transaksiId", selectedTransaction.transaksiId)
            intent.putExtra("metode", selectedTransaction.metode)
            intent.putExtra("nomorTujuan", selectedTransaction.nomortujuan)
            intent.putExtra("nominal", selectedTransaction.nominal)
            intent.putExtra("timestamp", selectedTransaction.timestamp)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Memperbarui data saat kembali ke aktivitas
        val userName = intent.getStringExtra("userName") ?: "Nama tidak ditemukan"
        val userSaldo = intent.getIntExtra("userSaldo", 0)
        mainViewModel.updateUserData(userName, userSaldo)
    }
}


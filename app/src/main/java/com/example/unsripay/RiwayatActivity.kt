package com.example.unsripay

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class RiwayatActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var listView: ListView
    private lateinit var filterButton: Button
    private lateinit var clearFilterButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val transactionsList = mutableListOf<Transaction>()
    private val filteredTransactions = mutableListOf<Transaction>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.riwayat)

        listView = findViewById(R.id.listView)
        filterButton = findViewById(R.id.btn_filter)
        clearFilterButton = findViewById(R.id.btn_clear_filter)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        // Ambil ID user dari SharedPreferences
        val userId = sharedPreferences.getInt("userId", -1)

        // Periksa apakah userId valid
        if (userId != -1) {
            loadTransactions(userId)
        }

        // Tombol filter untuk memilih tanggal
        filterButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                filterTransactionsByDate(selectedDate)
            }
        }

        // Tombol hapus filter
        clearFilterButton.setOnClickListener {
            clearFilter()
        }

        // Klik item untuk melihat detail transaksi
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedTransaction = filteredTransactions[position]

            val intent = Intent(this@RiwayatActivity, detailtransaksi::class.java)
            intent.putExtra("transaksiId", selectedTransaction.transaksiId)
            intent.putExtra("metode", selectedTransaction.metode)
            intent.putExtra("nomorTujuan", selectedTransaction.nomortujuan)
            intent.putExtra("nominal", selectedTransaction.nominal)
            intent.putExtra("timestamp", selectedTransaction.timestamp)
            startActivity(intent)
        }
    }

    private fun loadTransactions(userId: Int) {
        databaseReference = FirebaseDatabase.getInstance().getReference("unsripay/transaksi")

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
                Toast.makeText(this@RiwayatActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateListView() {
        val adapter = TransactionAdapter(this, filteredTransactions)
        listView.adapter = adapter
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
                onDateSelected(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun filterTransactionsByDate(selectedDate: String) {
        filteredTransactions.clear()
        filteredTransactions.addAll(
            transactionsList.filter { it.timestamp.startsWith(selectedDate) }
                .sortedByDescending { it.timestamp }
        )
        updateListView()

        if (filteredTransactions.isEmpty()) {
            Toast.makeText(this, "Tidak ada transaksi pada tanggal $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFilter() {
        // Kembalikan transaksi yang difilter ke daftar penuh
        filteredTransactions.clear()
        filteredTransactions.addAll(transactionsList)
        updateListView()

        Toast.makeText(this, "Filter dihapus, menampilkan semua transaksi", Toast.LENGTH_SHORT).show()
    }
}

data class Transaction(
    var transaksiId: String = "",
    val iduser: Int = 0,
    val metode: String = "",
    val nominal: Int = 0,
    val nomortujuan: String = "",
    val timestamp: String = ""
)

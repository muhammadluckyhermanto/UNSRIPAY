package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class yudisium : AppCompatActivity() {

    private lateinit var nimInput: EditText
    private lateinit var btnLanjut: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yudisium) // Ganti dengan layout yang sesuai jika berbeda

        nimInput = findViewById(R.id.nim_input)
        btnLanjut = findViewById(R.id.btn_lanjut)

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        btnLanjut.setOnClickListener {
            val nim = nimInput.text.toString()
            if (nim.isEmpty()) {
                Toast.makeText(this, "Masukkan NIM terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                fetchDataAndNavigate(nim)
            }
        }
    }

    private fun fetchDataAndNavigate(nim: String) {
        // Akses database Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("unsripay/yudisium")

        // Cari data berdasarkan NIM
        databaseReference.orderByChild("nim").equalTo(nim).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val yudisiumData = data.getValue(YudisiumData::class.java)
                        if (yudisiumData != null) {
                            // Kirim data ke halaman konfirmasi
                            val intent = Intent(this@yudisium, confirmyudisium::class.java)
                            intent.putExtra("nim", yudisiumData.nim)
                            intent.putExtra("nama", yudisiumData.nama)
                            intent.putExtra("gelar", yudisiumData.gelar)
                            intent.putExtra("tanggal", yudisiumData.tanggal)
                            intent.putExtra("tempat", yudisiumData.tempat)
                            intent.putExtra("nominal", yudisiumData.nominal)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this@yudisium, "Data tidak ditemukan untuk NIM: $nim", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@yudisium, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

// Model Data Yudisium
data class YudisiumData(
    val nim: String = "",
    val nama: String = "",
    val gelar: String = "",
    val tanggal: String = "",
    val tempat: String = "",
    val nominal: Int = 0
)

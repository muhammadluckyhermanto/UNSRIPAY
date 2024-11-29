package com.example.unsripay


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ukt : AppCompatActivity() {

    private lateinit var nimInput: EditText
    private lateinit var btnLanjut: Button
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ukt)

        // Fungsi untuk tombol kembali
        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini
        }

        nimInput = findViewById(R.id.nim_input)
        btnLanjut = findViewById(R.id.btn_lanjut)

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
        databaseReference = FirebaseDatabase.getInstance().getReference("unsripay/ukt")

        // Cari data berdasarkan NIM
        databaseReference.orderByChild("nim").equalTo(nim).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val uktData = data.getValue(UktData::class.java)
                        if (uktData != null) {
                            // Kirim data ke halaman berikutnya
                            val intent = Intent(this@ukt, confirmukt::class.java)
                            intent.putExtra("nim", uktData.nim)
                            intent.putExtra("nama", uktData.nama)
                            intent.putExtra("semester", uktData.semester)
                            intent.putExtra("nominal", uktData.nominal)
                            intent.putExtra("selectedBank", "UKT")
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this@ukt, "Data tidak ditemukan untuk NIM: $nim", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ukt, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

// Model Data UKT
data class UktData(
    val nim: String = "",
    val nama: String = "",
    val semester: Int = 0,
    val nominal: Int = 0
)

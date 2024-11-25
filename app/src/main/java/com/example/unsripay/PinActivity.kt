package com.example.unsripay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class PinActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var view01: View
    private lateinit var view02: View
    private lateinit var view03: View
    private lateinit var view04: View
    private lateinit var view05: View
    private lateinit var view06: View
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private val numbersList = ArrayList<String>()

    data class User(
        val iduser: Int = 0,
        val nama: String = "",
        val pin: Int = 0,
        val saldo: Int = 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("unsripay/user")

        initializeComponents()
    }

    private fun initializeComponents() {
        view01 = findViewById(R.id.view_01)
        view02 = findViewById(R.id.view_02)
        view03 = findViewById(R.id.view_03)
        view04 = findViewById(R.id.view_04)
        view05 = findViewById(R.id.view_05)
        view06 = findViewById(R.id.view_06)

        // Setup button listeners
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
        findViewById<View>(R.id.btn_Ok).setOnClickListener(this)
        findViewById<View>(R.id.btn_Hapus).setOnClickListener(this)
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
            R.id.btn_Ok -> {
                if (numbersList.size == 6) {
                    val pin = numbersList.joinToString("")
                    validatePinAndLogin(pin)
                } else {
                    Toast.makeText(this, "Masukkan 6 digit PIN", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_Hapus -> handleDeleteClick()
        }
    }

    private fun handleNumberClick(number: String) {
        if (numbersList.size < 6) {
            numbersList.add(number)
            updatePassCodeView()
        }
    }

    private fun handleDeleteClick() {
        if (numbersList.isNotEmpty()) {
            numbersList.removeAt(numbersList.size - 1)
            updatePassCodeView()
        }
    }

    private fun updatePassCodeView() {
        val dots = listOf(view01, view02, view03, view04, view05, view06)
        for (i in dots.indices) {
            if (i < numbersList.size) {
                dots[i].setBackgroundResource(R.drawable.bg_view_blue_oval)
            } else {
                dots[i].setBackgroundResource(R.drawable.bg_view_grey_oval)
            }
        }
    }

    private fun validatePinAndLogin(pin: String) {
        userRef.orderByChild("pin").equalTo(pin.toDouble()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.children.first().getValue(User::class.java)
                    user?.let {
                        // Simpan userId ke SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("userId", it.iduser)
                        editor.apply()

                        // Pindah ke halaman utama
                        val intent = Intent(this@PinActivity, MainActivity::class.java)
                        intent.putExtra("userName", it.nama)
                        intent.putExtra("userSaldo", it.saldo)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@PinActivity, "PIN salah!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PinActivity, "Terjadi kesalahan, coba lagi.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

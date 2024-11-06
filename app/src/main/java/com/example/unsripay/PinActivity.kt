package com.example.unsripay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.Toast

class PinActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var view01: View
    private lateinit var view02: View
    private lateinit var view03: View
    private lateinit var view04: View
    private lateinit var view05: View
    private lateinit var view06: View
    private lateinit var btn01: Button
    private lateinit var btn02: Button
    private lateinit var btn03: Button
    private lateinit var btn04: Button
    private lateinit var btn05: Button
    private lateinit var btn06: Button
    private lateinit var btn07: Button
    private lateinit var btn08: Button
    private lateinit var btn09: Button
    private lateinit var btn00: Button
    private lateinit var btnOk: Button
    private lateinit var btnHapus: Button

    private val numbersList = ArrayList<String>()
    private var passCode = ""
    private var num01: String = ""
    private var num02: String = ""
    private var num03: String = ""
    private var num04: String = ""
    private var num05: String = ""
    private var num06: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        // Fullscreen layout (background sampai ke status bar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Pastikan status bar transparan
        window.statusBarColor = Color.TRANSPARENT
        initializeComponents()
    }


    private fun initializeComponents() {
        view01 = findViewById(R.id.view_01)
        view02 = findViewById(R.id.view_02)
        view03 = findViewById(R.id.view_03)
        view04 = findViewById(R.id.view_04)
        view05 = findViewById(R.id.view_05)
        view06 = findViewById(R.id.view_06)

        btn01 = findViewById(R.id.btn_01)
        btn02 = findViewById(R.id.btn_02)
        btn03 = findViewById(R.id.btn_03)
        btn04 = findViewById(R.id.btn_04)
        btn05 = findViewById(R.id.btn_05)
        btn06 = findViewById(R.id.btn_06)
        btn07 = findViewById(R.id.btn_07)
        btn08 = findViewById(R.id.btn_08)
        btn09 = findViewById(R.id.btn_09)
        btn00 = findViewById(R.id.btn_00)
        btnOk = findViewById(R.id.btn_Ok)
        btnHapus = findViewById(R.id.btn_Hapus)

        btn01.setOnClickListener(this)
        btn02.setOnClickListener(this)
        btn03.setOnClickListener(this)
        btn04.setOnClickListener(this)
        btn05.setOnClickListener(this)
        btn06.setOnClickListener(this)
        btn07.setOnClickListener(this)
        btn08.setOnClickListener(this)
        btn09.setOnClickListener(this)
        btn00.setOnClickListener(this)
        btnOk.setOnClickListener(this)
        btnHapus.setOnClickListener(this)
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
                // Pastikan bahwa PIN yang dimasukkan sudah lengkap (6 digit)
                if (numbersList.size == 6) {
                    // Lanjut ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Untuk menutup activity PIN saat berpindah
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
        when (numbersList.size) {
            0 -> resetPassCodeView()
            1 -> view01.setBackgroundResource(R.drawable.bg_view_blue_oval)
            2 -> view02.setBackgroundResource(R.drawable.bg_view_blue_oval)
            3 -> view03.setBackgroundResource(R.drawable.bg_view_blue_oval)
            4 -> view04.setBackgroundResource(R.drawable.bg_view_blue_oval)
            5 -> view05.setBackgroundResource(R.drawable.bg_view_blue_oval)
            6 -> view06.setBackgroundResource(R.drawable.bg_view_blue_oval)
        }
    }

    private fun resetPassCodeView() {
        view01.setBackgroundResource(R.drawable.bg_view_grey_oval)
        view02.setBackgroundResource(R.drawable.bg_view_grey_oval)
        view03.setBackgroundResource(R.drawable.bg_view_grey_oval)
        view04.setBackgroundResource(R.drawable.bg_view_grey_oval)
        view05.setBackgroundResource(R.drawable.bg_view_grey_oval)
        view06.setBackgroundResource(R.drawable.bg_view_grey_oval)
    }

    private fun matchPassCode() {
        if (getPassCode() == passCode) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "PIN SALAH!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePassCode(passCode: String) {
        val preferences = getSharedPreferences("passcode_pref", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("passcode", passCode)
        editor.apply()
    }

    private fun getPassCode(): String {
        val preferences = getSharedPreferences("passcode_pref", Context.MODE_PRIVATE)
        return preferences.getString("passcode", "") ?: ""
    }
}

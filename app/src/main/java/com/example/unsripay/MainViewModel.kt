package com.example.unsripay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    // LiveData untuk mengamati perubahan nama dan saldo
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userSaldo = MutableLiveData<Int>()
    val userSaldo: LiveData<Int> get() = _userSaldo

    // Fungsi untuk memperbarui data pengguna
    fun updateUserData(userName: String, userSaldo: Int) {
        _userName.value = userName
        _userSaldo.value = userSaldo
    }
}


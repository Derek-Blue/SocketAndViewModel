package com.example.socketandviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {

    private val changedValue: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun valueChange(stocks: MutableList<String>) {
        changedValue.value = stocks
    }

    fun getChangedValue(): LiveData<MutableList<String>> {
        return changedValue
    }

}
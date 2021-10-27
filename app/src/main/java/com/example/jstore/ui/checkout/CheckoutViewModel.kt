package com.example.jstore.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.jstore.data.source.RajaOngkirDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val rajaOngkirDataSource: RajaOngkirDataSource) :
    ViewModel() {

    fun getProvince() = rajaOngkirDataSource.getProvinces().asLiveData()

    fun getCities(provinceId: String) = rajaOngkirDataSource.getCities(provinceId).asLiveData()

    fun getCost(origin: String, destination: String, weight: Int = 1000, courier: String) =
        rajaOngkirDataSource.getCost(origin, destination, weight, courier).asLiveData()

}
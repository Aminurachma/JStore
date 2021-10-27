package com.example.jstore.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.jstore.data.source.RajaOngkirDataSource
import com.example.jstore.data.source.RajaOngkirRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val rajaOngkirRepository: RajaOngkirRepository) :
    ViewModel() {

    fun getProvince() = rajaOngkirRepository.getProvinces().asLiveData()

    fun getCities(provinceId: String) = rajaOngkirRepository.getCities(provinceId).asLiveData()

    fun getCost(origin: String, destination: String, weight: Int = 1000, courier: String) =
        rajaOngkirRepository.getCost(origin, destination, weight, courier).asLiveData()

}
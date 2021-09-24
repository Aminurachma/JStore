package com.example.jstore.ui.orderhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityOrderHistoryBinding
import com.example.jstore.databinding.ActivityUpdateResiBinding

class UpdateResiActivity : BaseActivity() {
    private var _binding:ActivityUpdateResiBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateResiBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
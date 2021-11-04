package com.example.jstore.ui.rekening

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityRekeningBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.checkout.CheckoutActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class RekeningActivity : BaseActivity() {

    private var _binding: ActivityRekeningBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RekeningAdapter
    private var isAdmin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRekeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getRekeningList()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnAddRekening.setOnClickListener {
            startActivity(Intent(this, AddRekeningActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getRekeningList() {
        progress.show()
        FirestoreClass().getRekeningList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvNomorRekening.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvNomorRekening.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {
        binding.rvNomorRekening.adapter = adapter
        val type = intent.getStringExtra(EXTRA_TYPE)
        isAdmin = type.isNullOrEmpty()
        binding.btnAddRekening.isGone = !isAdmin
    }

    private fun setupAdapter() {
        adapter = RekeningAdapter(onClickListener = { rekening ->
            val intent = Intent()
            intent.putExtra(CheckoutActivity.EXTRA_REKENING, rekening)
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_CUSTOMER = "type_customer"
    }
}
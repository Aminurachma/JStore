package com.example.jstore.ui.rekening

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityRekeningBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class RekeningActivity : BaseActivity() {

    private var _binding: ActivityRekeningBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RekeningAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRekeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getRekeningList()
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
    }

    private fun setupAdapter() {
        adapter = RekeningAdapter(onClickListener = { rekening ->
            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ProductDetailsActivity.EXTRA_PRODUCT, rekening)
            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
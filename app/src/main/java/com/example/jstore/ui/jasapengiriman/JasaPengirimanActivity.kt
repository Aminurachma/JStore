package com.example.jstore.ui.jasapengiriman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityJasaPengirimanBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.checkout.CheckoutActivity
import com.example.jstore.ui.lokasipengiriman.AddLokasiPengirimanActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.ui.rekening.RekeningAdapter
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class JasaPengirimanActivity : BaseActivity() {

    private var _binding: ActivityJasaPengirimanBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: JasaPengirimanAdapter
    private var isAdmin = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityJasaPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        setupClickListener()
        getJasaPengirimanList()
    }

    private fun setupClickListener() {
        binding.btnAddJasa.setOnClickListener {
            startActivity(Intent(this, AddJasaPengirimanActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getJasaPengirimanList() {
        progress.show()
        FirestoreClass().getJasaPengirimanList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvJasaPengiriman.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvJasaPengiriman.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {
        binding.rvJasaPengiriman.adapter = adapter
        val type = intent.getStringExtra(EXTRA_TYPE)
        isAdmin = type.isNullOrEmpty()
        binding.btnAddJasa.isGone = !isAdmin
    }

    private fun setupAdapter() {
        adapter = JasaPengirimanAdapter(onClickListener = { jasaPengiriman ->
            val intent = Intent()
            intent.putExtra(CheckoutActivity.EXTRA_JASA, jasaPengiriman)
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
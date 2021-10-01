package com.example.jstore.ui.product

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityProductBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.profile.ProfileAdminActivity
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import timber.log.Timber

class ProductActivity : BaseActivity() {
    private var _binding: ActivityProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getProductList()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(onClickListener = { product ->
            startActivity(Intent(this, EditProductActivity::class.java).apply {
                putExtra(EditProductActivity.EXTRA_PRODUCT, product)
                Prefs.productId = product.productId
                Timber.e("Prefs ${Prefs.productId}")
            })
        }, this)
    }

    fun deleteProduct(productID : String){
//        Toast.makeText(requireActivity(), "You can now delete the product. $productID", Toast.LENGTH_SHORT).show()

        showAlertDialogToDeleteProduct(productID)
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.delete_dialog_title))
        builder.setMessage(getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(getString(R.string.yes)){
                dialogInterface,_->

            progress.show()

            FirestoreClass().deleteProduct(productID, onSuccessListener = {
                progress.dismiss()
                showToast(getString(R.string.product_delete_success))
                startActivity(
                    Intent(this, ProductActivity::class.java))
                finish()
            }, onFailureListener = {
                progress.dismiss()
                showToast(getString(R.string.update_product_failed, it.message.toString()))
            })

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)){
                dialogInterface,_->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun getProductList() {
        progress.show()
        FirestoreClass().getProductList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvProduct.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvProduct.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.jstore.ui.home.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.FragmentCustomerDashboardBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.example.jstore.ui.cart.MyCartActivity
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.ui.product.ProductAdapter
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.utils.*
import com.google.firebase.auth.FirebaseAuth

class CustomerDashboardFragment : Fragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter
    private lateinit var mUserDetails: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerDashboardBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupUI()
        setupClickListeners()
        getProductList()

    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter

        FirestoreClass().getUserDetails(onSuccessListener = {
            mUserDetails = it
            binding.tvWelcomingText.text = getString(R.string.hello_user, it.fullName)
            Glide.with(this)
                .load(it.image)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(binding.imgAvatar)
        }, onFailureListener = {
            logoutUser()
            logError("getUserDetails: ${it.message}")
        })
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        pushActivity(MainActivity::class.java)
        Prefs.clear()
        requireActivity().finish()
    }

    private fun setupClickListeners() {
        binding.imgAvatar.setOnClickListener{
            logoutUser()
        }

        binding.imgCart.setOnClickListener {
            pushActivity(MyCartActivity::class.java)
        }
    }

    private fun getProductList() {
        FirestoreClass().getProductList(onSuccessListener = {
            if (it.isNotEmpty()) {
                binding.rvProduct.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvProduct.toGone()
            }
        }, onFailureListener = {
            showToast(it.message.toString())
        })
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(onClickListener = {
            pushActivity(ProductDetailsActivity::class.java)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "CustomerDashboardFragment"

        @JvmStatic
        fun newInstance() = CustomerDashboardFragment()
    }
}
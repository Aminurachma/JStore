package com.example.jstore.ui.home.customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseFragment
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.FragmentCustomerDashboardBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.example.jstore.ui.cart.MyCartActivity
import com.example.jstore.ui.category.CategoryAdapter
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.ui.product.ProductActivity
import com.example.jstore.ui.product.ProductAdapter
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.ui.profile.ProfileActivity
import com.example.jstore.utils.*
import com.google.firebase.auth.FirebaseAuth

class CustomerDashboardFragment : BaseFragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter
    private lateinit var adapterCategory: CategoryAdapter
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
        getUserProfile()
        getProductList()
        getCategoryList()
        subscribeCart()

    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter
        binding.rvCategory.adapter = adapterCategory
        binding.searchView.searchBar.doOnTextChanged { text, _, _, _ ->
            adapter.filter.filter(text)
        }
    }

    private fun getCategoryList() {
        progress.show()
        FirestoreClass().getCategoryList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvCategory.toVisible()
                adapterCategory.submitList(it)
            } else {
                binding.rvCategory.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }
    private fun setupClickListeners() {
        binding.imgAvatar.setOnClickListener{
            pushActivity(ProfileActivity::class.java)
        }

        binding.imgCart.setOnClickListener {
            pushActivity(MyCartActivity::class.java)
        }
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(onClickListener = { product ->
            startActivity(Intent(requireContext(), ProductDetailsActivity::class.java).apply {
                putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, product.productId)
            })
        })
        adapterCategory = CategoryAdapter(onClickListener = { category ->
            subscribeProduct(category.namaCategory)
        })
    }


    private fun subscribeProduct(namaCategory: String) {
        progress.show()
        FirestoreClass().subscribeProduct(namaCategory, onSuccessListener = {
            progress.dismiss()
            binding.rvProduct.toVisible()
            adapter.submitList(it)
        }, onFailureListener = {
            progress.dismiss()
            logError("subscribeProduct: $it")
        })
    }

    private fun getUserProfile() {
        progress.show()
        FirestoreClass().subscribeUserProfile(onSuccessListener = {
            progress.dismiss()
            mUserDetails = it
            binding.tvWelcomingText.text = getString(R.string.hello_user, it.firstName +" "+ it.lastName)
            Glide.with(this)
                .load(it.image)
                .placeholder(R.drawable.user_pisc)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(binding.imgAvatar)
        }, onFailureListener = {
            progress.dismiss()
            logoutUser()
            logError("getUserDetails: $it")
        })
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        pushActivity(MainActivity::class.java)
        Prefs.clear()
        requireActivity().finish()
    }

    private fun getProductList() {
        progress.show()
        FirestoreClass().getProductList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvProduct.toVisible()
                adapter.submitList(it)
                adapter.setProductList(it)
                adapter.notifyItemRangeChanged(0, it.size)
            } else {
                binding.rvProduct.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun subscribeCart() {
        progress.show()
        FirestoreClass().subscribeToCart(onSuccessListener = {
            progress.dismiss()
            Prefs.activeCartId = it.cartId
        }, onFailureListener = {
            progress.dismiss()
            logError("subscribeCart: $it")
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
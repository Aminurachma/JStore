package com.example.jstore.ui.home.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.databinding.FragmentCustomerDashboardBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.ui.product.ProductAdapter
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.google.firebase.auth.FirebaseAuth

class CustomerDashboardFragment : Fragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter
    private lateinit var mUserDetails: User

    private lateinit var productList: List<Product>

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

//        setupAdapter()
//        setupUI()
//        setupClickListeners()

   //     mUserDetails = User("","","",0,"","","",1)
 //       Timber.d("checkProfileImage: ${mUserDetails.image}")
//        Glide.with(requireContext())
//            .load(mUserDetails.image)
//            .into(binding.imgAvatar)
       // GlideLoader(requireContext()).loadUserProfile(mUserDetails.image, binding.imgAvatar)

        binding.imgAvatar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            pushActivity(MainActivity::class.java)
        }

    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter
    }

    private fun setupClickListeners() {

    }

    fun successDashboardItemList(productItemList : ArrayList<Product>){
        if (productItemList.size > 0 ){
            binding.rvProduct.visibility = View.VISIBLE

            val adapter = ProductAdapter(requireActivity(),productItemList)
            binding.rvProduct.layoutManager = GridLayoutManager(activity,2)
            binding.rvProduct.setHasFixedSize(true)
            binding.rvProduct.adapter = adapter

//            adapter.setOnClickListener(object : ProductAdapter.OnClickListener{
//                override fun onClick(position: Int, product: Product) {
//                    val intent = Intent(requireActivity(),ProductDetailsActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,product.user_id)
//                    startActivity(intent)
//                }
//
//            })

        }else{
            binding.rvProduct.visibility = View.GONE
        }

    }

    private fun getDashboardItemsList() {
        FirestoreClass().getDashboardItemsList(onSuccessListener = {
            productList = it
        }, onFailureListener = {
            showToast(it.message.toString())
        })
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

//    private fun setupAdapter() {
//        adapter = ProductAdapter(onClickListener = {
//            pushActivity(ProductDetailsActivity::class.java)
//        })
//        adapter.submitList(dummyProducts)
//    }

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
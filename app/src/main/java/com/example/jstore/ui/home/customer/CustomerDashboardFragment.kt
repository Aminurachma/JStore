package com.example.jstore.ui.home.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jstore.R
import com.example.jstore.data.source.remote.response.Category
import com.example.jstore.data.source.remote.response.Product
import com.example.jstore.databinding.FragmentCustomerDashboardBinding
import com.example.jstore.ui.product.ProductAdapter

class CustomerDashboardFragment : Fragment() {

    private var _binding: FragmentCustomerDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter

    private val dummyProducts =
        listOf(
            Product("1", "Jam tangan mewah", Category("id", "Jam"), "https://www.jakartanotebook.com/images/products/77/63/28045/2/jam-tangan-wanita-unique-dial-black-13.jpg", "Rp. 50.000"),
            Product("1", "Jam tangan mewah", Category("id", "Jam"), "https://www.jakartanotebook.com/images/products/77/63/28045/2/jam-tangan-wanita-unique-dial-black-13.jpg", "Rp. 50.000"),
            Product("1", "Jam tangan mewah", Category("id", "Jam"), "https://www.jakartanotebook.com/images/products/77/63/28045/2/jam-tangan-wanita-unique-dial-black-13.jpg", "Rp. 50.000"),
            Product("1", "Jam tangan mewah", Category("id", "Jam"), "https://www.jakartanotebook.com/images/products/77/63/28045/2/jam-tangan-wanita-unique-dial-black-13.jpg", "Rp. 50.000"),
            Product("1", "Jam tangan mewah", Category("id", "Jam"), "https://www.jakartanotebook.com/images/products/77/63/28045/2/jam-tangan-wanita-unique-dial-black-13.jpg", "Rp. 50.000")
        )

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

    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(onClickListener = {

        })
        adapter.submitList(dummyProducts)
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
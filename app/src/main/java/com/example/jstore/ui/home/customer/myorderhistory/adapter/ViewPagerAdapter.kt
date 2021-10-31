package com.example.jstore.ui.home.customer.myorderhistory.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jstore.ui.home.customer.myorderhistory.fragment.BelumDibayarFragment
import com.example.jstore.ui.home.customer.myorderhistory.fragment.DikemasFragment
import com.example.jstore.ui.home.customer.myorderhistory.fragment.DikirimFragment
import com.example.jstore.ui.home.customer.myorderhistory.fragment.DiterimaFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BelumDibayarFragment()
            1 -> DikemasFragment()
            2 -> DikirimFragment()
            3 -> DiterimaFragment()
            else -> BelumDibayarFragment()
        }
    }
}
package com.example.jstore.ui.orderhistory.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jstore.ui.orderhistory.fragment.MenungguKonfirmasiAdminFragment
import com.example.jstore.ui.orderhistory.fragment.PerluDikirimAdminFragment
import com.example.jstore.ui.orderhistory.fragment.SedangDikirimAdminFragment
import com.example.jstore.ui.orderhistory.fragment.SelesaiAdminFragment

class ViewPagerAdminAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MenungguKonfirmasiAdminFragment()
            1 -> PerluDikirimAdminFragment()
            2 -> SedangDikirimAdminFragment()
            3 -> SelesaiAdminFragment()
            else -> MenungguKonfirmasiAdminFragment()
        }
    }

}




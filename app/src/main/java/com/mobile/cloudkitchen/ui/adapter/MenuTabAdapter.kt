package com.mobile.cloudkitchen.ui.adapter

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mobile.cloudkitchen.ui.fragments.WklyMonthlyMenuFragment

class MenuTabAdapter(activity: Activity, fm: FragmentManager, planType:String,count: Int) :
    FragmentPagerAdapter(fm) {

    private val ctxt: Context? = null
    private var tabSize = count
    private var planType = planType
    override fun getCount(): Int {
        return tabSize
    }

    override fun getItem(position: Int): Fragment {
        return WklyMonthlyMenuFragment(position,planType);
    }


}
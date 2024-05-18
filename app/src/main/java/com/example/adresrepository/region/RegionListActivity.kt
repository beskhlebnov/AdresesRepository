package com.example.adresrepository.region

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.adresrepository.SingleFragmentActivity

class RegionListActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, RegionListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = RegionListFragment()
}
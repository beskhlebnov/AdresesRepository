package com.example.adresrepository.region

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class RegionUpdateActivity : SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id: Int?): Intent{
            val intent = Intent(packageContext, RegionUpdateActivity::class.java)
            intent.putExtra(RegionUpdateFragment.ARG_REGION_ID,id)
            return intent
        }
    }

    override fun createFragment(): RegionUpdateFragment {
        val id = intent.getSerializableExtra(RegionUpdateFragment.ARG_REGION_ID) as Int?
        return RegionUpdateFragment.newInstance(id)
    }
}
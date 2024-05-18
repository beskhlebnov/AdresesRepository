package com.example.adresrepository.person

import android.content.Context
import android.content.Intent
import com.example.adresrepository.SingleFragmentActivity

class PersonUpdateActivity : SingleFragmentActivity() {
    companion object{
        const val ARG_PERSON_ID = "person_id"

        fun newIntent(packageContext: Context?, personId: Int): Intent{
            val intent = Intent(packageContext, PersonUpdateActivity::class.java)
            intent.putExtra(ARG_PERSON_ID,personId)
            return intent
        }
    }

    override fun createFragment(): PersonUpdateFragment {
        val personId = intent.getSerializableExtra(ARG_PERSON_ID) as Int?
        return PersonUpdateFragment.newInstance(personId)
    }
}
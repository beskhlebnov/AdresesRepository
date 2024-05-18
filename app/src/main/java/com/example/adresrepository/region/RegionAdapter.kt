package com.example.adresrepository.region

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.adresrepository.R

class RegionAdapter(context: Context, list: List<Region>) : ArrayAdapter<Region>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val region = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_region, parent, false)
        if (region != null) {
            val name = view.findViewById<TextView>(R.id.name)
            name.text = region.name
        }

        return view
    }
}
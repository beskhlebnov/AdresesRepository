package com.example.adresrepository.region

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adresrepository.person.PersonListActivity
import com.example.adresrepository.R
import com.example.adresrepository.city.CityListActivity
import com.example.adresrepository.country.CountryListActivity

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RegionListFragment: Fragment() {
    private lateinit var buttonCountry: Button
    private lateinit var buttonPerson: Button
    private lateinit var buttonCity: Button
    private var recycleView: RecyclerView? = null
    private var adapter: RegionAdapter? = null
    private lateinit var addButton: ImageButton
    private val client = OkHttpClient()
    private var regions = mutableListOf<Region>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.list_region_fragment, container, false)
        buttonCountry = view.findViewById(R.id.country)
        buttonPerson = view.findViewById(R.id.person)
        buttonCity = view.findViewById(R.id.city)
        buttonCity.setOnClickListener {context?.startActivity(CityListActivity.newIntent(context))}
        buttonCountry.setOnClickListener { context?.startActivity(CountryListActivity.newIntent(context))}
        buttonPerson.setOnClickListener {context?.startActivity(PersonListActivity.newIntent(context))}
        recycleView = view.findViewById(R.id.region_recycler_view)
        recycleView!!.layoutManager = LinearLayoutManager(activity)
        addButton = view.findViewById(R.id.add)
        addButton.setOnClickListener {
            val intent = RegionAddActivity.newIntent(context)
            context?.startActivity(intent)
        }
        getInfo()
        return view
    }

    private fun getInfo(){
        val request = Request.Builder().url("http://192.168.1.4:5000/region").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                regions.clear()
                for(i in 0..<objectcList.length()){
                    println(JSONObject(objectcList.get(i).toString()))
                    val iter = JSONObject(objectcList.get(i).toString())
                    regions.add(Region(iter.getInt("id"), iter.getInt("country_id"),iter.getString("name"), iter.getString("country")))
                }
            }
        })

        if (adapter == null){
            adapter = RegionAdapter(regions)
            recycleView!!.adapter = adapter
        }

    }

    private class Holder (item: View?): ViewHolder(item!!), View.OnClickListener {
        var nameTextView: TextView? = itemView.findViewById(R.id.list_item_region_name)
        var countryTextView: TextView? = itemView.findViewById(R.id.list_item_region_country)


        private lateinit var region: Region

        @SuppressLint("SetTextI18n")
        fun bindCountry(region1: Region){
            this.region = region1
            nameTextView?.text = region1.name
            countryTextView?.text = region1.country
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = RegionUpdateActivity.newIntent(context, region.id)
            context.startActivity(intent)
        }
    }
    private class RegionAdapter(regionList: List<Region>?): RecyclerView.Adapter<Holder?>(){
        private var regions: List<Region>? = null
        init { this.regions = regionList }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_region, parent,false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return  regions!!.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val country = regions!![position]
            holder.bindCountry(country)

        }
    }

}
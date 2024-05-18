package com.example.adresrepository.city

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
import com.example.adresrepository.country.CountryListActivity
import com.example.adresrepository.person.PersonListActivity
import com.example.adresrepository.R
import com.example.adresrepository.region.RegionListActivity

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class CityListFragment: Fragment() {
    private lateinit var buttonCountry: Button
    private lateinit var buttonRegion: Button
    private lateinit var buttonPerson: Button
    private var recycleView: RecyclerView? = null
    private var adapter: RegionAdapter? = null
    private lateinit var addButton: ImageButton
    private val client = OkHttpClient()
    private var cities = mutableListOf<City>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.list_city_fragment, container, false)
        buttonCountry = view.findViewById(R.id.country)
        buttonPerson = view.findViewById(R.id.person)
        buttonRegion = view.findViewById(R.id.region)
        buttonCountry.setOnClickListener { context?.startActivity(
            CountryListActivity.newIntent(
                context
            )
        )}
        buttonPerson.setOnClickListener {context?.startActivity(PersonListActivity.newIntent(context))}
        buttonRegion.setOnClickListener {context?.startActivity(RegionListActivity.newIntent(context))}
        recycleView = view.findViewById(R.id.region_recycler_view)
        recycleView!!.layoutManager = LinearLayoutManager(activity)
        addButton = view.findViewById(R.id.add)
        addButton.setOnClickListener {
            val intent = CityAddActivity.newIntent(context)
            context?.startActivity(intent)
        }
        getInfo()
        return view
    }

    private fun getInfo(){
        val request = Request.Builder().url("http://192.168.1.4:5000/city").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                cities.clear()
                for(i in 0..<objectcList.length()){
                    println(JSONObject(objectcList.get(i).toString()))
                    val iter = JSONObject(objectcList.get(i).toString())
                    cities.add(City(iter.getInt("id"), iter.getInt("region_id"),iter.getString("name"), iter.getString("region")))
                }
            }
        })

        if (adapter == null){
            adapter = RegionAdapter(cities)
            recycleView!!.adapter = adapter
        }

    }

    private class Holder (item: View?): ViewHolder(item!!), View.OnClickListener {
        var nameTextView: TextView? = itemView.findViewById(R.id.list_item_city_name)
        var regionTextView: TextView? = itemView.findViewById(R.id.list_item_city_region)


        private lateinit var city: City

        @SuppressLint("SetTextI18n")
        fun bindCountry(city1: City){
            this.city = city1
            nameTextView?.text = city1.name
            regionTextView?.text = city1.region
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = CityUpdateActivity.newIntent(context, city.id)
            context.startActivity(intent)
        }
    }
    private class RegionAdapter(cityList: List<City>?): RecyclerView.Adapter<Holder?>(){
        private var cities: List<City>? = null
        init { this.cities = cityList }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_city, parent,false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return  cities!!.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val country = cities!![position]
            holder.bindCountry(country)

        }
    }

}
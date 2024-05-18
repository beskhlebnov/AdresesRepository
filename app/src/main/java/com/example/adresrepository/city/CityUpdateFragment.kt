package com.example.adresrepository.city
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adresrepository.R
import com.example.adresrepository.region.Region
import com.example.adresrepository.region.RegionAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class CityUpdateFragment : Fragment() {

    companion object{
        const val ARG_CITY_ID = "city_id"
        fun newInstance(personId: Int?) =
            CityUpdateFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CITY_ID, personId)
                }
            }
    }
    private val client = OkHttpClient()
    private var cityId = 0
    private var city: City? = null
    private var regionList = mutableListOf<Region>()
    private lateinit var nameField: EditText
    private lateinit var regionSpinner: Spinner
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityId = (requireActivity().intent.getSerializableExtra(ARG_CITY_ID) as Int?)!!
        val request = Request.Builder().url("http://192.168.1.4:5000/city/${cityId}").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body()!!.string())
                city = City(jsonObject.getInt("id"), jsonObject.getInt("region_id"),
                    jsonObject.getString("name"), jsonObject.getString("region"))
            }
        })

        client.newCall(Request.Builder().url("http://192.168.1.4:5000/region").build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException){
                Log.e("CallBackError", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                regionList.clear()
                for(i in 0..<objectcList.length()){
                    val iter = JSONObject(objectcList.get(i).toString())
                    regionList.add(
                        Region(iter.getInt("id"), iter.getInt("country_id"),iter.getString("name"),
                        iter.getString("country"))
                    )
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.update_city_fragment, container, false)
        editButton = v.findViewById(R.id.edit)
        backButton = v.findViewById(R.id.back)
        deleteButton = v.findViewById(R.id.delete)
        regionSpinner = v.findViewById(R.id.region_spinner)

        regionSpinner.adapter = RegionAdapter(requireContext(), regionList)
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city?.regionId= regionList[position].id
                city?.region= regionList[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        backButton.setOnClickListener {goBack()}

        editButton.setOnClickListener {
            if (nameField.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("name", nameField.text.toString())
                .addFormDataPart("region_id", city?.regionId.toString())
                .build()
            val request = Request.Builder().url("http://192.168.1.4:5000/city/${cityId}").put(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("onFailLoginRequest", e.toString())
                }
                override fun onResponse(call: Call, response: Response) {
                    val objectc = JSONObject(response.body()!!.string())
                    val statusa: String = objectc.getString("status")
                    if (statusa == "200"){goBack()}
                }
            })
            }
        }

        deleteButton.setOnClickListener {
            val request = Request.Builder().url("http://192.168.1.4:5000/city/${cityId}").delete().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
                override fun onResponse(call: Call, response: Response) {
                    val objectc = JSONObject(response.body()!!.string())
                    val statusa: String = objectc.getString("status")
                    if (statusa == "200"){ goBack()}
                }
            })
        }

        nameField = v.findViewById(R.id.name)
        nameField.setText(city?.name)
        regionSpinner.setSelection(regionList.indexOfFirst {it.id == city?.regionId})
        return v
    }

    fun goBack(){
        val intent = CityListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}
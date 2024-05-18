package com.example.adresrepository.region
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
import com.example.adresrepository.country.Country
import com.example.adresrepository.country.CountryAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RegionUpdateFragment : Fragment() {

    companion object{
        const val ARG_REGION_ID = "region_id"
        fun newInstance(personId: Int?) =
            RegionUpdateFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_REGION_ID, personId)
                }
            }
    }
    private val client = OkHttpClient()
    private var regionId = 0
    private var region: Region? = null
    private var countrys = mutableListOf<Country>()
    private lateinit var nameField: EditText
    private lateinit var countrySpinner: Spinner
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        regionId = (requireActivity().intent.getSerializableExtra(ARG_REGION_ID) as Int?)!!
        val request = Request.Builder().url("http://192.168.1.4:5000/region/${regionId}").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { Log.e("GGG", e.toString()) }
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body()!!.string())
                region = Region(jsonObject.getInt("id"), jsonObject.getInt("country_id"),
                    jsonObject.getString("name"), jsonObject.getString("country"))
            }
        })

        client.newCall(Request.Builder().url("http://192.168.1.4:5000/country")
            .build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException){
                Log.e("CallBackError", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                countrys.clear()
                for(i in 0..<objectcList.length()){
                    println(JSONObject(objectcList.get(i).toString()))
                    val iter = JSONObject(objectcList.get(i).toString())
                    countrys.add(
                        Country(iter.getInt("id"), iter.getString("fullname"),
                        iter.getString("shortname"))
                    )
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.update_region_fragment, container, false)
        editButton = v.findViewById(R.id.edit)
        backButton = v.findViewById(R.id.back)
        deleteButton = v.findViewById(R.id.delete)
        countrySpinner = v.findViewById(R.id.country_spinner)

        countrySpinner.adapter = CountryAdapter(requireContext(), countrys)
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                region?.countryId=countrys.get(position).id
                region?.country=countrys.get(position).shortname
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        editButton.setOnClickListener {
            if (nameField.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("name", nameField.text.toString())
                .addFormDataPart("country_id", region?.countryId.toString())
                .build()
            val request = Request.Builder().url("http://192.168.1.4:5000/region/${regionId}").put(body).build()

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
        backButton.setOnClickListener {goBack()}
        deleteButton.setOnClickListener {
            val request = Request.Builder().url("http://192.168.1.4:5000/region/${regionId}").delete().build()
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
        nameField.setText(region?.name)
        countrySpinner.setSelection(countrys.indexOfFirst {it.id == region?.countryId})
        return v
    }

    fun goBack(){
        val intent = RegionListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}
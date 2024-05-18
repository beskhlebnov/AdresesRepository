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

class CityAddFragment : Fragment() {

    companion object{ fun newInstance() = CityAddFragment() }

    private val client = OkHttpClient()
    private var regionId = 0
    private lateinit var nameField: EditText
    private var regionList = mutableListOf<Region>()
    private lateinit var regionSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client.newCall(Request.Builder().url("http://192.168.1.4:5000/region")
            .build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException){ Log.e("CallBackError", e.toString())}

            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                regionList.clear()
                for(i in 0..<objectcList.length()){
                    val iter = JSONObject(objectcList.get(i).toString())
                    regionList.add(Region(iter.getInt("id"), iter.getInt("country_id"),iter.getString("name"), iter.getString("country")))
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.add_city_fragment, container, false)
        saveButton = v.findViewById(R.id.save)
        backButton = v.findViewById(R.id.back)
        cancelButton = v.findViewById(R.id.cancel)
        regionSpinner = v.findViewById(R.id.region_spinner)

        nameField = v.findViewById(R.id.name)
        backButton.setOnClickListener {goBack()}
        regionSpinner.adapter = RegionAdapter(requireContext(), regionList)
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               regionId=regionList.get(position).id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        regionSpinner.setSelection(0)

        saveButton.setOnClickListener {
            if (nameField.text.toString().isEmpty() && regionId==0){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", nameField.text.toString())
                    .addFormDataPart("region_id", regionId.toString())
                    .build()
                val request =
                    Request.Builder().url("http://192.168.1.4:5000/city").post(body).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("onFailLoginRequest", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val objectc = JSONObject(response.body()!!.string())
                        val statusa: String = objectc.getString("status")
                        if (statusa == "200") {goBack()}
                    }
                })
            }
        }

        cancelButton.setOnClickListener {goBack()}
        return v
    }

    fun goBack(){
        val intent = CityListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}
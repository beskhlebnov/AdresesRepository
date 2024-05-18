package com.example.adresrepository.person

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
import com.example.adresrepository.city.City
import com.example.adresrepository.city.CityAdapter
import com.example.adresrepository.city.CityListActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PersonAddFragment : Fragment() {

    companion object{
        fun newInstance() = PersonAddFragment()
    }

    private val client = OkHttpClient()
    private var cityId = 0
    private var cityList = mutableListOf<City>()
    private lateinit var personField: EditText
    private lateinit var streetField: EditText
    private lateinit var buildingField: EditText
    private lateinit var offficeField: EditText
    private lateinit var citySpinner: Spinner
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button
    private lateinit var backButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client.newCall(Request.Builder().url("http://192.168.1.4:5000/city").build()).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException){
                Log.e("CallBackError", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val objectcList = JSONObject(response.body()!!.string()).getJSONArray("list")
                cityList.clear()
                for(i in 0..<objectcList.length()){
                    val iter = JSONObject(objectcList.get(i).toString())
                    cityList.add(
                        City(iter.getInt("id"), iter.getInt("region_id"),iter.getString("name"),
                            iter.getString("region"))
                    )
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.add_person_fragment, container, false)
        personField = v.findViewById(R.id.person)
        streetField = v.findViewById(R.id.street)
        buildingField = v.findViewById(R.id.building)
        offficeField = v.findViewById(R.id.office)
        citySpinner = v.findViewById(R.id.city_spinner)
        editButton = v.findViewById(R.id.save)
        backButton = v.findViewById(R.id.back)
        cancelButton = v.findViewById(R.id.cancel)

        cancelButton.setOnClickListener {goBack()}
        citySpinner.adapter = CityAdapter(requireContext(), cityList)
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cityId= cityList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        citySpinner.setSelection(0)

        backButton.setOnClickListener {goBack()}

        editButton.setOnClickListener {
            if (personField.text.toString().isEmpty() && streetField.text.isEmpty() && buildingField.text.isEmpty() && offficeField.text.isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("person", personField.text.toString())
                    .addFormDataPart("city_id", cityId.toString())
                    .addFormDataPart("street", streetField.text.toString())
                    .addFormDataPart("building", buildingField.text.toString())
                    .addFormDataPart("office", offficeField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.1.4:5000/person").post(body).build()

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
        return v
    }
    fun goBack(){ context?.startActivity(PersonListActivity.newIntent(context))}
}
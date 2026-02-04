package com.stranger_sparks.view.activities.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.Age
import com.stranger_sparks.data_model.AgeResponse
import com.stranger_sparks.data_model.CastResponse
import com.stranger_sparks.data_model.Caste
import com.stranger_sparks.data_model.UserProfileResponse
import com.stranger_sparks.databinding.ActivityFiltersBinding
import com.stranger_sparks.view.activities.BasicProfileActivity.CustomArrayAdapter
import com.stranger_sparks.view.activities.ui.fragments.home.HomeViewModel
import com.stranger_sparks.viewmodel.BasicProfileViewModel
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class FiltersActivity : AppCompatActivity() {
    lateinit var binding: ActivityFiltersBinding
    val religionlist = mutableListOf<Caste>()
    val ageList = mutableListOf<Age>()
    val genderlist = mutableListOf<String>()
    var gender=""
    var castSelected=""
    var age=""
    var locationAddress=""
    @Inject
    lateinit var repository: StrangerSparksRepository
    @Inject
    lateinit var viewModel: BasicProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding=ActivityFiltersBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        viewModel.getCitysList()
        viewModel.cityListLiveData.observe(this) { response ->
            response?.data?.let { cities ->
                val cityNames = cities.map { it.name }
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)
                binding.llAutoCompleteTextView.setAdapter(adapter)
                binding.llAutoCompleteTextView.threshold = 1
            }
        }
        repository.getCaste().enqueue(object:retrofit2.Callback<CastResponse> {
            override fun onResponse(
                call: Call<CastResponse>,
                response: Response<CastResponse>
            ) {
                religionlist.clear()
                religionlist.add( Caste("0","Select Caste"))
                religionlist.addAll(response.body()!!.data)

            }

            override fun onFailure(call: Call<CastResponse>, t: Throwable) {

            }

        })
        repository.getAgeFilter().enqueue(object:retrofit2.Callback<AgeResponse> {
            override fun onResponse(
                call: Call<AgeResponse>,
                response: Response<AgeResponse>
            ) {
                ageList.clear()
                ageList.add( Age("0","Select Age"))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ageList.addAll(body.data)
                    }
                }
            }

            override fun onFailure(call: Call<AgeResponse>, t: Throwable) {

            }

        })


        genderlist.add( "I'm Looking for")
        genderlist.add( "Bride")
        genderlist.add( "BrideGroom")

        religionlist.add( Caste("0","Select Caste"))
        ageList.add( Age("0","Select Age"))
       // religionlist.add( "Muslim")
       // religionlist.add( "Cristian")
        val adapter = CustomArrayAdapter(this, R.layout.custom_spinner_dropdown_item, genderlist)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spgender.adapter = adapter


        val religionAdapter = CasteArrayAdapter(this, R.layout.custom_spinner_dropdown_item, religionlist)
        religionAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spreligion.adapter = religionAdapter

        val ageAdapter = AgeArrayAdapter(this, R.layout.custom_spinner_dropdown_item, ageList)
        ageAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spAge.adapter = ageAdapter


        binding.ivClose.setOnClickListener {
            setResult(400)
            finish()
        }
        binding.btnapplyFilter.setOnClickListener {

            val pos=binding.spgender.selectedItemPosition
            if(pos==0)
                gender=""
            else if(pos==1)
                gender="Male"
            if(pos==2)
                gender="Female"

            val agePos=binding.spAge.selectedItemPosition
            if(agePos==0)
                age=""
            else age=ageList.get(agePos).id


            val relPos=binding.spreligion.selectedItemPosition
            if(relPos==0)
                castSelected=""
            else castSelected=religionlist.get(relPos).id


            locationAddress = binding.llAutoCompleteTextView.text.toString().trim()

            val intent=Intent()
            intent.putExtra("gender",gender)
            intent.putExtra("religion",castSelected)
            intent.putExtra("address",locationAddress)
            intent.putExtra("age",age)
            setResult(200,intent)
            finish()
        }
    }

    inner class CasteArrayAdapter(context: Context, resource: Int, objects: List<Caste>) :
        ArrayAdapter<Caste>(context, resource, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            if (position == 0) {
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            }
            (view as TextView).setText(religionlist.get(position).name)
            if(position==0)
                castSelected=""
            else castSelected=religionlist.get(position).id
            return view
        }
    }

    inner class AgeArrayAdapter(context: Context, resource: Int, objects: List<Age>) :
        ArrayAdapter<Age>(context, resource, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            if (position == 0) {
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            }
            (view as TextView).setText(ageList.get(position).name)
            if(position==0)
                age=""
            else age=ageList.get(position).id
            return view
        }
    }
}


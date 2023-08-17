package com.example.covid19

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var covidArrayList: ArrayList<Model>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:RVAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchArrayList: ArrayList<Model>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycleView)
        covidArrayList = ArrayList()
        searchArrayList = ArrayList()
        searchView = findViewById(R.id.search_bar)

        //setting recyclerview adapter
        adapter = RVAdapter(this,searchArrayList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchArrayList.clear()
                val searText = newText!!.lowercase(Locale.ROOT)

                if (searText.isNotEmpty()){

                    covidArrayList.forEach{
                        if(it.city.lowercase(Locale.ROOT).contains(searText) || it.state.lowercase(Locale.ROOT).contains(searText)){
                            searchArrayList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                else{
                    searchArrayList.clear()
                    searchArrayList.addAll(covidArrayList)
                    adapter.notifyDataSetChanged()
                }
                return false
            }
        })

    }

    private fun fetchData() {
        val url = "https://data.covid19india.org/state_district_wise.json"

        val req = StringRequest(Request.Method.GET,url,
            {response ->
                try {
                    val jsonObject = JSONObject(response)
                    val states = jsonObject.keys()

                    states.next()

                    while (states.hasNext()){
                        val state = states.next()
                        val obj1 = jsonObject.getJSONObject(state)
                        val obj2 = obj1.getJSONObject("districtData")
                        val cities = obj2.keys()

                        while (cities.hasNext()){
                            val city = cities.next()
                            val obj3 = obj2.getJSONObject(city)

                            val active = obj3.getString("active")
                            val confirmed = obj3.getString("confirmed")
                            val deceased = obj3.getString("deceased")
                            val recovered = obj3.getString("recovered")

                            val model = Model(state,city, active, confirmed, deceased, recovered)
                            covidArrayList.add(model)
                        }
                    }
                    searchArrayList.addAll(covidArrayList)
                    adapter.notifyDataSetChanged()
                }
                catch (e:JSONException){
                    e.printStackTrace()
                    Toast.makeText(applicationContext,"Error on Response",Toast.LENGTH_LONG).show()

                }
        }, {error ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            })

        val reqQueue = Volley.newRequestQueue(this)
        reqQueue.add(req)
    }


}
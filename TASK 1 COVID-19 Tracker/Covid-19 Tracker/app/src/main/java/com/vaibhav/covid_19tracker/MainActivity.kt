package com.vaibhav.covid_19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    lateinit var worldCasesTV: TextView
    lateinit var worldRecoverdTV: TextView
    lateinit var worldDeathsTV: TextView
    lateinit var cuntryCasesTV: TextView
    lateinit var cuntryRecoveredTV: TextView
    lateinit var cuntryDeathsTV: TextView
    lateinit var stateRV: RecyclerView
    lateinit var stateRVAdapter: StateRVAdapter
    lateinit var stateList: List<StateModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        worldCasesTV = findViewById(R.id.idTvWorldCases)
        worldRecoverdTV = findViewById(R.id.idTvRecoverCases)
        worldDeathsTV = findViewById(R.id.idTvDeathsCases)
        cuntryCasesTV = findViewById(R.id.idTvIndiaCases)
        cuntryDeathsTV = findViewById(R.id.idTvIndiaDeathsCases)
        cuntryRecoveredTV = findViewById(R.id.idTvIndiaRecoverCases)
        stateRV = findViewById(R.id.idTvStates)
        stateList = ArrayList<StateModel>()
        getStateInfo()
    }

    private fun getStateInfo() {
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {

              val dataObj = response.getJSONObject("data")
              val summaryObj=dataObj.getJSONObject("summary")
              val cases:Int=summaryObj.getInt("total")
                    val recovered:Int=summaryObj.getInt("Discharged")
                    val deaths:Int=summaryObj.getInt("deaths")
                    cuntryCasesTV.text=cases.toString()
                    cuntryRecoveredTV.text=recovered.toString()
                    cuntryDeathsTV.text=deaths.toString()

                    val regionalArray=dataObj.getJSONArray("regional")
                for(i in 0 until regionalArray.length()){
                    val regionalObj=regionalArray.getJSONObject(i)
                    val stateName:String=regionalObj.getString("Loc")
                    val cases:Int=regionalObj.getInt("totalConfirmed")
                    val deaths:Int=regionalObj.getInt("Deaths")
                    val recovered:Int=regionalObj.getInt("discharged")
                    val stateModel =StateModel(stateName,recovered,deaths,cases)
                    stateList=stateList+stateModel
                }
                    stateRVAdapter= StateRVAdapter(stateList)
                    stateRV.layoutManager=LinearLayoutManager(this)
                    stateRV.adapter=stateRVAdapter


                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }, { error ->
                {
                    Toast.makeText(this, "fail to get data", Toast.LENGTH_SHORT).show()
                }

            })
        queue.add(request)
    }
    private fun getWorldInfo(){
        val url ="https://disease.sh/v3/covid-19/all"
        val queue =Volley.newRequestQueue(this@MainActivity)
        val request=
            JsonObjectRequest(Request.Method.GET,url,null,{
                response->
             val worldcases:Int =response.getInt("cases")
             val worldRecovered:Int=response.getInt("recovered")
             val worldDeaths:Int =response.getInt("deaths")
          worldRecoverdTV.text=worldRecovered.toString()
                worldCasesTV.text=worldcases.toString()
                worldDeathsTV.text=worldDeaths.toString()

            }

                ,{
                error->
                Toast.makeText(this,"fail to get data",Toast.LENGTH_SHORT).show()
            })
    }
}




















































































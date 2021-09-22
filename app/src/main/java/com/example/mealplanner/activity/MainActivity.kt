package com.example.mealplanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import com.example.mealplanner.R
import com.example.mealplanner.adapter.PlannerAdapter
import com.example.mealplanner.adapter.TodayPlannerAdapter
import com.example.mealplanner.model.DatabaseHandler
import com.example.mealplanner.model.PlannerData
import kotlinx.android.synthetic.main.activity_download_meal.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_planner.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var plannerList : ArrayList<PlannerData> = ArrayList<PlannerData>()
    var dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(this.supportActionBar!=null)
         this.supportActionBar!!.hide()


        loadPlanner()

        //Button Planner
        btn_planner.setOnClickListener {
            val intent = Intent(this, PlannerActivity::class.java)
            startActivity (intent)
        }

        btn_grocery.setOnClickListener {
            val intent = Intent(this, GroceryActivity::class.java)
            startActivity (intent)
        }

        btn_recipe.setOnClickListener {
            val intent = Intent(this, MealListActivity::class.java)
            startActivity (intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadPlanner()
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getDateTime(addDate:Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, addDate)
        return cal.time
    }

    fun loadPlanner() {
        plannerList.clear()
            val date = getDateTime(0)
            val dateInString = date.toString("yyyyMMdd")
            for (j in 1..3) {
                val plannerId = dateInString + j.toString()
                val plannerValue = dbHandler.getPlanner(plannerId)
                if (plannerValue.isNotEmpty()) {
                    for (k in plannerValue.indices) {
                        plannerList.add(plannerValue[k])
                    }
                }
                else {
                    plannerList.add(PlannerData(PlannerId = plannerId))
                }
            }


        val cardListAdapter = TodayPlannerAdapter(
            this,
            plannerList
        )
        listview_today.adapter = cardListAdapter
    }
}
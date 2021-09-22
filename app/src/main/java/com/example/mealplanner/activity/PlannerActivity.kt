package com.example.mealplanner.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplanner.R
import com.example.mealplanner.adapter.MealListAdapter
import com.example.mealplanner.adapter.PlannerAdapter
import com.example.mealplanner.model.DatabaseHandler
import com.example.mealplanner.model.MealContentModel
import com.example.mealplanner.model.MealData
import com.example.mealplanner.model.PlannerData
import kotlinx.android.synthetic.main.activity_meal_list.*
import kotlinx.android.synthetic.main.activity_meal_list.listview_meal
import kotlinx.android.synthetic.main.activity_planner.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlannerActivity : AppCompatActivity() {
    val mealContentModel = MealContentModel(this)
    var plannerList : ArrayList<PlannerData> = ArrayList<PlannerData>()
    var dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        loadPlanner()


    }

    override fun onResume() {
        super.onResume()
        loadPlanner()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
        for (i in 0..6) {
            val date = getDateTime(i)
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
        }

        val cardListAdapter = PlannerAdapter(
            this,
            plannerList
        )
        listview_planner.adapter = cardListAdapter
        listview_planner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, MealListActivity::class.java)
                intent.putExtra("fromPlanner", true)
                intent.putExtra("plannerId",plannerList[position].PlannerId)
                startActivity (intent)
            }
    }

}
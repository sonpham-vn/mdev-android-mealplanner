package com.example.mealplanner.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mealplanner.R
import com.example.mealplanner.model.PlannerData

class PlannerAdapter(private val context: Activity, private val plannerArray: List<PlannerData>)
    : ArrayAdapter<PlannerData>(context, R.layout.customview_planner, plannerArray) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.customview_planner, null, true)

        val textMealPeriod = rowView.findViewById(R.id.meal_period) as TextView
        val textMealDate = rowView.findViewById(R.id.meal_date) as TextView
        val textMealName = rowView.findViewById(R.id.meal_name) as TextView

        var period = ""
        when (plannerArray[position].PlannerId?.get(8)) {
            '1' -> period = "Breakfast"
            '2' -> period = "Lunch"
            '3' -> period = "Dinner"
            else -> period = ""
        }

        val plannerId = plannerArray[position].PlannerId
        textMealPeriod.text = period
        if (plannerId != null) {
            textMealDate.text = "${plannerId.substring(4,6)}/${plannerId.substring(6,8)}"
        }
        textMealName.text = "${plannerArray[position].MealName}"
        return rowView
    }
}
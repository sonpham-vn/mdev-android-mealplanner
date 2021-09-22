package com.example.mealplanner.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import com.example.mealplanner.R
import com.example.mealplanner.model.PlannerData

class TodayPlannerAdapter(private val context: Activity, private val plannerArray: List<PlannerData>)
    : ArrayAdapter<PlannerData>(context, R.layout.customview_todayplanner, plannerArray) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.customview_todayplanner, null, true)

        val textPeriod = rowView.findViewById(R.id.meal_period) as TextView
        val textMealName = rowView.findViewById(R.id.meal_name) as TextView
        var period = ""
        when (plannerArray[position].PlannerId?.get(8)) {
            '1' -> period = "Breakfast"
            '2' -> period = "Lunch"
            '3' -> period = "Dinner"
            else -> period = ""
        }

        textPeriod.text = period
        textMealName.text = "${plannerArray[position].MealName}"
        return rowView
    }
}
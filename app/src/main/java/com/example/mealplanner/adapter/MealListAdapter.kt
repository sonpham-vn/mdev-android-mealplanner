package com.example.mealplanner.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mealplanner.R
import com.example.mealplanner.model.MealData

class MealListAdapter(private val context: Activity, private val mealArray: List<MealData>)
    : ArrayAdapter<MealData>(context, R.layout.customview_onlmeal, mealArray) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.customview_onlmeal, null, true)

        val textMealName = rowView.findViewById(R.id.meal_period) as TextView

        textMealName.text = "${mealArray[position].MealName}"
        return rowView
    }
}
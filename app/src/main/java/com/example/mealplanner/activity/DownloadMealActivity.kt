package com.example.mealplanner.activity

import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplanner.R
import com.example.mealplanner.adapter.MealListAdapter
import com.example.mealplanner.model.DatabaseHandler
import com.example.mealplanner.model.MealContentModel
import com.example.mealplanner.model.MealData
import com.example.mealplanner.model.RecipeData
import kotlinx.android.synthetic.main.activity_download_meal.*


class DownloadMealActivity : AppCompatActivity() {
    val mealContentModel = MealContentModel(this)
    var onlMealList : List<MealData> = ArrayList<MealData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_meal)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)


        mealContentModel.getOnlMeal {
            onlMealList = it
            val cardArrayContent = Array<String>(onlMealList.size) { "testing" }
            for ((index, c) in onlMealList.withIndex()) {
                cardArrayContent[index] = c.MealName.toString()
            }
            val cardListAdapter = MealListAdapter(
                    this,
                    onlMealList
            )
            listview_download.adapter = cardListAdapter
            listview_download.choiceMode = ListView.CHOICE_MODE_MULTIPLE
            listview_download.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                val listItem: SparseBooleanArray = listview_download.checkedItemPositions
                val i = 0
                if (listItem[position]) {
                    listview_download.getChildAt(position).setBackgroundColor(Color.parseColor("#FFA500"))
                } else {
                    listview_download.getChildAt(position).setBackgroundColor(Color.TRANSPARENT)
                }
            }

        }

        //Button Download
        btn_download.setOnClickListener {
            val listItem: SparseBooleanArray = listview_download.checkedItemPositions
            val dbHandler: DatabaseHandler = DatabaseHandler(this)
            for (i in 0 until listview_download.getChildCount()) {
                if (listItem[i]) {
                    val mealValue = onlMealList[i]
                    var status: Long = 0
                    status = dbHandler.insertMeal(mealValue)
                    val mealId = dbHandler.getMealId(onlMealList[i].MealOnlId)
                    val ingList = onlMealList[i].IngredientList!!
                    for (j in ingList.indices) {
                        status = dbHandler.insertIng(ingList[j])
                        val ingId = dbHandler.getIngId(ingList[j].IngOnlId)
                        status = dbHandler.insertRecipe(
                                RecipeData(MealId = mealId, IngId = ingId, Quantity = 1)
                        )
                    }

                }
            }
            onBackPressed()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
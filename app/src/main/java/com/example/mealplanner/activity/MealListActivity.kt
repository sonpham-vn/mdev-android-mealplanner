package com.example.mealplanner.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplanner.R
import com.example.mealplanner.adapter.MealListAdapter
import com.example.mealplanner.model.DatabaseHandler
import com.example.mealplanner.model.MealContentModel
import com.example.mealplanner.model.MealData
import kotlinx.android.synthetic.main.activity_meal_list.*

class MealListActivity : AppCompatActivity() {
    var mealList : List<MealData> = ArrayList<MealData>()
    val dbHandler = DatabaseHandler (this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        loadMealList()

        listview_meal.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if (intent.getBooleanExtra("fromPlanner", false)) {
                    dbHandler.insertPlanner(
                        intent.getStringExtra("plannerId"),
                        mealList[position].MealId
                        )
                    onBackPressed()
                } else {
                    Toast.makeText(this,"Show detail Recipe. COMING SOON!",Toast.LENGTH_LONG).show()
                }

            }


        //Button Download
        btn_download_meal.setOnClickListener {
            val intent = Intent(this, DownloadMealActivity::class.java)
            startActivity (intent)

        }
    }

    override fun onResume() {
        super.onResume()
        loadMealList()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun loadMealList() {
        mealList = dbHandler.getMeal()
        val cardArrayContent = Array<String>(mealList.size) { "testing" }
        for ((index, c) in mealList.withIndex()) {
            cardArrayContent[index] = c.MealName.toString()
        }
        val cardListAdapter = MealListAdapter(
                this,
                mealList
        )
        listview_meal.adapter = cardListAdapter
    }
}
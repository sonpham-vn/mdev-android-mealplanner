package com.example.mealplanner.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mealplanner.R
import com.example.mealplanner.adapter.GroceryAdapter
import com.example.mealplanner.adapter.MealListAdapter
import com.example.mealplanner.adapter.PlannerAdapter
import com.example.mealplanner.model.*
import kotlinx.android.synthetic.main.activity_download_meal.*
import kotlinx.android.synthetic.main.activity_grocery.*
import kotlinx.android.synthetic.main.activity_meal_list.*
import kotlinx.android.synthetic.main.activity_meal_list.listview_meal
import kotlinx.android.synthetic.main.activity_planner.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroceryActivity : AppCompatActivity() {
    val mealContentModel = MealContentModel(this)
    var groceryList : ArrayList<GroceryData> = ArrayList<GroceryData>()
    var groceryRawList : ArrayList<GroceryRawData> = ArrayList<GroceryRawData>()
    var dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        loadGrocery()



        //Button Download
        btn_share.setOnClickListener {
            var shareText = "Grocery List: \n"

            for (i in groceryList.indices) {
                shareText = shareText + groceryList[i].IngName + " x " +groceryList[i].TotalQuantity + "\n"
            }

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        btn_purchase.setOnClickListener {
            Toast.makeText(applicationContext,"Purchase online. COMING SOON!", Toast.LENGTH_LONG).show()
        }


    }

    override fun onResume() {
        super.onResume()
        loadGrocery()
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

    fun loadGrocery() {
        groceryList.clear()
        groceryRawList.clear()
        for (i in 0..6) {
            val date = getDateTime(i)
            val dateInString = date.toString("yyyyMMdd")
            for (j in 1..3) {
                val plannerId = dateInString + j.toString()
                val groceryRawValue = dbHandler.getGroceryRaw(plannerId)
                if (groceryRawValue.isNotEmpty()) {
                    for (k in groceryRawValue.indices) {
                        groceryRawList.add(groceryRawValue[k])
                    }
                }
            }
        }

        groceryList = groceryRawList
            .groupBy { it.IngId }
            .map {GroceryData(it.value.first().IngId,
                it.value.first().IngName,
                it.value.sumBy{it.Quantity!!})} as ArrayList<GroceryData>

        val cardListAdapter = GroceryAdapter(
            this,
            groceryList
        )

        listview_grocery.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listview_grocery.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val listItem: SparseBooleanArray = listview_grocery.checkedItemPositions
            val i = 0
            if (listItem[position]) {
                listview_grocery.getChildAt(position).setBackgroundColor(Color.parseColor("#3CB043"))
            } else {
                listview_grocery.getChildAt(position).setBackgroundColor(Color.TRANSPARENT)
            }
        }



        listview_grocery.adapter = cardListAdapter
    }

}
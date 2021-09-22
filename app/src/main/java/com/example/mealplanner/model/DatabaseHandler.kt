package com.example.mealplanner.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "MealDB"

        private val TABLE_MEAL = "Meal"
        private val KEY_MEAL_ID = "MealId"
        private val KEY_MEAL_ONLID = "MealOnlId"
        private val KEY_MEAL_NAME = "MealName"
        private val KEY_MEAL_MEMO = "MealMemo"

        private val TABLE_PLANNER = "Planner"
        private val KEY_PLANNER_ID = "PlannerId"

        private val TABLE_ING = "Ingredient"
        private val KEY_ING_ID = "IngId"
        private val KEY_ING_ONLID = "IngOnlId"
        private val KEY_ING_NAME = "IngName"

        private val TABLE_RECIPE = "Recipe"
        private val KEY_RECIPE_ID = "RecipeId"
        private val KEY_RECIPE_QUANTITY = "Quantity"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_MEAL_TABLE = ("CREATE TABLE " + TABLE_MEAL + "("
                + KEY_MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MEAL_ONLID + " TEXT UNIQUE,"
                + KEY_MEAL_NAME + " TEXT,"
                + KEY_MEAL_MEMO + " TEXT" + ")")
        db?.execSQL(CREATE_MEAL_TABLE)

        val CREATE_PLANNER_TABLE = ("CREATE TABLE " + TABLE_PLANNER + "("
                + KEY_PLANNER_ID + " TEXT PRIMARY KEY,"
                + KEY_MEAL_ID + " INT,"
                + "FOREIGN KEY (" + KEY_MEAL_ID + ") "
                + "REFERENCES " + TABLE_MEAL
                + "(" + KEY_MEAL_ID + ")"+")")
        db?.execSQL(CREATE_PLANNER_TABLE)

        val CREATE_ING_TABLE = ("CREATE TABLE " + TABLE_ING + "("
                + KEY_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ING_ONLID + " TEXT UNIQUE,"
                + KEY_ING_NAME + " TEXT" + ")")
        db?.execSQL(CREATE_ING_TABLE)

        val CREATE_RECIPE_TABLE = ("CREATE TABLE " + TABLE_RECIPE + "("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MEAL_ID + " INT,"
                + KEY_ING_ID + " INT,"
                + KEY_RECIPE_QUANTITY + " INT,"
                + "FOREIGN KEY (" + KEY_MEAL_ID + ") "
                + "REFERENCES " + TABLE_MEAL
                + "(" + KEY_MEAL_ID + "),"
                + "FOREIGN KEY (" + KEY_ING_ID + ") "
                + "REFERENCES " + TABLE_ING
                + "(" + KEY_ING_ID + "),"
                + "UNIQUE (" + KEY_MEAL_ID + "," + KEY_ING_ID + ")"
                +")")
        db?.execSQL(CREATE_RECIPE_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //create new db when upgrade //not used
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL)
        onCreate(db)
    }


    //method to insert data
    fun insertMeal(mealValue: MealData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_MEAL_ONLID, mealValue.MealOnlId)
        contentValues.put(KEY_MEAL_NAME, mealValue.MealName)
        contentValues.put(KEY_MEAL_MEMO, mealValue.MealMemo)
        // Inserting Row
        val success = db.insert(TABLE_MEAL, null, contentValues)
        db.close()
        return success
    }

    fun insertIng(ingValue: IngData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ING_ONLID, ingValue.IngOnlId)
        contentValues.put(KEY_ING_NAME, ingValue.IngName)
        // Inserting Row
        val success = db.insert(TABLE_ING, null, contentValues)
        db.close()
        return success
    }

    fun insertRecipe(recipeValue: RecipeData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_MEAL_ID, recipeValue.MealId)
        contentValues.put(KEY_ING_ID, recipeValue.IngId)
        contentValues.put(KEY_RECIPE_QUANTITY, recipeValue.Quantity)
        // Inserting Row
        val success = db.insert(TABLE_RECIPE, null, contentValues)
        db.close()
        return success
    }

    fun insertPlanner(plannerId: String?, mealId: Int?):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PLANNER_ID, plannerId)
        contentValues.put(KEY_MEAL_ID, mealId)

        // Inserting Row
        val success = db.insertWithOnConflict(TABLE_PLANNER, null, contentValues,SQLiteDatabase.CONFLICT_IGNORE)
        if (success.toInt() == -1 ) {
            db.update(TABLE_PLANNER, contentValues, "$KEY_PLANNER_ID=?",arrayOf<String?>(plannerId) )
        }
        db.close()
        return success
    }


    //method to read data
    fun getMeal():List<MealData>{
        val mealList:ArrayList<MealData> = ArrayList<MealData>()
        val selectQuery = "SELECT  * FROM $TABLE_MEAL"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return ArrayList()
        }

        // parse querydata to data class array
        var mealId: Int
        var mealOnlId: String
        var mealName: String
        var mealMemo: String

        if (cursor.moveToFirst()) {
            do {
                mealId = cursor.getInt(cursor.getColumnIndex(KEY_MEAL_ID))
                mealOnlId = cursor.getString(cursor.getColumnIndex(KEY_MEAL_ONLID))
                mealName = cursor.getString(cursor.getColumnIndex(KEY_MEAL_NAME))
                mealMemo = cursor.getString(cursor.getColumnIndex(KEY_MEAL_MEMO))
                val mealValue= MealData(
                    MealId = mealId,
                    MealOnlId = mealOnlId,
                    MealName = mealName,
                    MealMemo = mealMemo
                )
                mealList.add(mealValue)
            } while (cursor.moveToNext())
        }
        return mealList
    }

    fun getIngId(ingOnlId: String?):Int{
        var ingId = -1
        val selectQuery = "SELECT  * FROM $TABLE_ING WHERE $KEY_ING_ONLID='$ingOnlId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return -1
        }

        // parse querydata to data class array

        if (cursor.moveToFirst()) {
            do {
                ingId = cursor.getInt(cursor.getColumnIndex(KEY_ING_ID))
            } while (cursor.moveToNext())
        }
        return ingId
    }

    fun getMealId(mealOnlId: String?):Int{
        var mealId = -1
        val selectQuery = "SELECT  * FROM $TABLE_MEAL WHERE $KEY_MEAL_ONLID='$mealOnlId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return -1
        }

        // parse querydata to data class array

        if (cursor.moveToFirst()) {
            do {
                mealId = cursor.getInt(cursor.getColumnIndex(KEY_MEAL_ID))
            } while (cursor.moveToNext())
        }
        return mealId
    }

    fun getPlanner(plannerId: String):List<PlannerData>{
        val plannerList:ArrayList<PlannerData> = ArrayList<PlannerData>()
        val selectQuery = """
           SELECT $TABLE_PLANNER.$KEY_PLANNER_ID, $TABLE_PLANNER.$KEY_MEAL_ID, $TABLE_MEAL.$KEY_MEAL_NAME 
            FROM $TABLE_PLANNER INNER JOIN $TABLE_MEAL ON $TABLE_PLANNER.$KEY_MEAL_ID = $TABLE_MEAL.$KEY_MEAL_ID 
            WHERE $TABLE_PLANNER.$KEY_PLANNER_ID LIKE '$plannerId%'
        """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return ArrayList()
        }

        // parse querydata to data class array
        var plannerId: String
        var mealId: Int
        var mealName: String

        if (cursor.moveToFirst()) {
            do {
                mealId = cursor.getInt(cursor.getColumnIndex(KEY_MEAL_ID))
                plannerId = cursor.getString(cursor.getColumnIndex(KEY_PLANNER_ID))
                mealName = cursor.getString(cursor.getColumnIndex(KEY_MEAL_NAME))

                val plannerValue= PlannerData(
                    PlannerId = plannerId,
                    MealId = mealId,
                    MealName = mealName,
                )
                plannerList.add(plannerValue)
            } while (cursor.moveToNext())
        }
        return plannerList
    }

    fun getGroceryRaw(plannerId: String):List<GroceryRawData>{
        val groceryRawList:ArrayList<GroceryRawData> = ArrayList<GroceryRawData>()
        val selectQuery = """
           SELECT $TABLE_PLANNER.$KEY_PLANNER_ID, $TABLE_PLANNER.$KEY_MEAL_ID, 
           $TABLE_MEAL.$KEY_MEAL_NAME, $TABLE_RECIPE.$KEY_ING_ID, $TABLE_ING.$KEY_ING_NAME
                FROM $TABLE_ING INNER JOIN ($TABLE_RECIPE
                left outer join ($TABLE_PLANNER
                INNER JOIN $TABLE_MEAL
                ON $TABLE_PLANNER.$KEY_MEAL_ID = $TABLE_MEAL.$KEY_MEAL_ID)
                ON $TABLE_RECIPE.$KEY_MEAL_ID = $TABLE_PLANNER.$KEY_MEAL_ID)
                ON $TABLE_RECIPE.$KEY_ING_ID = $TABLE_ING.$KEY_ING_ID
                WHERE $TABLE_PLANNER.$KEY_PLANNER_ID LIKE '$plannerId%';
        """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return ArrayList()
        }

        // parse querydata to data class array
        var mPlannerId: String
        var mealId: Int
        var mealName: String
        var ingId: Int
        var ingName: String

        if (cursor.moveToFirst()) {
            do {
                mPlannerId = cursor.getString(cursor.getColumnIndex(KEY_PLANNER_ID))
                mealId = cursor.getInt(cursor.getColumnIndex("$KEY_MEAL_ID:1"))
                mealName = cursor.getString(cursor.getColumnIndex(KEY_MEAL_NAME))
                ingId = cursor.getInt(cursor.getColumnIndex(KEY_ING_ID))
                ingName = cursor.getString(cursor.getColumnIndex(KEY_ING_NAME))

                val groceryRawValue= GroceryRawData(
                    PlannerId = mPlannerId,
                    MealId = mealId,
                    MealName = mealName,
                    IngId = ingId,
                    IngName = ingName,
                    Quantity = 1
                )
                groceryRawList.add(groceryRawValue)
            } while (cursor.moveToNext())
        }
        return groceryRawList
    }

    /*
    //check card is existed to avoid duplicate
    fun isCardExisted(checkOnid: String): Boolean{
        val selectQuery = "SELECT  * FROM $TABLE_CARDS WHERE $KEY_ONID = '$checkOnid' AND $KEY_USERID = '$userId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return true
        }
        if (cursor.moveToFirst()) {
            return true
        }
        return false
    }



    //method to delete data
    fun deleteAllCard():Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_USERID, userId) // CardData UserId
        // Deleting Row
        val success = db.delete(TABLE_CARDS, "$KEY_USERID='$userId'",null)
        db.close()
        return success
    }
    */


}
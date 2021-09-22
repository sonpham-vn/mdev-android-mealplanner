package com.example.mealplanner.model

import android.content.Context
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject



class MealContentModel(context: Context) {

    private val mContext : Context =  context



    fun getOnlMeal(onSuccess:(mealArray: List<MealData>) -> Unit ) {
        var onlMealList : List<MealData> = ArrayList<MealData>()
        //fetching cards json from server
        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.get("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/recipes",
            object : AsyncHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    response: ByteArray
                ) {
                    // called when response HTTP status is "200 OK"
                    val strResponse = String(response)
                    try {
                        onlMealList = Json.decodeFromString<List<MealData>>(strResponse)

                    } catch (e: SerializationException) {
                        null
                    }
                    onSuccess(onlMealList)
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    errorResponse: ByteArray,
                    e: Throwable
                ) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    val errorResponse = String(errorResponse);
                    Toast.makeText(mContext, "Server error! $errorResponse", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }
}
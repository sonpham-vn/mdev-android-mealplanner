package com.example.mealplanner.model

import kotlinx.serialization.Serializable

@Serializable
data class MealData(val MealId: Int? = null,
                    val MealOnlId: String? = "",
                    val MealName: String? = "",
                    val MealMemo: String? = "",
                    val IngredientList: List <IngData>? = null ): java.io.Serializable

@Serializable
data class IngData(val IngId: Int? = null,
                    val IngOnlId: String? = "",
                    val IngName: String? = ""): java.io.Serializable

@Serializable
data class PlannerData(val PlannerId: String? = null,
                    val MealId: Int? = null,
                    val MealName: String? = ""): java.io.Serializable

@Serializable
data class RecipeData(val RecipeId: Int? = null,
                       val MealId: Int? = null,
                       val IngId: Int? = null,
                       val Quantity: Int? = null): java.io.Serializable

@Serializable
data class GroceryData(val IngId: Int? = null,
                       val IngName: String? = "",
                       val TotalQuantity: Int? = null): java.io.Serializable

@Serializable
data class GroceryRawData(val PlannerId: String? = null,
                       val MealId: Int? = null,
                       val MealName: String? = "",
                       val IngId: Int? = null,
                       val IngName: String? = "",
                       val Quantity: Int? = null): java.io.Serializable






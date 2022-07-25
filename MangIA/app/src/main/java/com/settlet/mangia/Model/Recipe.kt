package com.settlet.mangia.Model

class Recipe (var complexity: Int = 0,
              var description: String = "",
              var isCeliac: Boolean = false,
              var isDiabetic: Boolean = false,
              var isVegan: Boolean = false,
              var isVegetarian: Boolean = false,
              var listIngredients: List<Ingredient> = emptyList(),
              var listSteps:List<Step> = emptyList(),
              var numberTimesValored: Int = 0,
              var preparationTime: String = "",
              var publisher: String = "",
              var recipeID: String = "",
              var listImages: List<String> = emptyList(),
              var stars: Float = 0F,
              var title: String = ""){

}

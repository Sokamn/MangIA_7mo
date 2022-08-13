package com.settlet.mangia.Model

data class Recipe (var complexity: Int = 0,
              var description: String = "",
              var isCeliac: Boolean = false,
              var isDiabetic: Boolean = false,
              var isVegan: Boolean = false,
              var isVegetarian: Boolean = false,
              var listIngredients: List<Ingredient> = emptyList(),
              var listSteps:List<Step> = emptyList(),
              var preparationTime: String = "",
              var publisher: String = "",
              var recipeID: String = "",
              var listImages: List<String> = emptyList(),
              var timeLaunch: String = "",
              var title: String = ""){

}

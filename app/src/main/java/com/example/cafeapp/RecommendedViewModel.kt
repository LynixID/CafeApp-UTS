package com.example.cafeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendedViewModel : ViewModel() {
    private val _recommendedItems = MutableLiveData<List<RecommendedItem>>() // Holds the list of recommended items
    val recommendedItems: LiveData<List<RecommendedItem>> get() = _recommendedItems // Exposes recommended items as LiveData


    private val allItems = listOf(
        RecommendedItem("Nasi Kuning", "Comfort Food", "$15.99",  Categories.HeavyFood,R.drawable.yellow_rice),
        RecommendedItem("Nasi Goreng", "Comfort Food", "$22.00",  Categories.HeavyFood,R.drawable.fried_rice),
        RecommendedItem("Combo Platter", "Comfort Food", "$22.00", Categories.SNACK,R.drawable.combo_platter),
        RecommendedItem("Cireng", "Comfort Food", "$22.00",  Categories.SNACK,R.drawable.cireng),
        RecommendedItem("Tea", "Comfort Drink", "$22.00",  Categories.DRINK,R.drawable.tea),
        RecommendedItem("Ocha", "Comfort Drink", "$22.00",  Categories.DRINK,R.drawable.ocha),
    )

    init {
        _recommendedItems.value = allItems // Initializes recommended items with all available items
    }

    // Searches items based on the query string and updates the recommended items list
    fun searchItems(query: String) {
        _recommendedItems.value = filterItems(query, categoryFilter)
    }

    private var categoryFilter: String? = null // Stores the selected category filter

    // Filters the recommended items based on the selected category
    fun filterByCategory(category: String?) {
        categoryFilter = category
        _recommendedItems.value = filterItems("", category) // Updates the list based on the selected category
    }

    // Filters items based on the search query and the selected category
    private fun filterItems(query: String, category: String?): List<RecommendedItem> {
        return allItems.filter { item ->
            (category == null || item.category == category) &&
                    (query.isEmpty() || item.name.contains(query, ignoreCase = true))
        }
    }

    // Sorts the recommended items based on the specified order
    fun sortItems(order: SortOrder) {
        _recommendedItems.value = when (order) {
            SortOrder.A_TO_Z -> _recommendedItems.value?.sortedBy { it.name } // Sorts from A to Z
            SortOrder.Z_TO_A -> _recommendedItems.value?.sortedByDescending { it.name } // Sorts from Z to A
        }
    }

    enum class SortOrder {
        A_TO_Z, // Sort order from A to Z
        Z_TO_A  // Sort order from Z to A
    }
}

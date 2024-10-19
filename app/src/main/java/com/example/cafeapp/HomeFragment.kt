package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.MakanDatabase.MakanAdapter
import com.example.cafeapp.MakanDatabase.MakanViewModel


class HomeFragment : Fragment() {
    private lateinit var makanviewmodel: MakanViewModel // ViewModel for managing recommended items
    private lateinit var makanadapter: MakanAdapter // Adapter for displaying recommended items in a RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializes the ViewModel
        makanviewmodel = ViewModelProvider(this).get(MakanViewModel::class.java)

        // Sets up the RecyclerView to display recommended items
        val recyclerView = view.findViewById<RecyclerView>(R.id.recommendedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Observes changes in the recommended items and updates the RecyclerView adapter
        makanviewmodel.getAllMakans().observe(viewLifecycleOwner, Observer { items ->
            // Initialize the adapter with the list of items and a click listener
            makanadapter = MakanAdapter(items) { selectedMakan ->
                // Handle item click event here, for example:
//                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
//                    putExtra("MakanId", selectedMakan.id) // Assuming `Makan` has an `id` property
//                }
//                startActivity(intent)
            }
            recyclerView.adapter = makanadapter
        })

        // Handles click event to navigate to AllFoodActivity
        val seeAllTextView = view.findViewById<TextView>(R.id.seeAll)
        seeAllTextView.setOnClickListener {
            val intent = Intent(requireContext(), AllFoodActivity::class.java)
            startActivity(intent) // Starts AllFoodActivity to show all food items
        }

        // Sets up the search functionality for filtering items based on user input
//        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
//        searchEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                makanviewmodel.searchItems(s.toString()) // Searches items when text is changed
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        // Sets up click listeners for category filters
//        view.findViewById<ImageView>(R.id.categoryBurger).setOnClickListener { makanviewmodel.filterByCategory(Categories.HeavyFood) }
//        view.findViewById<ImageView>(R.id.categoryPizza).setOnClickListener { makanviewmodel.filterByCategory(Categories.SNACK) }
//        view.findViewById<ImageView>(R.id.categoryPasta).setOnClickListener { makanviewmodel.filterByCategory(Categories.DRINK) }
//
//        // Sets up click listener for the filter icon to show filter options
//        val filterIcon = view.findViewById<ImageView>(R.id.filterIcon)
//        filterIcon.setOnClickListener { showFilterOptions() }
//    }
//
//    // Displays filter and sort options in a dialog
//    private fun showFilterOptions() {
//        val categories = arrayOf("All", Categories.HeavyFood, Categories.SNACK, Categories.DRINK) // Available categories for filtering
//        val sortOptions = arrayOf("A-Z", "Z-A") // Sorting options
//        val filterDialog = AlertDialog.Builder(requireContext())
//        filterDialog.setTitle("Filter & Sort")
//
//        // Sets the items in the dialog for category filtering
//        filterDialog.setItems(categories) { _, which ->
//            val selectedCategory = if (which == 0) null else categories[which]
//            makanviewmodel.filterByCategory(selectedCategory) // Filters items based on selected category
//        }
//
//        // Handles cancel button in the dialog
//        filterDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
//
//        // Sets up neutral button to show sorting options
//        filterDialog.setNeutralButton("Sort") { _, _ ->
//            val sortDialog = AlertDialog.Builder(requireContext())
//            sortDialog.setTitle("Sort Options")
//            sortDialog.setItems(sortOptions) { _, which ->
//                when (which) {
//                    0 -> makanviewmodel.sortItems(MakanViewModel.SortOrder.A_TO_Z) // Sorts items A-Z
//                    1 -> makanviewmodel.sortItems(MakanViewModel.SortOrder.Z_TO_A) // Sorts items Z-A
//                }
//            }
//            sortDialog.create().show()
//        }
//
//        filterDialog.create().show() // Displays the filter dialog
    }
}

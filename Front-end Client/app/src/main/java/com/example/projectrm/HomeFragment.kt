package com.example.projectrm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var recipeRV: RecyclerView
    private lateinit var greetingsLabel: TextView
    private lateinit var recipeAdapter: RecipeAdapter
    private var recipeData = ArrayList<RecipeModel>()

    companion object {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        greetingsLabel = view.findViewById(R.id.greetingsLabel)
        greetingsLabel.text = getGreeting()
        recipeRV = view.findViewById(R.id.recipeRV)
        recipeAdapter = RecipeAdapter(recipeData)
        recipeRV.layoutManager = LinearLayoutManager(requireContext())
        recipeRV.adapter = recipeAdapter
        recipeRV.setHasFixedSize(true)
    }

    private fun getGreeting(): String {
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when {
            currentTime < 12 -> "Good morning!"
            currentTime < 17 -> "Good afternoon!"
            currentTime < 20 -> "Good evening!"
            else -> "Good night!"
        }
    }
    fun initData(){
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
 }

}
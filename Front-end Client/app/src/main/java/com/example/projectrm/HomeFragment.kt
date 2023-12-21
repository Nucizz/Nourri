package com.example.projectrm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var recipeRV: RecyclerView
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

        recipeRV = view.findViewById(R.id.recipeRV)
        recipeAdapter = RecipeAdapter(recipeData)
        recipeRV.layoutManager = LinearLayoutManager(requireContext())
        recipeRV.adapter = recipeAdapter
        recipeRV.setHasFixedSize(true)
    }

    fun initData(){
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
        recipeData.add(RecipeModel("Salted Egg Chicken", "-Ayam 200gr -Telur Asin", "-masak aja lah ", "Ayam goreng tepung dibalur dengan saur telur asin ala uncle Cals😳"))
 }

}
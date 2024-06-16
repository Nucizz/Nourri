package com.example.projectrm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var recipeRV: RecyclerView
    private lateinit var greetingsLabel: TextView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var loadingBar: ProgressBar
    private var recipeData = ArrayList<RecipeModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingBar = view.findViewById(R.id.loadingBar)
        greetingsLabel = view.findViewById(R.id.greetingsLabel)
        greetingsLabel.text = getGreeting()
        recipeRV = view.findViewById(R.id.recipeRV)
        recipeAdapter = RecipeAdapter(recipeData)
        recipeRV.layoutManager = LinearLayoutManager(requireContext())
        recipeRV.adapter = recipeAdapter
        recipeRV.setHasFixedSize(true)

        getRecipeList()
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
    private fun getRecipeList() {
        GlobalScope.launch(Dispatchers.Main) { // Launch the coroutine on the main thread
            loadingBar.visibility = View.VISIBLE
            recipeRV.visibility = View.GONE

            withContext(Dispatchers.IO) {
                val localAPIURL = "${localURL.domain}/get-all-recipe/"
                val localAPIConnection = URL(localAPIURL).openConnection() as HttpURLConnection

                try {
                    localAPIConnection.requestMethod = "GET"

                    // Get Response from localhost:3000
                    val localAPIStream = localAPIConnection.inputStream
                    val localAPIReader = BufferedReader(InputStreamReader(localAPIStream))
                    var localAPILine: String?
                    val localAPIResponse = StringBuilder()
                    while (localAPIReader.readLine().also { localAPILine = it } != null) {
                        localAPIResponse.append(localAPILine)
                    }
                    localAPIReader.close()

                    val localAPIArray = JSONArray(localAPIResponse.toString())
                    val recipeList = ArrayList<RecipeModel>()

                    for (i in 0 until localAPIArray.length()) {
                        val recipeObject = localAPIArray.getJSONObject(i)
                        val recipeTitle = recipeObject.getString("title")
                        val recipeSummary = recipeObject.getString("summary")
                        val recipeIngredients = recipeObject.getString("ingredients")
                        val recipeInstruction = recipeObject.getString("instructions")
                        val recipeRaw = recipeObject.getString("raw")

                        recipeList.add(RecipeModel(recipeTitle, recipeIngredients, recipeInstruction, recipeSummary, recipeRaw))
                    }

                    // Update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        recipeData.addAll(recipeList)
                        loadingBar.visibility = View.GONE
                        recipeRV.visibility = View.VISIBLE
                        recipeAdapter.notifyDataSetChanged()
                    }
                } finally {
                    localAPIConnection.disconnect()
                }
            }
        }
    }


}
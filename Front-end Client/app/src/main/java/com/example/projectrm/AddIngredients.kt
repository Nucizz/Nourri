package com.example.projectrm

import ApiService
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


class AddIngredients : Fragment(), DeleteInterface {

    private lateinit var loadingBar: ProgressBar
    private lateinit var addButton: FloatingActionButton
    private lateinit var generateButton: Button
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var ingredientsRV: RecyclerView
    private var ingredientsData = ArrayList<IngredientsModel>()
    private val detectedItems = HashSet<String>()

    private val localURL: String = "http://" + "192.168.1.8:3000"

    private val CAMERA_PERMISSION_CODE = 1001
    private val CAMERA_REQUEST_CODE = 1002

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingBar = view.findViewById(R.id.loadingProgressBar)
        ingredientsRV = view.findViewById(R.id.ingredientsRV)
        ingredientsAdapter = IngredientsAdapter(ingredientsData, this)
        ingredientsRV.layoutManager = LinearLayoutManager(requireContext())
        ingredientsRV.adapter = ingredientsAdapter
        ingredientsRV.setHasFixedSize(true)


        generateButton = view.findViewById(R.id.generateBtn)
        generateButton.setOnClickListener {
            getGPTresponse()
        }

        addButton = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            showPopup(addButton)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.add_menu)

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.camera -> {
                    checkCameraPermission()
                }
                R.id.add -> {
                    addManually()
                }
            }
            true
        }
        popup.show()
    }

    private fun addManually() {
        getAllItemInfo { ingredientList ->
            val ingredientNames = ingredientList.map { it.name }.toTypedArray()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Add Ingredients Manually")
                .setSingleChoiceItems(ingredientNames, -1) { dialog, index ->
                    val selectedIngredient = ingredientList[index]

                    if(!(ingredientsData.contains(selectedIngredient) || detectedItems.contains(selectedIngredient.name))) {
                        Handler(Looper.getMainLooper()).post {
                            ingredientsData.add(selectedIngredient)
                            detectedItems.add(selectedIngredient.name)
                            ingredientsAdapter.notifyDataSetChanged()
                            generateButton.visibility = View.VISIBLE
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                activity,
                                "Item already on the list!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }


    override fun onItemRemoved(position: Int) {
        detectedItems.remove(ingredientsData[position].name)
        ingredientsData.removeAt(position)
        ingredientsAdapter.notifyDataSetChanged()

        if(ingredientsData.isEmpty()) {
            generateButton.visibility = View.GONE
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
            photo?.let {
                // Convert the Bitmap to a Base64-encoded String
                val base64EncodedImage = convertBitmapToBase64(photo)

                // Upload the image to Roboflow
                println("Calling roboflow function")
                roboflow(base64EncodedImage)
            }
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun roboflow(base64EncodedImage: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                loadingBar.visibility = View.VISIBLE
                val API_KEY = "gjeimk0O8olVp9XNJqi9" // Your API Key
                val MODEL_ENDPOINT = "food-ingredients-detection-nxe34/2" // Set model endpoint (Found in Dataset URL)

                // Construct the URL
                val uploadURL = "https://detect.roboflow.com/" + MODEL_ENDPOINT + "?api_key=" + API_KEY + "&name=YOUR_IMAGE.jpg" + "&confidence=0.3" + "&overlap=0.9"

                // Http Request
                var connection: HttpURLConnection? = null
                try {
                    // Configure connection to URL
                    val url = URL(uploadURL)
                    connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    connection.setRequestProperty("Content-Length", base64EncodedImage.toByteArray().size.toString())
                    connection.setRequestProperty("Content-Language", "en-US")
                    connection.useCaches = false
                    connection.doOutput = true

                    // Send request
                    val wr = DataOutputStream(connection.outputStream)
                    wr.writeBytes(base64EncodedImage)
                    wr.close()

                    // Get Response
                    val stream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(stream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        println(line)
                        response.append(line).append('\n')
                    }
                    reader.close()

                    val roboflowJson = JSONObject(response.toString())
                    val predictionsArray = roboflowJson.getJSONArray("predictions")
                    val predictionsLen = predictionsArray.length()
                    if(predictionsLen > 0) {
                        for (i in 0 until predictionsLen) {
                            val prediction = predictionsArray.getJSONObject(i)
                            val predictedClass = prediction.getString("class")
                            val predictedConfidence = prediction.getDouble("confidence")

                            if (!detectedItems.contains(predictedClass)) {
                                if (predictedConfidence >= 0.5) {
                                    detectedItems.add(predictedClass)
                                    println("Predicted Class $i: $predictedClass")
                                    getItemInfo(predictedClass)
                                } else {
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            activity,
                                            "Identification confidence is insufficient!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        loadingBar.visibility = View.GONE
                                    }
                                }

                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                activity,
                                "Couldn't identify ingredients!",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadingBar.visibility = View.GONE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadingBar.visibility = View.GONE
                } finally {
                    connection?.disconnect()
                    loadingBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getItemInfo(detectedIngredient: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val localAPIURL = "$localURL/get-ingredient-info/$detectedIngredient"
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

                // Parse JSON response from localhost:3000
                val localAPIJson = JSONObject(localAPIResponse.toString())
                val ingredientName = localAPIJson.getString("name")
                val ingredientCcal = localAPIJson.getString("ccal")

                // Update RecyclerView with the retrieved data
                withContext(Dispatchers.Main) {
                    ingredientsData.add(IngredientsModel(ingredientName, ingredientCcal.toFloat()))
                    ingredientsAdapter.notifyDataSetChanged()
                    generateButton.visibility = View.VISIBLE
                }

            } finally {
                localAPIConnection.disconnect()
            }
        }
    }

    private fun getAllItemInfo(callback: (List<IngredientsModel>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            loadingBar.visibility = View.VISIBLE
            val localAPIURL = "$localURL/get-ingredient/"
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
                val ingredientList = ArrayList<IngredientsModel>()

                for (i in 0 until localAPIArray.length()) {
                    val ingredientObject = localAPIArray.getJSONObject(i)
                    val ingredientName = ingredientObject.getString("name")
                    val ingredientCcal = ingredientObject.getString("ccal")

                    withContext(Dispatchers.Main) {
                        ingredientList.add(IngredientsModel(ingredientName, ingredientCcal.toFloat()))
                    }
                }

                withContext(Dispatchers.Main) {
                    callback(ingredientList)
                    loadingBar.visibility = View.GONE
                }

            } finally {
                localAPIConnection.disconnect()
                loadingBar.visibility = View.GONE
            }
        }
    }

    private fun getGPTresponse() {
        loadingBar.visibility = View.VISIBLE
        val retrofit = Retrofit.Builder()
            .baseUrl(localURL) // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Adjust the connect timeout as needed
                    .readTimeout(30, TimeUnit.SECONDS)    // Adjust the read timeout as needed
                    .writeTimeout(30, TimeUnit.SECONDS)   // Adjust the write timeout as needed
                    .build()
            )
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.postIngredients(ingredientsData)

        call.enqueue(object : Callback<RecipeModel> {
            override fun onResponse(call: Call<RecipeModel>, response: Response<RecipeModel>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val intent = Intent(activity, RecipeDetailsActivity::class.java)
                    loadingBar.visibility = View.GONE
                    intent.putExtra("title", responseBody?.title)
                    intent.putExtra("summary", responseBody?.summary)
                    intent.putExtra("ingredients", responseBody?.ingredients)
                    intent.putExtra("instruction", responseBody?.instruction)
                    intent.putExtra("raw", responseBody?.raw)
                    startActivity(intent)
                } else {
                    handleApiError(response)
                    loadingBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<RecipeModel>, t: Throwable) {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                loadingBar.visibility = View.GONE
            }
        })
    }

    private fun handleApiError(response: Response<RecipeModel>) {
        // Handle API error
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        // Show error message to the user or log it
        println("API Error: $errorMessage")
    }

}

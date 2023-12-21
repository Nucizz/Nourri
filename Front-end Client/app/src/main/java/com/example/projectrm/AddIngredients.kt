package com.example.projectrm

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class AddIngredients : Fragment(), DeleteInterface {

    private lateinit var addButton: FloatingActionButton
    private lateinit var generateButton: Button
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var ingredientsRV: RecyclerView
    private var ingredientsData = ArrayList<IngredientsModel>()
    private val detectedItems = HashSet<String>()



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

        ingredientsRV = view.findViewById(R.id.ingredientsRV)
        ingredientsAdapter = IngredientsAdapter(ingredientsData, this)
        ingredientsRV.layoutManager = LinearLayoutManager(requireContext())
        ingredientsRV.adapter = ingredientsAdapter
        ingredientsRV.setHasFixedSize(true)

        generateButton = view.findViewById(R.id.generateBtn)
        generateButton.setOnClickListener {
            // TODO: Implement logic for generating recipe based on ingredientsData
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
        val view1: View = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
        val ingredientsTextField: EditText = view1.findViewById(R.id.ingredientsTextField)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        with(alertDialogBuilder) {
            setTitle("Add Ingredients Manually")
            setMessage("13 ingredients available")
            setView(view1)
            setPositiveButton("Done") { _, _ ->
                val item = ingredientsTextField.text.toString()
//
                ingredientsData.add(IngredientsModel(item, 0f))
                ingredientsAdapter.notifyDataSetChanged()

//                makeLocalAPIRequest(item)
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }
        val builder = alertDialogBuilder.create()
        builder.show()
    }

    override fun onItemRemoved(position: Int) {
        detectedItems.remove(ingredientsData[position].name)
        ingredientsData.removeAt(position)
        ingredientsAdapter.notifyDataSetChanged()
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

                            if (!detectedItems.contains(predictedClass) && predictedConfidence >= 0.5) {
                                detectedItems.add(predictedClass)
                                println("Predicted Class $i: $predictedClass")
                                makeLocalAPIRequest(predictedClass)
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                activity,
                                "Couldn't identify ingredients!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connection?.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun makeLocalAPIRequest(detectedIngredient: String) {
        trustAllCertificates()
        val localAPIURL = "https://172.20.10.3:3000/get-ingredient-info/$detectedIngredient"
        val localAPIConnection = URL(localAPIURL).openConnection() as HttpURLConnection

        try {
            localAPIConnection.requestMethod = "GET"

//            printLn("GET $detectedIngredient from local host.")

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
            GlobalScope.launch(Dispatchers.Main) {
                ingredientsData.add(IngredientsModel(ingredientName, ingredientCcal.toFloat()))
                ingredientsAdapter.notifyDataSetChanged()
            }

        } finally {
            localAPIConnection.disconnect()
        }
    }

    private fun trustAllCertificates() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)

            // Create an all-trusting host name verifier
            HttpsURLConnection.setDefaultHostnameVerifier(HostnameVerifier { _, _ -> true })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

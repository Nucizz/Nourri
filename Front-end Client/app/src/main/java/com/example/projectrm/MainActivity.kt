package com.example.projectrm

import ApiService
import android.os.Bundle
import android.view.WindowInsetsAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //test backend

//        val call: Call<List<RecipeModel>> = ApiService.getData()
//
//        call.enqueue(object : Callback<List<YourDataModel>> {
//            override fun onResponse(call: Call<List<YourDataModel>>, response: Response<List<YourDataModel>>) {
//                // Handle the successful response here
//                val data = response.body()
//                // Process the data as needed
//            }
//
//            override fun onFailure(call: Call<List<YourDataModel>>, t: Throwable) {
//                // Handle the failure here
//                t.printStackTrace()
//            }
//        })


        // test backend

        bottomNav = findViewById(R.id.homeBottomNavbar)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.add -> {
                    loadFragment(AddIngredients())
                    true
                }

                else -> {
                    false
                }
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }


}
package com.example.projectrm

import ApiService
import android.os.Bundle
import android.view.WindowInsetsAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView
    private lateinit var mainConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())

        // test backend
        mainConstraintLayout = findViewById(R.id.mainConstraintLayout)
        bottomNav = findViewById(R.id.homeBottomNavbar)
        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    setHorizontalBias(0.5f)
                    true
                }
                R.id.add -> {
                    loadFragment(AddIngredients())
                    setHorizontalBias(0.0f)
                    true
                }
                R.id.about -> {
                    loadFragment(AboutFragment())
                    setHorizontalBias(0.5f)
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
    private fun setHorizontalBias(bias: Float) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainConstraintLayout)  // Replace with your actual ConstraintLayout reference
        constraintSet.setHorizontalBias(R.id.homeBottomNavbar, bias)
        constraintSet.applyTo(mainConstraintLayout)  // Replace with your actual ConstraintLayout reference
    }

}
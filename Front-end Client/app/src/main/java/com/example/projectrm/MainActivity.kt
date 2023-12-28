package com.example.projectrm

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : FragmentActivity() {

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
        constraintSet.clone(mainConstraintLayout)
        constraintSet.setHorizontalBias(R.id.homeBottomNavbar, bias)

        // Apply transition with animation
        val transition = androidx.transition.AutoTransition()
        transition.duration = 300 // Set the duration of the animation in milliseconds
        TransitionManager.beginDelayedTransition(mainConstraintLayout, transition)

        constraintSet.applyTo(mainConstraintLayout)
    }

}
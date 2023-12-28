package com.example.projectrm

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import com.example.projectrm.R.*

class RecipeDetailsActivity : Activity() {
    private lateinit var backBtn: ImageButton

    private lateinit var headerLabel: TextView
    private lateinit var titleLabel: TextView
    private lateinit var summaryLabel: TextView
    private lateinit var ingredientLabel: TextView
    private lateinit var instructionLabel: TextView
    private lateinit var rawLabel: TextView

    private var format: Boolean = true
    private lateinit var formattedLayout: ScrollView
    private lateinit var unformattedLayout: ScrollView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_recipe_details)

        backBtn = findViewById(R.id.backButton)
        headerLabel = findViewById(R.id.headerBtn)
        titleLabel = findViewById(R.id.titleLabel)
        summaryLabel = findViewById(R.id.summaryLabel)
        ingredientLabel = findViewById(R.id.ingredientLabel)
        instructionLabel = findViewById(R.id.instructionLabel)
        rawLabel = findViewById(R.id.rawLabel)
        formattedLayout = findViewById(R.id.formattedLayout)
        unformattedLayout = findViewById(R.id.unformattedLayout)

        val intent = intent
        titleLabel.text = intent.getStringExtra("title")
        summaryLabel.text = intent.getStringExtra("summary")
        ingredientLabel.text = intent.getStringExtra("ingredients")
        instructionLabel.text = intent.getStringExtra("instruction")
        rawLabel.text = intent.getStringExtra("raw")

        backBtn.setOnClickListener{
            finish()
        }

        headerLabel.setOnClickListener {
            format = !format
            if (format) {
                formattedLayout.visibility = View.VISIBLE
                unformattedLayout.visibility = View.GONE
            } else {
                formattedLayout.visibility = View.GONE
                unformattedLayout.visibility = View.VISIBLE
            }
        }

    }
}
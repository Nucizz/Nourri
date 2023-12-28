package com.example.projectrm

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(private val recipeList: List<RecipeModel>): RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipes_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipeViewModel = recipeList[position]
        holder.recipeTitle.text = recipeViewModel.title
        holder.recipeSummary.text = recipeViewModel.summary
        holder.recipeCard.setOnClickListener{
            val context = holder.itemView.context  // Obtain the context from the itemView
            val intent = Intent(context, RecipeDetailsActivity::class.java)
            intent.putExtra("title", recipeViewModel.title)
            intent.putExtra("summary", recipeViewModel.summary)
            intent.putExtra("ingredients", recipeViewModel.ingredients)
            intent.putExtra("instruction", recipeViewModel.instruction)
            intent.putExtra("raw", recipeViewModel.raw)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeSummary: TextView = itemView.findViewById(R.id.recipeSummary)
        val recipeCard: LinearLayout = itemView.findViewById(R.id.recipe_list)
    }

}
package com.example.projectrm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter(private val List: MutableList<IngredientsModel>, private val deleteListener: DeleteInterface): RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredients_list, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredientsViewModel = List[position]
        holder.ingredientsName.text = ingredientsViewModel.name
        holder.ingredientsCcal.text = (ingredientsViewModel.ccal * 100).toString() + " ccal/oz"

        holder.deleteButton.setOnClickListener {
//            List.removeAt(position)
            deleteListener.onItemRemoved(position = position)
//            notifyDataSetChanged()


        }

    }

    override fun getItemCount(): Int {
        return List.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientsName: TextView = itemView.findViewById(R.id.addIngredientsName)
        val ingredientsCcal: TextView = itemView.findViewById(R.id.addIngredientsCcal)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteBtn)
    }
}


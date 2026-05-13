package com.example.kaagada.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaagada.R
import com.example.kaagada.data.model.Proverb

// This is the Adapter required by Requirement 2.2
class ProverbAdapter(private val proverbs: List<Proverb>) :
    RecyclerView.Adapter<ProverbAdapter.ProverbViewHolder>() {

    // ViewHolder holds the references to the UI elements in item_proverb.xml
    class ProverbViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val proverbTextView: TextView = view.findViewById(R.id.proverbText)
    }

    // This "inflates" the XML layout for each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProverbViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_proverb, parent, false)
        return ProverbViewHolder(view)
    }

    // Binds the data from our list to the actual TextView
    override fun onBindViewHolder(holder: ProverbViewHolder, position: Int) {
        holder.proverbTextView.text = proverbs[position].text
    }

    // Tells the RecyclerView how many items are in the list
    override fun getItemCount(): Int = proverbs.size
}
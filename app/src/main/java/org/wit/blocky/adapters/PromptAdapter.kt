package org.wit.blocky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.prompt_card.view.*
import org.wit.blocky.R

class PromptAdapter(
    private val prompts: List<String>
) :
    RecyclerView.Adapter<PromptAdapter.MainHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.prompt_card, parent, false))
    }

    override fun getItemCount(): Int = prompts.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val prompt = prompts[holder.adapterPosition]
        holder.itemView.prompt.text = prompt
    }

    class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
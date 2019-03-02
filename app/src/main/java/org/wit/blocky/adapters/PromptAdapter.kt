package org.wit.blocky.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.prompt_card.view.*
import org.wit.blocky.R
import org.wit.blocky.views.entry.EntryViewModel

class PromptAdapter(
    private val prompts: List<String>,
    private val viewModel: EntryViewModel
) :
    RecyclerView.Adapter<PromptAdapter.MainHolder>() {

    val entry = viewModel.entry

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.prompt_card, parent, false)
        return MainHolder(v, TextListener())
    }

    override fun getItemCount(): Int = prompts.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val prompt = prompts[holder.adapterPosition]
        holder.itemView.prompt.text = prompt
        holder.itemView.answer.setText(entry.prompts[prompt])

        holder.textListener.updatePosition(holder.adapterPosition)
        holder.itemView.answer.addTextChangedListener(holder.textListener)
    }

    class MainHolder(itemView: View, val textListener: TextListener) :
        RecyclerView.ViewHolder(itemView)

    inner class TextListener : TextWatcher {
        private var position: Int = 0

        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            val prompt = prompts[position]
            entry.prompts[prompt] = charSequence.toString()
        }

        override fun afterTextChanged(editable: Editable) {}
    }
}


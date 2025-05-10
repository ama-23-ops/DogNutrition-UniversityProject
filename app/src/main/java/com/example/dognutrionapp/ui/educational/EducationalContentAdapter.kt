package com.example.dognutrionapp.ui.educational

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dognutrionapp.databinding.ItemEducationalContentBinding
import com.example.dognutrionapp.ui.models.EducationalContent

class EducationalContentAdapter(private val contentList: List<EducationalContent>) :
    RecyclerView.Adapter<EducationalContentAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemEducationalContentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEducationalContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = contentList[position]
        holder.binding.tvTitle.text = content.title
        holder.binding.tvDescription.text = content.description
    }

    override fun getItemCount() = contentList.size
}

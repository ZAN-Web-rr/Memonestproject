package com.memonest.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.memonest.app.R
import com.memonest.app.data.model.JournalEntry
import com.memonest.app.databinding.ItemEntryBinding
import com.memonest.app.domain.DateFormatter

class EntriesAdapter(
    private val onClick: (JournalEntry) -> Unit
) : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    private val items = mutableListOf<JournalEntry>()

    fun submitList(newItems: List<JournalEntry>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class EntryViewHolder(
        private val binding: ItemEntryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JournalEntry) {
            binding.tvTitle.text = item.title
            binding.tvExcerpt.text = item.content
            binding.tvDate.text = buildString {
                append(DateFormatter.format(item.updatedAt))
                if (item.photoUri.isNotBlank()) {
                    append("  •  ")
                    append(binding.root.context.getString(R.string.photo_attached))
                }
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}

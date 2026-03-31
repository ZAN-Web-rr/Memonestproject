package com.memonest.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.memonest.app.R
import com.memonest.app.data.model.JournalEntry
import com.memonest.app.databinding.ActivityEntriesBinding
import kotlinx.coroutines.launch

class EntriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntriesBinding
    private lateinit var adapter: EntriesAdapter
    private var allEntries = emptyList<JournalEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EntriesAdapter { entry ->
            startActivity(Intent(this, EntryDetailActivity::class.java).putExtra(EXTRA_ENTRY_ID, entry.id))
        }

        binding.rvEntries.layoutManager = LinearLayoutManager(this)
        binding.rvEntries.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, EntryEditorActivity::class.java))
        }

        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_profile) {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            } else {
                false
            }
        }

        binding.etSearch.doAfterTextChanged { renderEntries() }
        binding.filterGroup.setOnCheckedStateChangeListener { _, _ -> renderEntries() }
    }

    override fun onResume() {
        super.onResume()
        loadEntries()
    }

    private fun loadEntries() {
        lifecycleScope.launch {
            val ownerId = AppContainer.sessionManager(this@EntriesActivity).currentSession().ownerId
            allEntries = AppContainer.repository(this@EntriesActivity).getEntries(ownerId)
            renderEntries()
        }
    }

    private fun renderEntries() {
        val query = binding.etSearch.text?.toString().orEmpty().trim().lowercase()
        val filteredEntries = allEntries.filter { entry ->
            matchesQuery(entry, query) && matchesFilter(entry)
        }

        adapter.submitList(filteredEntries)
        binding.tvEmpty.visibility = if (filteredEntries.isEmpty()) View.VISIBLE else View.GONE
        binding.tvEmpty.text = if (allEntries.isEmpty()) {
            getString(R.string.empty_entries)
        } else {
            getString(R.string.empty_search_results)
        }
    }

    private fun matchesQuery(entry: JournalEntry, query: String): Boolean {
        if (query.isBlank()) return true
        val searchableText = listOf(entry.title, entry.content, entry.tags).joinToString(" ").lowercase()
        return searchableText.contains(query)
    }

    private fun matchesFilter(entry: JournalEntry): Boolean {
        return when (binding.filterGroup.checkedChipId) {
            R.id.chipPhotos -> entry.photoUri.isNotBlank()
            R.id.chipTagged -> entry.tags.isNotBlank()
            else -> true
        }
    }

    companion object {
        const val EXTRA_ENTRY_ID = "entry_id"
    }
}

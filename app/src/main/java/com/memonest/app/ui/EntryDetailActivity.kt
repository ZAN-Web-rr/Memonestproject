package com.memonest.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.memonest.app.R
import com.memonest.app.data.model.JournalEntry
import com.memonest.app.databinding.ActivityEntryDetailBinding
import com.memonest.app.domain.DateFormatter
import kotlinx.coroutines.launch

class EntryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryDetailBinding
    private var entryId: Long = 0L
    private var currentEntry: JournalEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        entryId = intent.getLongExtra(EntriesActivity.EXTRA_ENTRY_ID, 0L)
        if (entryId == 0L) finish()

        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this, EntryEditorActivity::class.java).putExtra(EntriesActivity.EXTRA_ENTRY_ID, entryId))
        }

        binding.btnDelete.setOnClickListener { askDelete() }
    }

    override fun onResume() {
        super.onResume()
        loadEntry()
    }

    private fun loadEntry() {
        lifecycleScope.launch {
            val session = AppContainer.sessionManager(this@EntryDetailActivity).currentSession()
            val entry = AppContainer.repository(this@EntryDetailActivity).getEntry(entryId, session.ownerId)
            if (entry == null) {
                finish()
                return@launch
            }
            currentEntry = entry
            bind(entry)
        }
    }

    private fun bind(entry: JournalEntry) {
        binding.tvTitle.text = entry.title
        binding.tvMeta.text = getString(R.string.last_updated, DateFormatter.format(entry.updatedAt))
        binding.tvTags.text = if (entry.tags.isBlank()) "" else "#" + entry.tags.replace(",", " #")
        binding.tvContent.text = entry.content
        if (entry.photoUri.isBlank()) {
            binding.ivEntryPhoto.visibility = View.GONE
            binding.tvPhotoLabel.visibility = View.GONE
        } else {
            binding.ivEntryPhoto.visibility = View.VISIBLE
            binding.tvPhotoLabel.visibility = View.VISIBLE
            binding.ivEntryPhoto.setImageURI(Uri.parse(entry.photoUri))
        }
    }

    private fun askDelete() {
        AlertDialog.Builder(this)
            .setMessage(R.string.confirm_delete)
            .setPositiveButton(R.string.yes) { _, _ -> deleteEntry() }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun deleteEntry() {
        lifecycleScope.launch {
            val session = AppContainer.sessionManager(this@EntryDetailActivity).currentSession()
            val deleted = AppContainer.repository(this@EntryDetailActivity).delete(entryId, session.ownerId)
            if (deleted) {
                Toast.makeText(this@EntryDetailActivity, R.string.deleted_success, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}

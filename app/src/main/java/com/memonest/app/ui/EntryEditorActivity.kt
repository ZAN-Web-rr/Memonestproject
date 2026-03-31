package com.memonest.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.memonest.app.R
import com.memonest.app.data.model.JournalEntry
import com.memonest.app.databinding.ActivityEntryEditorBinding
import kotlinx.coroutines.launch

class EntryEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryEditorBinding
    private var entryId: Long = 0L
    private var createdAt: Long = 0L
    private var selectedPhotoUri: String = ""

    private val photoPicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@registerForActivityResult

        try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) {
            // Keep going even if the provider does not support a persistable grant.
        }

        selectedPhotoUri = uri.toString()
        renderPhotoPreview()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        entryId = intent.getLongExtra(EntriesActivity.EXTRA_ENTRY_ID, 0L)
        configureToolbar()
        if (entryId != 0L) {
            binding.toolbar.title = getString(R.string.edit)
            binding.btnSave.text = getString(R.string.update)
            loadEntry()
        } else {
            renderPhotoPreview()
        }

        binding.btnSave.setOnClickListener {
            saveEntry()
        }

        binding.btnAddPhoto.setOnClickListener {
            photoPicker.launch(arrayOf("image/*"))
        }

        binding.btnRemovePhoto.setOnClickListener {
            selectedPhotoUri = ""
            renderPhotoPreview()
        }
    }

    private fun configureToolbar() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadEntry() {
        lifecycleScope.launch {
            val session = AppContainer.sessionManager(this@EntryEditorActivity).currentSession()
            val entry = AppContainer.repository(this@EntryEditorActivity).getEntry(entryId, session.ownerId) ?: return@launch
            createdAt = entry.createdAt
            binding.etTitle.setText(entry.title)
            binding.etContent.setText(entry.content)
            binding.etTags.setText(entry.tags)
            selectedPhotoUri = entry.photoUri
            renderPhotoPreview()
        }
    }

    private fun saveEntry() {
        lifecycleScope.launch {
            val session = AppContainer.sessionManager(this@EntryEditorActivity).currentSession()
            val useCase = AppContainer.saveEntryUseCase(this@EntryEditorActivity)
            val result = useCase(
                JournalEntry(
                    id = entryId,
                    ownerId = session.ownerId,
                    title = binding.etTitle.text?.toString().orEmpty(),
                    content = binding.etContent.text?.toString().orEmpty(),
                    tags = binding.etTags.text?.toString().orEmpty(),
                    photoUri = selectedPhotoUri,
                    createdAt = createdAt,
                    updatedAt = 0L
                )
            )

            if (result.isSuccess) {
                Toast.makeText(this@EntryEditorActivity, R.string.saved_success, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@EntryEditorActivity, R.string.validation_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun renderPhotoPreview() {
        if (selectedPhotoUri.isBlank()) {
            binding.ivPhotoPreview.visibility = View.GONE
            binding.tvPhotoStatus.text = getString(R.string.no_photo_selected)
            binding.btnRemovePhoto.visibility = View.GONE
            binding.btnAddPhoto.text = getString(R.string.add_photo)
            return
        }

        binding.ivPhotoPreview.visibility = View.VISIBLE
        binding.ivPhotoPreview.setImageURI(Uri.parse(selectedPhotoUri))
        binding.tvPhotoStatus.text = getString(R.string.photo_attached)
        binding.btnRemovePhoto.visibility = View.VISIBLE
        binding.btnAddPhoto.text = getString(R.string.change_photo)
    }
}

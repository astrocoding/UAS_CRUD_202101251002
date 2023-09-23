package com.belajar.uas_crud_202101251002

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.belajar.uas_crud_202101251002.adapter.MahasiswaAdapter
import com.belajar.uas_crud_202101251002.databinding.ActivityMhsAddUpdateBinding
import com.belajar.uas_crud_202101251002.databinding.ActivityMhsListBinding
import com.belajar.uas_crud_202101251002.db.MahasiswaHelper
import com.belajar.uas_crud_202101251002.entity.Mahasiswa

class MhsListActivity : AppCompatActivity() {
    private var mahasiswa: Mahasiswa? = null
    private lateinit var binding: ActivityMhsListBinding


    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMhsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Detail Mahasiswa"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        mahasiswa = intent.getParcelableExtra(MhsListActivity.EXTRA_NOTE)
        val selectedNote = intent.getParcelableExtra<Mahasiswa>(EXTRA_NOTE)

        if (mahasiswa != null) {
            showMahasiswaDetails()
        } else {
            Toast.makeText(this, "Tidak ada data yang diterima", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showMahasiswaDetails() {
        binding.apply {
            txtNim.text = mahasiswa?.nim
            txtNama.text = mahasiswa?.nama
            txtJurusan.text = mahasiswa?.jurusan
            txtHobi.text = mahasiswa?.hobi
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
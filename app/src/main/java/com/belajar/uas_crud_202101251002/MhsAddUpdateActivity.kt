package com.belajar.uas_crud_202101251002

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.belajar.uas_crud_202101251002.databinding.ActivityMhsAddUpdateBinding
import com.belajar.uas_crud_202101251002.db.DatabaseContract
import com.belajar.uas_crud_202101251002.db.MahasiswaHelper
import com.belajar.uas_crud_202101251002.entity.Mahasiswa
import java.text.SimpleDateFormat
import java.util.*

class MhsAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var isEdit = false
    private var mahasiswa: Mahasiswa? = null
    private var position: Int = 0
    private lateinit var mahasiswaHelper: MahasiswaHelper
    private lateinit var binding: ActivityMhsAddUpdateBinding

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMhsAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mahasiswaHelper = MahasiswaHelper.getInstance(applicationContext)
        mahasiswaHelper.open()

        mahasiswa = intent.getParcelableExtra(EXTRA_NOTE)
        if (mahasiswa != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            mahasiswa = Mahasiswa()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Edit Biodata"
            btnTitle = "Update"
            mahasiswa?.let {
                binding.edtNim.setText(it.nim)
                binding.edtNama.setText(it.nama)
                binding.edtJurusan.setText(it.jurusan)
                binding.edtHobi.setText(it.hobi)
            }
        } else {
            actionBarTitle = "Tambah Biodata"
            btnTitle = "Simpan"
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSubmit.text = btnTitle
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val nim = binding.edtNim.text.toString().trim()
            val nama = binding.edtNama.text.toString().trim()
            val jurusan = binding.edtJurusan.text.toString().trim()
            val hobi = binding.edtHobi.text.toString().trim()

            if (nim.isEmpty()) {
                binding.edtNim.error = "Input tidak boleh kosong!"
                return
            }
            mahasiswa?.nim = nim
            mahasiswa?.nama = nama
            mahasiswa?.jurusan = jurusan
            mahasiswa?.hobi = hobi

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, mahasiswa)
            intent.putExtra(EXTRA_POSITION, position)
            val values = ContentValues()
            values.put(DatabaseContract.MahasiswaColumns.NIM, nim)
            values.put(DatabaseContract.MahasiswaColumns.NAMA, nama)
            values.put(DatabaseContract.MahasiswaColumns.JURUSAN, jurusan)
            values.put(DatabaseContract.MahasiswaColumns.HOBI, hobi)

            if (isEdit) {
                val result = mahasiswaHelper.update(mahasiswa?.id.toString(), values)
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@MhsAddUpdateActivity, "Gagal memperbaharui data!", Toast.LENGTH_SHORT).show()
                }
            } else {
                mahasiswa?.date = getCurrentDate()
                values.put(DatabaseContract.MahasiswaColumns.DATE, getCurrentDate())
                val result = mahasiswaHelper.insert(values)

                if (result > 0) {
                    mahasiswa?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@MhsAddUpdateActivity, "Gagal menambahkan data!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Data"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = mahasiswaHelper.deleteById(mahasiswa?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@MhsAddUpdateActivity, "Gagal menghapus data!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
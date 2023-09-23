package com.belajar.uas_crud_202101251002

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.uas_crud_202101251002.adapter.MahasiswaAdapter
import com.belajar.uas_crud_202101251002.databinding.ActivityMainBinding
import com.belajar.uas_crud_202101251002.db.MahasiswaHelper
import com.belajar.uas_crud_202101251002.entity.Mahasiswa
import com.belajar.uas_crud_202101251002.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MahasiswaAdapter

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                MhsAddUpdateActivity.RESULT_ADD -> {
                    val mahasiswa = result.data?.getParcelableExtra<Mahasiswa>(MhsAddUpdateActivity.EXTRA_NOTE) as Mahasiswa
                    adapter.addItem(mahasiswa)
                    binding.rvMahasiswa.smoothScrollToPosition(adapter.itemCount - 1)
                    showPopUp("Satu item berhasil ditambahkan!")
                }
                MhsAddUpdateActivity.RESULT_UPDATE -> {
                    val mahasiswa = result.data?.getParcelableExtra<Mahasiswa>(MhsAddUpdateActivity.EXTRA_NOTE) as Mahasiswa
                    val position = result?.data?.getIntExtra(MhsAddUpdateActivity.EXTRA_POSITION, 0) as Int
                    adapter.updateItem(position, mahasiswa)
                    binding.rvMahasiswa.smoothScrollToPosition(position)
                    showPopUp("Satu item berhasil diubah!")
                }
                MhsAddUpdateActivity.RESULT_DELETE -> {
                    val position = result?.data?.getIntExtra(MhsAddUpdateActivity.EXTRA_POSITION, 0) as Int
                    adapter.removeItem(position)
                    showPopUp("Satu item berhasil dihapus!")
                }
            }
        }
    }
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Mahasiswa App"
        binding.rvMahasiswa.layoutManager = LinearLayoutManager(this)
        binding.rvMahasiswa.setHasFixedSize(true)
        adapter = MahasiswaAdapter(object : MahasiswaAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedNote: Mahasiswa?, position: Int?) {
                val intent = Intent(this@MainActivity, MhsAddUpdateActivity::class.java)
                intent.putExtra(MhsAddUpdateActivity.EXTRA_NOTE, selectedNote)
                intent.putExtra(MhsAddUpdateActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)

            }
        })

        binding.rvMahasiswa.adapter = adapter
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, MhsAddUpdateActivity::class.java)
            resultLauncher.launch(intent)
        }

        if (intent.hasExtra("HAS_INSTANCE_STATE") && savedInstanceState != null) {
            val list = savedInstanceState.getParcelableArrayList<Mahasiswa>(EXTRA_STATE)
            if (list != null) {
                adapter.listMahasiswa = list
            }
        } else {
            loadMhsAsync()
        }
    }

    private fun loadMhsAsync() {
        lifecycleScope.launch {
            Handler().postDelayed({
                binding.progressbar!!.setVisibility(View.INVISIBLE)
            },1800/* 2 detik */)
            Handler().postDelayed({
                binding.rvMahasiswa!!.setVisibility(View.VISIBLE)
            },2000)

            val mahasiswaHelper = MahasiswaHelper.getInstance(applicationContext)
            mahasiswaHelper.open()
            val deferredMhs = async(Dispatchers.IO) {
                val cursor = mahasiswaHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val mahasiswa = deferredMhs.await()

            if (mahasiswa.size > 0) {
                adapter.listMahasiswa = mahasiswa
                adapter.notifyDataSetChanged()
            } else {
                adapter.listMahasiswa = ArrayList()
                showPopUp("Tidak ada data saat ini!")
            }
            mahasiswaHelper.close()

        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMahasiswa)
    }
    private fun showPopUp(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0) // Mengatur posisi toast
        toast.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this@MainActivity, AboutPage::class.java)
                startActivity(intent)
            }
            R.id.action_version -> {
                val intent = Intent(this@MainActivity, Version::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
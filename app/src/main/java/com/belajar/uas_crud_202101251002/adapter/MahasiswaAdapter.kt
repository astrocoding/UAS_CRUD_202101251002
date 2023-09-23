package com.belajar.uas_crud_202101251002.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.belajar.uas_crud_202101251002.MainActivity
import com.belajar.uas_crud_202101251002.MhsAddUpdateActivity
import com.belajar.uas_crud_202101251002.MhsListActivity
import com.belajar.uas_crud_202101251002.R
import com.belajar.uas_crud_202101251002.databinding.ItemMahasiswaBinding
import com.belajar.uas_crud_202101251002.entity.Mahasiswa

class MahasiswaAdapter(private val onItemClickCallback: OnItemClickCallback) : RecyclerView.Adapter<MahasiswaAdapter.MhsViewHolder>() {
    private lateinit var binding: ItemMahasiswaBinding

    var listMahasiswa = ArrayList<Mahasiswa>()
        set(listMahasiswa) {
            if (listMahasiswa.size > 0) {
                this.listMahasiswa.clear()
            }
            this.listMahasiswa.addAll(listMahasiswa)
        }

    fun addItem(mahasiswa: Mahasiswa) {
        this.listMahasiswa.add(mahasiswa)
        notifyItemInserted(this.listMahasiswa.size - 1)
    }

    fun updateItem(position: Int, mahasiswa: Mahasiswa) {
        this.listMahasiswa[position] = mahasiswa
        notifyItemChanged(position, mahasiswa)
    }

    fun removeItem(position: Int) {
        this.listMahasiswa.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMahasiswa.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MhsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        binding = ItemMahasiswaBinding.bind(view)
        return MhsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MhsViewHolder, position: Int) {
        val mahasiswa = listMahasiswa[position]
        holder.bind(listMahasiswa[position])
        holder.bind(mahasiswa)

        holder.binding.actionDetail.setOnClickListener {
            val intent = Intent(holder.itemView.context, MhsListActivity::class.java)
            intent.putExtra(MhsListActivity.EXTRA_NOTE, mahasiswa)
            intent.putExtra(MhsListActivity.EXTRA_POSITION, position)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = this.listMahasiswa.size

    inner class MhsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMahasiswaBinding.bind(itemView)
        fun bind(mahasiswa: Mahasiswa) {
            binding.tvItemNim.text = mahasiswa.nim
            binding.tvItemNama.text = mahasiswa.nama
            binding.tvItemDate.text = mahasiswa.date
            binding.cvItemMahasiswa.setOnClickListener {
                onItemClickCallback.onItemClicked(mahasiswa, adapterPosition)
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(selectedNote: Mahasiswa?, position: Int?)
    }
}
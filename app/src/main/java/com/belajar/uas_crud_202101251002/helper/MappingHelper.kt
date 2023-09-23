package com.belajar.uas_crud_202101251002.helper

import android.database.Cursor
import com.belajar.uas_crud_202101251002.db.DatabaseContract
import com.belajar.uas_crud_202101251002.entity.Mahasiswa

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Mahasiswa> {
        val mahasiswaList = ArrayList<Mahasiswa>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns._ID))
                val nim = getString(getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NIM))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.NAMA))
                val jurusan = getString(getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.JURUSAN))
                val hobi = getString(getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.HOBI))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.MahasiswaColumns.DATE))
                mahasiswaList.add(Mahasiswa(id, nim, nama, jurusan, hobi, date))
            }
        }
        return mahasiswaList
    }
}
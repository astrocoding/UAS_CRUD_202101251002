package com.belajar.uas_crud_202101251002.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class MahasiswaColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "mahasiswa"
            const val _ID = "_id"
            const val NIM = "nim"
            const val NAMA = "nama"
            const val JURUSAN = "jurusan"
            const val HOBI = "hobi"
            const val DATE = "date"
        }
    }
}
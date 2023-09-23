package com.belajar.uas_crud_202101251002.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Mahasiswa (
    var id: Int = 0,
    var nim: String? = null,
    var nama: String? = null,
    var jurusan: String? = null,
    var hobi: String? = null,
    var date: String? = null
) : Parcelable
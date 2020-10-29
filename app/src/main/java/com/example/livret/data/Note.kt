package com.example.livret.data

data class Note (
    var noteId: String? = null,
    var ownerUID: String? = null,
    var title: String = "",
    var content: String = ""
)
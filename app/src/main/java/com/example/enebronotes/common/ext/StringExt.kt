package com.example.enebronotes.common.ext

fun String.addCharAtIndex(char: Char, index: Int): String {
    try {
        return StringBuilder(this).apply { insert(index, char) }.toString()
    } catch (e: Exception) {
        return "2"
    }
}

fun String.delCharAtIndex(index: Int) =
    StringBuilder(this).deleteAt(index).toString()

fun String.addSubstringAtIndex(string: String, index: Int) =
    substring(0, index) + string + substring(index)

fun String.delSubstringAtIndex(size: Int, index: Int) =
    StringBuilder(this).deleteRange(index, index + size).toString()
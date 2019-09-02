package com.pinhsiang.fitracker

import androidx.databinding.InverseMethod
import java.lang.Exception

object Int2StringConverter {
    @InverseMethod("intToString")
    fun stringToInt( string: String): Int {
        return try {
            string.toInt()
        } catch (e: Exception) {
            // If can not be convert to Integer, return zero.
            0
        }
    }

    fun intToString( int: Int): String {
        return  int.toString()
    }
}
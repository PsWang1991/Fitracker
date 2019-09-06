package com.pinhsiang.fitracker

import androidx.databinding.InverseMethod
import java.lang.Exception

object Int2StringConverter {
    @InverseMethod("intToString")
    fun stringToInt(string: String): Int {
        return try {
            string.toInt()
        } catch (e: Exception) {
            // If can not be convert to Integer, return zero.
            0
        }
    }

    fun intToString(int: Int): String {
        return int.toString()
    }
}

object Float2StringConverter {
    @InverseMethod("floatToString")
    fun stringToFloat(string: String): Float {
        return try {
            string.toFloat()
        } catch (e: Exception) {
            // If can not be convert to Float, return zero.
            0.0F
        }
    }

    fun floatToString(float: Float): String {
        return float.toString()
    }
}

object Double2StringConverter {
    @InverseMethod("doubleToString")
    fun stringToDouble(string: String): Double {
        return try {
            string.toDouble()
        } catch (e: Exception) {
            // If can not be convert to Double, return zero.
            0.0
        }
    }

    fun doubleToString(double: Double): String {
        return double.toString()
    }
}
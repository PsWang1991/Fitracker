package com.pinhsiang.fitracker

import android.content.Context
import com.pinhsiang.fitracker.R

class ClassUnderTest(private val context: Context) {

    fun getHelloWorldString(): String {
        return context.getString(R.string.hello_word)
    }

    fun helloMockito(): String {
        return "Hello Mockito"
    }
}
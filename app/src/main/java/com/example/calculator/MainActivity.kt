package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var resultTv: TextView  //lateinit is used to initialize variables to be used later in the program and it is always declared as var.
    private var canAddOperation = false //to check if we can insert an operator i.e (+,-,*,/) in a textview.
    private var canAddDecimal = true // to check if we can add decimal.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        resultTv = findViewById(R.id.resultTv) //initializing textview
    }

    fun backspaceoperation(view: View) { // function to remove numbers from right hand side
        val length = resultTv.length()
        if (length > 0) {
            resultTv.text = resultTv.text.toString().substring(0, length - 1) // just extracting numbers from first to second last and assigning back to textview to implement backspace functionality
        }
    }

    fun clearoperation(view: View) { // All Clear operation
        resultTv.text = "" // assign empty string
    }

    fun arithmeticoperation(view: View) { // function that validates and only adds one operator in a textview
        if (view is TextView && canAddOperation) { // casting view to textview
            resultTv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun numberoperation(view: View) { // function that adds numbers in textview
        if (view is TextView) {
            if (view.text == ".") {
                if (canAddDecimal) resultTv.append(view.text)
                canAddDecimal = false
            } else {
                resultTv.append(view.text)
            }
            canAddOperation = true
        }
    }

    fun equalsoperation(view: View) { // function that display results
        resultTv.text = calculate()
    }

    fun calculate(): String {   // function which processes all calculations and return the final results
        val digitsList = calculatelist() // calculatelist() takes all inputs from textview and return them as list
        if (digitsList.isEmpty()) return ""

        val timesDivide = timesdivideoperation(digitsList) // from the list first calculates times and division operator and then returns the list with only sum and difference items remaining
        if (timesDivide.isEmpty()) return ""

        val sumMinus = summinusoperation(timesDivide) // calculates the sum and difference from list
        return sumMinus.toString()
    }

    private fun summinusoperation(timesdivide: MutableList<Any>): Float { //performs add and subtract operation
        var firstDigit = timesdivide[0].toString().toFloat()
        for (i in timesdivide.indices) {
            if (timesdivide[i] is Char && i != timesdivide.lastIndex) {
                val operator = timesdivide[i]
                val next = timesdivide[i + 1].toString().toFloat()
                if (operator == '+') {
                    firstDigit += next
                }
                if (operator == '-') {
                    firstDigit -= next
                }
            }
        }
        return firstDigit
    }

    private fun timesdivideoperation(passedlist: MutableList<Any>): MutableList<Any> {  // checks if input contains 'x' or '/' operation
        var list = passedlist
        while (list.contains('x') || list.contains('/')) {
            list = calculatetimesdiv(list)
        }
        return list
    }

    private fun calculatetimesdiv(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = list.size
        for (i in list.indices) { // 1..5 -> list.indices
            if (list[i] is Char && i != list.lastIndex && i < restartIndex) { // checks if the item is operator and it is not the last item
                val operator = list[i] // operator
                val previous = list[i - 1].toString().toFloat() // number before operator
                val next = list[i + 1].toString().toFloat() // number after operator
                when (operator) { // checks the operator
                    'x' -> {
                        newList.add(previous * next)
                        restartIndex = i + 1 // increments  i
                    }
                    '/' -> {
                        newList.add(previous / next)
                        restartIndex = i + 1
                    }
                    else -> { // if the operator is sum or difference
                        newList.add(previous)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex) {
                newList.add(list[i])
            }
        }
        return newList
    }

    fun calculatelist(): MutableList<Any> { // this function will convert input values in a textview to list i.e 7*2+5 -> [7,*,2,+,5]
        val list = mutableListOf<Any>()
        var currentVal = ""
        for (character in resultTv.text) {
            if (character.isDigit() || character == '.') {
                currentVal += character
            } else {
                if (currentVal.isNotEmpty()) {
                    list.add(currentVal.toFloatOrNull() ?: "")
                    currentVal = ""
                }
                list.add(character)
            }
        }
        if (currentVal.isNotEmpty()) {
            list.add(currentVal.toFloatOrNull() ?: "")
        }
        return list
    }
}

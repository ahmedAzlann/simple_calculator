package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

         private lateinit var resultTv: TextView
         private var canAddOperation = false
         private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
              resultTv = findViewById(R.id.resultTv);

        }
    fun backspaceoperation(view: View) {
        var length = resultTv.length()
        if(length>0){
            resultTv.text = resultTv.text.toString().substring(0,length-1)
        }
    }
    fun clearoperation(view: View) {
        resultTv.text = ""
    }
    fun arithmeticoperation(view: View) {

            if(view is TextView && canAddOperation){
                resultTv.append(view.text)
                canAddOperation = false
                canAddDecimal = true
            }


    }
    fun numberoperation(view: View) {
        if(view is TextView){
            if(view.text == "."){
                if(canAddDecimal)
                    resultTv.append(view.text)

                canAddDecimal = false
            }

            else
                resultTv.append(view.text)

            canAddOperation = true

        }

    }

    fun equalsoperation(view: View) {}


}



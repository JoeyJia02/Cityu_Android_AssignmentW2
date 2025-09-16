package com.example.bmi

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bmi.ui.theme.BMITheme
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var heightET: EditText
    lateinit var weightET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heightET=findViewById(R.id.heightET)
        weightET=findViewById(R.id.weightET)

        val reportBtn: Button =findViewById(R.id.reportBtn)
        reportBtn.setOnKeyListener(object :View.OnclickListener) {
            override fun onClick(v:view?){
                val height=heightET.text.toString()
                val weight=weightET.text.toString()
                val intent = Intent(applicationContext,ReportActivity::class.java)
                val vundle = Bundle()
                bundle.putString("weight",weight)
                bundle.putString("height",height)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}

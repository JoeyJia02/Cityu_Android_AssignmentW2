package com.example.bmi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var ageET: EditText
    private lateinit var genderSwitch: Switch
    private lateinit var heightET: EditText
    private lateinit var weightET: EditText
    private lateinit var reportBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ageET = findViewById(R.id.ageET)
        genderSwitch = findViewById(R.id.genderSwitch)
        heightET = findViewById(R.id.heightET)
        weightET = findViewById(R.id.weightET)
        reportBtn = findViewById(R.id.reportBtn)

        reportBtn.setOnClickListener {
            // 1. 校验年龄
            val ageStr = ageET.text.toString()
            if (ageStr.isEmpty()) {
                Toast.makeText(this, "请输入年龄", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val age = ageStr.toInt()
            if (age < 6) {
                Toast.makeText(this, "年龄请输入≥6岁", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. 校验身高
            val heightStr = heightET.text.toString()
            if (heightStr.isEmpty()) {
                Toast.makeText(this, "请输入身高", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val height = heightStr.toDouble() / 100 // 转换为米

            // 3. 校验体重
            val weightStr = weightET.text.toString()
            if (weightStr.isEmpty()) {
                Toast.makeText(this, "请输入体重", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val weight = weightStr.toDouble()

            // 4. 区分用户类型：≥19岁为成人，6-18岁为未成年人
            val isAdult = age >= 19
            val intent = Intent(this, ReportActivity::class.java)
            intent.putExtra("isAdult", isAdult)
            if (!isAdult) {
                // 未成年人需传递性别和年龄
                intent.putExtra("age", age)
                intent.putExtra("isMale", genderSwitch.isChecked)
            }
            intent.putExtra("height", height)
            intent.putExtra("weight", weight)
            startActivity(intent)
        }
    }
}
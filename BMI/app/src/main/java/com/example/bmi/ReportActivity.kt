package com.example.bmi

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class ReportActivity : AppCompatActivity() {
    // 未成年人（男孩）BMI区间映射（同前，略）
    private val boyBmiRanges = mapOf(
        6 to mapOf("severelyUnderweight" to 12.0, "underweight" to 13.3, "acceptable" to 18.8, "overweight" to 23.0, "severelyOverweight" to 23.5),
        7 to mapOf("severelyUnderweight" to 12.8, "underweight" to 13.3, "acceptable" to 19.8, "overweight" to 23.4, "severelyOverweight" to 24.1),
        8 to mapOf("severelyUnderweight" to 13.2, "underweight" to 13.6, "acceptable" to 20.0, "overweight" to 24.6, "severelyOverweight" to 24.7),
        9 to mapOf("severelyUnderweight" to 13.5, "underweight" to 13.8, "acceptable" to 21.8, "overweight" to 27.0, "severelyOverweight" to 27.1),
        10 to mapOf("severelyUnderweight" to 13.8, "underweight" to 14.1, "acceptable" to 22.7, "overweight" to 27.3, "severelyOverweight" to 28.4),
        11 to mapOf("severelyUnderweight" to 14.1, "underweight" to 14.5, "acceptable" to 23.6, "overweight" to 28.3, "severelyOverweight" to 29.3),
        12 to mapOf("severelyUnderweight" to 14.4, "underweight" to 14.8, "acceptable" to 24.3, "overweight" to 29.2, "severelyOverweight" to 30.1),
        13 to mapOf("severelyUnderweight" to 14.7, "underweight" to 15.1, "acceptable" to 25.0, "overweight" to 30.0, "severelyOverweight" to 30.7),
        14 to mapOf("severelyUnderweight" to 15.0, "underweight" to 15.4, "acceptable" to 25.5, "overweight" to 30.2, "severelyOverweight" to 31.3),
        15 to mapOf("severelyUnderweight" to 15.3, "underweight" to 15.8, "acceptable" to 26.1, "overweight" to 31.0, "severelyOverweight" to 31.8),
        16 to mapOf("severelyUnderweight" to 15.6, "underweight" to 16.1, "acceptable" to 26.5, "overweight" to 31.7, "severelyOverweight" to 32.2),
        17 to mapOf("severelyUnderweight" to 15.9, "underweight" to 16.3, "acceptable" to 27.0, "overweight" to 32.1, "severelyOverweight" to 32.5),
        18 to mapOf("severelyUnderweight" to 16.1, "underweight" to 16.6, "acceptable" to 27.4, "overweight" to 32.4, "severelyOverweight" to 32.5)
    )

    // 未成年人（女孩）BMI区间映射（同前，略）
    private val girlBmiRanges = mapOf(
        6 to mapOf("severelyUnderweight" to 12.6, "underweight" to 12.8, "acceptable" to 18.3, "overweight" to 20.5, "severelyOverweight" to 20.6),
        7 to mapOf("severelyUnderweight" to 12.8, "underweight" to 13.1, "acceptable" to 19.1, "overweight" to 21.1, "severelyOverweight" to 21.2),
        8 to mapOf("severelyUnderweight" to 13.2, "underweight" to 13.4, "acceptable" to 20.1, "overweight" to 23.1, "severelyOverweight" to 23.2),
        9 to mapOf("severelyUnderweight" to 13.4, "underweight" to 13.7, "acceptable" to 21.0, "overweight" to 24.4, "severelyOverweight" to 24.5),
        10 to mapOf("severelyUnderweight" to 13.7, "underweight" to 14.1, "acceptable" to 21.9, "overweight" to 25.6, "severelyOverweight" to 25.7),
        11 to mapOf("severelyUnderweight" to 14.1, "underweight" to 14.4, "acceptable" to 22.7, "overweight" to 26.6, "severelyOverweight" to 26.7),
        12 to mapOf("severelyUnderweight" to 14.4, "underweight" to 14.8, "acceptable" to 23.4, "overweight" to 27.5, "severelyOverweight" to 27.6),
        13 to mapOf("severelyUnderweight" to 14.8, "underweight" to 15.2, "acceptable" to 24.0, "overweight" to 28.3, "severelyOverweight" to 28.4),
        14 to mapOf("severelyUnderweight" to 15.1, "underweight" to 15.5, "acceptable" to 24.6, "overweight" to 29.0, "severelyOverweight" to 29.0),
        15 to mapOf("severelyUnderweight" to 15.4, "underweight" to 15.8, "acceptable" to 25.0, "overweight" to 29.4, "severelyOverweight" to 29.5),
        16 to mapOf("severelyUnderweight" to 15.7, "underweight" to 16.1, "acceptable" to 25.4, "overweight" to 30.0, "severelyOverweight" to 30.1),
        17 to mapOf("severelyUnderweight" to 15.9, "underweight" to 16.3, "acceptable" to 25.7, "overweight" to 30.7, "severelyOverweight" to 30.8),
        18 to mapOf("severelyUnderweight" to 16.1, "underweight" to 16.5, "acceptable" to 25.9, "overweight" to 30.3, "severelyOverweight" to 30.4)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val resultTV: TextView = findViewById(R.id.resultTV)
        val adviceTV: TextView = findViewById(R.id.adviceTV)
        val resultImage: ImageView = findViewById(R.id.resultImage)

        // 获取参数
        val isAdult = intent.getBooleanExtra("isAdult", true)
        val height = intent.getDoubleExtra("height", 0.0)
        val weight = intent.getDoubleExtra("weight", 0.0)

        // 校验参数
        if (height == 0.0 || weight == 0.0) {
            resultTV.text = "数据传递失败"
            return
        }

        // 计算BMI
        val bmi = weight / (height * height)
        val nf = DecimalFormat("0.00")
        resultTV.text = "Your BMI value is ${nf.format(bmi)}"

        // 分支逻辑：成人 / 未成年人
        if (isAdult) {
            // 成年人BMI判断（原逻辑）
            if (bmi > 25) {
                resultImage.setImageResource(R.drawable.bot_fat)
                adviceTV.text = "Obese (Adult)"
            } else if (bmi >23) {
                resultImage.setImageResource(R.drawable.bot_fat)
                adviceTV.text = "Overweight (Adult)"
            } else if (bmi >18.5){
                resultImage.setImageResource(R.drawable.bot_fit)
                adviceTV.text = "Healthy (Adult)"
            }else {
                resultImage.setImageResource(R.drawable.bot_thin)
                adviceTV.text = "Underweight (Adult)"
            }
        } else {
            // 未成年人BMI判断（结合性别+年龄）
            val age = intent.getIntExtra("age", 0)
            val isMale = intent.getBooleanExtra("isMale", true)
            val ranges = if (isMale) boyBmiRanges[age] else girlBmiRanges[age]

            ranges?.let { range ->
                when {
                    bmi <= range["severelyUnderweight"]!! -> {
                        resultImage.setImageResource(R.drawable.bot_thin)
                        adviceTV.text = "Severely Underweight (Youth)"
                    }
                    bmi <= range["underweight"]!! -> {
                        resultImage.setImageResource(R.drawable.bot_thin)
                        adviceTV.text = "Underweight (Youth)"
                    }
                    bmi <= range["acceptable"]!! -> {
                        resultImage.setImageResource(R.drawable.bot_fit)
                        adviceTV.text = "Acceptable Weight (Youth)"
                    }
                    bmi <= range["overweight"]!! -> {
                        resultImage.setImageResource(R.drawable.bot_fat)
                        adviceTV.text = "Overweight (Youth)"
                    }
                    else -> {
                        resultImage.setImageResource(R.drawable.bot_fat)
                        adviceTV.text = "Severely Overweight (Youth)"
                    }
                }
            } ?: run {
                adviceTV.text = "未找到对应年龄的BMI区间"
            }
        }
    }
}
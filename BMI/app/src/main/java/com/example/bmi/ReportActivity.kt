package com.example.bmi

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bmi.presenter.ReportPresenter
import com.example.bmi.view.ReportView

class ReportActivity : AppCompatActivity(), ReportView {
    private lateinit var resultTV: TextView
    private lateinit var adviceTV: TextView
    private lateinit var resultImage: ImageView
    private lateinit var presenter: ReportPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        // 初始化控件
        resultTV = findViewById(R.id.resultTV)
        adviceTV = findViewById(R.id.adviceTV)
        resultImage = findViewById(R.id.resultImage)

        // 初始化Presenter
        presenter = ReportPresenter(this)

        // 获取传参并处理
        val bmi = intent.getDoubleExtra("bmi", 0.0)
        val isAdult = intent.getBooleanExtra("isAdult", true)
        presenter.processBMIResult(bmi, isAdult)
    }

    // ReportView接口实现
    override fun showBMIResult(bmi: Double, advice: String, resId: Int) {
        resultTV.text = String.format("BMI: %.2f", bmi)
        adviceTV.text = advice
        resultImage.setImageResource(resId)
    }
}
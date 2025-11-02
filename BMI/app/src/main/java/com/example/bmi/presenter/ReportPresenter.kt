package com.example.bmi.presenter

import com.example.bmi.model.BMIModel
import com.example.bmi.view.ReportView

class ReportPresenter(private val view: ReportView) {
    private val bmiModel = BMIModel()

    fun processBMIResult(bmi: Double, isAdult: Boolean) {
        val advice = bmiModel.getBMIAdvice(bmi, isAdult)
        val resId = bmiModel.getBMIResId(bmi, isAdult)
        view.showBMIResult(bmi, advice, resId)
    }
}
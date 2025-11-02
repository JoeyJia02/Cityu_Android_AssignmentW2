package com.example.bmi.presenter

import com.example.bmi.model.BMIModel
import com.example.bmi.view.MainView

class MainPresenter(private val view: MainView) {
    private val bmiModel = BMIModel()

    fun calculateBMI(ageStr: String, heightStr: String, weightStr: String, isMale: Boolean) {
        // 输入校验
        if (ageStr.isEmpty()) {
            view.showToast("请输入年龄")
            return
        }
        val age = ageStr.toInt()
        if (age < 6) {
            view.showToast("年龄请输入≥6岁")
            return
        }

        if (heightStr.isEmpty()) {
            view.showToast("请输入身高")
            return
        }
        val height = heightStr.toDouble() / 100

        if (weightStr.isEmpty()) {
            view.showToast("请输入体重")
            return
        }
        val weight = weightStr.toDouble()

        // 计算BMI
        val bmi = bmiModel.calculateBMI(weight, height)
        val isAdult = age >= 19

        // 通知View跳转
        view.navigateToReport(bmi, isAdult, age, isMale, height, weight)
    }
}
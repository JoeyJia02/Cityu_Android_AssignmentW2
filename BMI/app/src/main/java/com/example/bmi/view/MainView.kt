package com.example.bmi.view

interface MainView {
    fun showToast(message: String)
    fun navigateToReport(bmi: Double, isAdult: Boolean, age: Int, isMale: Boolean, height: Double, weight: Double)
}
package com.example.bmi.view

interface ReportView {
    fun showBMIResult(bmi: Double, advice: String, resId: Int)
}
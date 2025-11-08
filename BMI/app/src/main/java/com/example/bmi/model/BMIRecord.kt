package com.example.bmi.model

// 封装单条BMI记录的数据模型
data class BMIRecord(
    val gender: String,       // 性别（"male"/"female"）
    val age: String,          // 年龄（字符串形式，便于存储）
    val height: String,       // 身高（单位：cm，字符串形式）
    val weight: String,       // 体重（单位：kg，字符串形式）
    val bmi: Double,          // BMI值
    val date: String          // 记录日期（格式：yyyy-MM-dd）
)
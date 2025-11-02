package com.example.bmi.model

import com.example.bmi.R

class BMIModel {
    // BMI计算逻辑
    fun calculateBMI(weight: Double, height: Double): Double {
        return weight / (height * height)
    }

    // 体型判断（成人）
    fun getBMIAdvice(bmi: Double, isAdult: Boolean): String {
        return if (isAdult) {
            when {
                bmi <= 18.5 -> "偏瘦：你需要多补充热量！"
                bmi <= 23 -> "健康：你的体重很标准！"
                bmi <= 25 -> "超重：你需要控制饮食了！"
                else -> "肥胖：你需要认真减重了！"
            }
        } else {
            // 未成年人逻辑（示例，实际需根据年龄/性别细化）
            "未成年人BMI需结合年龄和性别判断，建议咨询专业人士。"
        }
    }

    // 获取BMI对应图标资源ID
    fun getBMIResId(bmi: Double, isAdult: Boolean): Int {
        return if (isAdult) {
            when {
                bmi <= 18.5 -> R.drawable.bot_thin
                bmi <= 23 -> R.drawable.bot_fit
                bmi <= 25 -> R.drawable.bot_fat
                else -> R.drawable.bot_fat
            }
        } else {
            R.drawable.bot_fit // 未成年人默认图标
        }
    }
}
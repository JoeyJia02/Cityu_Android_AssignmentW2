package com.example.bmi

import android.os.Bundle
import android.widget.Button
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.bmi.model.BMIDbHelper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BMIHistoryActivity : AppCompatActivity() {
    private lateinit var lineChart: LineChart  // 折线图控件

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)  // 关联布局

        // 绑定控件（确保布局中存在line_chart和btn_back）
        lineChart = findViewById(R.id.line_chart)
        val btnBack = findViewById<Button>(R.id.btn_back)

        // 加载并展示历史数据
        loadAndShowHistoryData()

        // 返回主界面
        btnBack.setOnClickListener { finish() }
    }

    // 从数据库加载历史数据并显示在折线图上
    private fun loadAndShowHistoryData() {
        // 查询所有记录
        val records = BMIDbHelper(this).getAllRecords()
        if (records.isEmpty()) {
            // 无数据时可添加提示（如Toast）
            return
        }

        // 准备折线图数据：X轴为序号，Y轴为BMI值
        val entries = records.mapIndexed { index, record ->
            Entry(index.toFloat(), record.bmi.toFloat())
        }

        // X轴标签：显示日期
        val xAxisLabels = records.map { it.date }

        // 配置折线样式（使用系统颜色避免资源缺失）
        val dataSet = LineDataSet(entries, "BMI趋势").apply {
            color = Color.parseColor("#8A2BE2")  // 紫色（替换为项目已有颜色）
            valueTextColor = Color.BLACK  // 系统黑色，避免依赖资源
            lineWidth = 2f  // 折线宽度
            setDrawCircles(true)  // 显示数据点圆圈
            circleRadius = 4f  // 圆圈半径
            circleHoleRadius = 2f  // 圆圈中心孔半径
        }

        // 配置X轴（底部显示，标签为日期）
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM  // X轴在底部
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)  // 显示日期
        xAxis.labelRotationAngle = -45f  // 标签旋转45度，避免重叠
        xAxis.setLabelCount(records.size.coerceAtMost(7), false)  // 最多显示7个标签
        xAxis.textColor = Color.DKGRAY  // 轴标签颜色

        // 隐藏右侧Y轴，保留左侧
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.textColor = Color.DKGRAY  // 左侧Y轴颜色

        // 设置数据并刷新图表
        lineChart.data = LineData(dataSet)
        lineChart.description.isEnabled = false  // 隐藏描述文字
        lineChart.legend.isEnabled = true  // 显示图例（"BMI趋势"）
        lineChart.invalidate()  // 刷新图表
    }
}
package com.example.bmi

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.util.TypedValue
import com.example.bmi.model.BMIDbHelper
import com.example.bmi.model.BMIRecord
import com.example.bmi.presenter.MainPresenter
import com.example.bmi.view.MainView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var ageET: EditText
    private lateinit var genderSwitch: Switch
    private lateinit var heightET: EditText
    private lateinit var weightET: EditText
    private lateinit var reportBtn: Button
    private lateinit var aboutBmiBtn: Button
    private lateinit var btnHistory: Button  // 新增：查看历史按钮
    private lateinit var presenter: MainPresenter
    private lateinit var sp: SharedPreferences  // 新增：SharedPreferences实例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化控件（保留原有+新增）
        ageET = findViewById(R.id.ageET)
        genderSwitch = findViewById(R.id.genderSwitch)
        heightET = findViewById(R.id.heightET)
        weightET = findViewById(R.id.weightET)
        reportBtn = findViewById(R.id.reportBtn)
        aboutBmiBtn = findViewById(R.id.aboutBmiBtn)
        btnHistory = findViewById(R.id.btn_history)  // 绑定新增按钮

        // 初始化Presenter和SP
        presenter = MainPresenter(this)
        sp = getSharedPreferences("BMI_USER_INFO", MODE_PRIVATE)  // 新增：SP初始化

        // 注册上下文菜单（保留原有）
        registerForContextMenu(findViewById(R.id.titleTV))

        // 加载保存的用户信息（新增）
        loadUserInfoFromSP()

        // 查看报告按钮点击事件（保留原有+扩展存储逻辑）
        reportBtn.setOnClickListener {
            presenter.calculateBMI(
                ageET.text.toString(),
                heightET.text.toString(),
                weightET.text.toString(),
                genderSwitch.isChecked
            )
        }

        // 关于BMI按钮点击事件（保留原有）
        aboutBmiBtn.setOnClickListener {
            showAboutDialog()
        }

        // 新增：查看历史按钮点击事件（跳转历史页面）
        btnHistory.setOnClickListener {
            startActivity(Intent(this, BMIHistoryActivity::class.java))
        }

        // 恢复横竖屏数据（保留原有）
        if (savedInstanceState != null) {
            ageET.setText(savedInstanceState.getString("saved_age"))
            genderSwitch.isChecked = savedInstanceState.getBoolean("saved_is_male", true)
            heightET.setText(savedInstanceState.getString("saved_height"))
            weightET.setText(savedInstanceState.getString("saved_weight"))
        }
        //insertManualHistoryData() insert data
    }

    // 新增：从SP加载用户信息（性别、年龄、身高）
    private fun loadUserInfoFromSP() {
        val savedIsMale = sp.getBoolean("is_male", true)  // 默认为男
        val savedAge = sp.getString("age", "")
        val savedHeight = sp.getString("height", "")

        // 填充到UI
        genderSwitch.isChecked = savedIsMale
        ageET.setText(savedAge)
        heightET.setText(savedHeight)
    }

    // 新增：保存用户信息到SP
    private fun saveUserInfoToSP(isMale: Boolean, age: String, height: String) {
        sp.edit()
            .putBoolean("is_male", isMale)
            .putString("age", age)
            .putString("height", height)
            .apply()
    }

    // 保留原有：横竖屏数据保存
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("saved_age", ageET.text.toString())
        outState.putBoolean("saved_is_male", genderSwitch.isChecked)
        outState.putString("saved_height", heightET.text.toString())
        outState.putString("saved_weight", weightET.text.toString())
    }

    // 实现MainView接口：显示Toast（保留原有）
    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 实现MainView接口：跳转ReportActivity（扩展存储逻辑）
    override fun navigateToReport(
        bmi: Double,
        isAdult: Boolean,
        age: Int,
        isMale: Boolean,
        height: Double,
        weight: Double
    ) {
        // 1. 保存用户信息到SP（性别、年龄、身高）
        saveUserInfoToSP(
            isMale = isMale,
            age = age.toString(),
            height = (height * 100).toString()  // 身高转为cm（原始输入单位）
        )

        // 2. 保存BMI记录到SQLite
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val record = BMIRecord(
            gender = if (isMale) "male" else "female",
            age = age.toString(),
            height = (height * 100).toString(),  // cm
            weight = weight.toString(),          // kg
            bmi = bmi,
            date = currentDate
        )
        BMIDbHelper(this).insertOrUpdate(record)

        // 3. 跳转报告页（保留原有逻辑）
        val intent = Intent(this, ReportActivity::class.java)
        intent.putExtra("bmi", bmi)
        intent.putExtra("isAdult", isAdult)
        intent.putExtra("age", age)
        intent.putExtra("isMale", isMale)
        intent.putExtra("height", height)
        intent.putExtra("weight", weight)
        startActivity(intent)
    }

    // 选项菜单（保留原有）
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                showAboutDialog()
                true
            }
            R.id.menu_wiki -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://baike.baidu.com/item/BMI/1491"))
                startActivity(intent)
                true
            }
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 上下文菜单（保留原有）
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ctx_menu_about -> {
                showAboutDialog()
                true
            }
            R.id.ctx_menu_wiki -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://baike.baidu.com/item/BMI/1491"))
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // 关于BMI对话框（保留原有）
    private fun showAboutDialog() {
        val dialogTitle = TextView(this).apply {
            text = getString(R.string.about_dialog_title)
            textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 18f, resources.displayMetrics
            )
            setTextColor(Color.parseColor("#F44336"))
            setPadding(0, 0, 0, dpToPx(12))
        }

        val dialogContent = getString(R.string.about_dialog_content)

        AlertDialog.Builder(this)
            .setCustomTitle(dialogTitle)
            .setMessage(dialogContent)
            .setPositiveButton(getString(R.string.dialog_confirm), null)
            .setCancelable(true)
            .show()
    }

    // dp转px工具方法（保留原有）
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }
    private fun insertManualHistoryData() {
        val dbHelper = BMIDbHelper(this)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // 构造过去3天的模拟数据（可根据需要调整日期、BMI值）
        listOf(
            BMIRecord(
                gender = "male",
                age = "25",
                height = "175",
                weight = "65",
                bmi = 19.8,
                date = sdf.format(calendar.apply { add(Calendar.DAY_OF_MONTH, -3) }.time)
            ),
            BMIRecord(
                gender = "male",
                age = "25",
                height = "175",
                weight = "66",
                bmi = 20.2,
                date = sdf.format(calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time)
            ),
            BMIRecord(
                gender = "male",
                age = "25",
                height = "175",
                weight = "67",
                bmi = 20.8,
                date = sdf.format(calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time)
            )
        ).forEach { record ->
            dbHelper.insertOrUpdate(record)
        }
        showToast("已手动插入3天历史数据！")
    }
}
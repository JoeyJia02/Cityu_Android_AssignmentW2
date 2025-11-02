package com.example.bmi

import android.content.Intent
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
import com.example.bmi.presenter.MainPresenter
import com.example.bmi.view.MainView

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var ageET: EditText
    private lateinit var genderSwitch: Switch
    private lateinit var heightET: EditText
    private lateinit var weightET: EditText
    private lateinit var reportBtn: Button
    private lateinit var aboutBmiBtn: Button
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化控件
        ageET = findViewById(R.id.ageET)
        genderSwitch = findViewById(R.id.genderSwitch)
        heightET = findViewById(R.id.heightET)
        weightET = findViewById(R.id.weightET)
        reportBtn = findViewById(R.id.reportBtn)
        aboutBmiBtn = findViewById(R.id.aboutBmiBtn)

        // 初始化Presenter
        presenter = MainPresenter(this)

        // 注册上下文菜单（长按标题栏触发）
        registerForContextMenu(findViewById(R.id.titleTV))

        // 查看报告按钮点击事件（委托给Presenter处理）
        reportBtn.setOnClickListener {
            presenter.calculateBMI(
                ageET.text.toString(),
                heightET.text.toString(),
                weightET.text.toString(),
                genderSwitch.isChecked
            )
        }

        // 关于BMI按钮点击事件
        aboutBmiBtn.setOnClickListener {
            showAboutDialog()
        }

        // 恢复横竖屏切换时的数据
        if (savedInstanceState != null) {
            ageET.setText(savedInstanceState.getString("saved_age"))
            genderSwitch.isChecked = savedInstanceState.getBoolean("saved_is_male", true)
            heightET.setText(savedInstanceState.getString("saved_height"))
            weightET.setText(savedInstanceState.getString("saved_weight"))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("saved_age", ageET.text.toString())
        outState.putBoolean("saved_is_male", genderSwitch.isChecked)
        outState.putString("saved_height", heightET.text.toString())
        outState.putString("saved_weight", weightET.text.toString())
    }

    // 实现MainView接口：显示Toast
    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 实现MainView接口：跳转ReportActivity
    override fun navigateToReport(
        bmi: Double,
        isAdult: Boolean,
        age: Int,
        isMale: Boolean,
        height: Double,
        weight: Double
    ) {
        val intent = Intent(this, ReportActivity::class.java)
        intent.putExtra("bmi", bmi)
        intent.putExtra("isAdult", isAdult)
        intent.putExtra("age", age)
        intent.putExtra("isMale", isMale)
        intent.putExtra("height", height)
        intent.putExtra("weight", weight)
        startActivity(intent)
    }

    // 选项菜单创建与处理
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

    // 上下文菜单创建与处理
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

    // 关于BMI对话框
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

    // dp转px工具方法
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }
}
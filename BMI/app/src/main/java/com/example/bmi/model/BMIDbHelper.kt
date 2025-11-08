package com.example.bmi.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 数据库帮助类：管理BMI历史记录的增删查改
class BMIDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "BMIHistory.db"       // 数据库名
        private const val DB_VERSION = 1                  // 版本号
        private const val TABLE_RECORD = "bmi_records"    // 表名

        // 表字段
        private const val COL_ID = "_id"                  // 自增ID
        private const val COL_GENDER = "gender"           // 性别
        private const val COL_AGE = "age"                 // 年龄
        private const val COL_HEIGHT = "height"           // 身高
        private const val COL_WEIGHT = "weight"           // 体重
        private const val COL_BMI = "bmi"                 // BMI值
        private const val COL_DATE = "date"               // 日期（唯一，确保单日只存一条）
    }

    // 创建表
    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = """
            CREATE TABLE $TABLE_RECORD (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_GENDER TEXT NOT NULL,
                $COL_AGE TEXT NOT NULL,
                $COL_HEIGHT TEXT NOT NULL,
                $COL_WEIGHT TEXT NOT NULL,
                $COL_BMI REAL NOT NULL,
                $COL_DATE TEXT UNIQUE NOT NULL  -- 日期唯一，避免重复记录
            )
        """.trimIndent()
        db.execSQL(createTableSql)
    }

    // 升级数据库（此处简化处理：删除旧表重建）
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECORD")
        onCreate(db)
    }

    // 插入或更新记录（若当天已有记录，则覆盖）
    fun insertOrUpdate(record: BMIRecord) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_GENDER, record.gender)
            put(COL_AGE, record.age)
            put(COL_HEIGHT, record.height)
            put(COL_WEIGHT, record.weight)
            put(COL_BMI, record.bmi)
            put(COL_DATE, record.date)
        }
        // replace：若存在相同date（唯一键冲突），则更新记录
        db.replace(TABLE_RECORD, null, values)
        db.close()
    }

    // 查询所有历史记录（按日期升序排列）
    fun getAllRecords(): List<BMIRecord> {
        val records = mutableListOf<BMIRecord>()
        val db = readableDatabase
        // 查询所有数据，按日期排序
        val cursor = db.query(
            TABLE_RECORD,
            null,
            null,
            null,
            null,
            null,
            "$COL_DATE ASC"
        )

        // 遍历结果集
        if (cursor.moveToFirst()) {
            do {
                val record = BMIRecord(
                    gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER)),
                    age = cursor.getString(cursor.getColumnIndexOrThrow(COL_AGE)),
                    height = cursor.getString(cursor.getColumnIndexOrThrow(COL_HEIGHT)),
                    weight = cursor.getString(cursor.getColumnIndexOrThrow(COL_WEIGHT)),
                    bmi = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BMI)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                )
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return records
    }
}
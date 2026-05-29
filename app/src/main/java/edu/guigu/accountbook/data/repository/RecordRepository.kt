package edu.guigu.accountbook.data.repository

import edu.guigu.accountbook.data.dao.CategorySummary
import edu.guigu.accountbook.data.dao.MonthlySummary
import edu.guigu.accountbook.data.dao.MonthlyTrend
import edu.guigu.accountbook.data.dao.RecordDao
import edu.guigu.accountbook.data.model.Record

class RecordRepository(private val dao: RecordDao) {

    suspend fun getAllRecords(): List<Record> = dao.getAllRecords()
    suspend fun getTotalIncome(): Double? = dao.getTotalIncome()
    suspend fun getTotalExpense(): Double? = dao.getTotalExpense()
    suspend fun getCategorySummary(type: Int): List<CategorySummary> = dao.getCategorySummary(type)
    suspend fun getMonthlyTrend(): List<MonthlyTrend> = dao.getMonthlyTrend()
    suspend fun searchRecords(keyword: String): List<Record> = dao.searchRecords(keyword)
    suspend fun getMonthlySummary(month: String): MonthlySummary? = dao.getMonthlySummary(month)
    suspend fun getCategoryByMonth(month: String, category: String): Double {
        val records = dao.getRecordsByMonth(month)
        return records.filter { it.category == category }.sumOf { it.amount }
    }

    suspend fun insert(record: Record): Long = dao.insert(record)
    suspend fun update(record: Record) = dao.update(record)
    suspend fun delete(record: Record) = dao.delete(record)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
}

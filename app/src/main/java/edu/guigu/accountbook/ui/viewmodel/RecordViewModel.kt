package edu.guigu.accountbook.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.guigu.accountbook.data.dao.CategorySummary
import edu.guigu.accountbook.data.dao.MonthlySummary
import edu.guigu.accountbook.data.dao.MonthlyTrend
import edu.guigu.accountbook.data.database.AppDatabase
import edu.guigu.accountbook.data.model.Record
import edu.guigu.accountbook.data.repository.RecordRepository
import kotlinx.coroutines.launch

class RecordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecordRepository

    private val _allRecords = MutableLiveData<List<Record>>(emptyList())
    val allRecords: LiveData<List<Record>> get() = _allRecords

    private val _totalIncome = MutableLiveData(0.0)
    val totalIncome: LiveData<Double> get() = _totalIncome

    private val _totalExpense = MutableLiveData(0.0)
    val totalExpense: LiveData<Double> get() = _totalExpense

    private val _expenseCategorySummary = MutableLiveData<List<CategorySummary>>(emptyList())
    val expenseCategorySummary: LiveData<List<CategorySummary>> get() = _expenseCategorySummary

    private val _incomeCategorySummary = MutableLiveData<List<CategorySummary>>(emptyList())
    val incomeCategorySummary: LiveData<List<CategorySummary>> get() = _incomeCategorySummary

    private val _monthlyTrend = MutableLiveData<List<MonthlyTrend>>(emptyList())
    val monthlyTrend: LiveData<List<MonthlyTrend>> get() = _monthlyTrend

    // 当前选中的月份（格式："2025-05"）
    private val _selectedMonth = MutableLiveData<String>()
    val selectedMonth: LiveData<String> get() = _selectedMonth

    // 选中月份的收支汇总
    private val _selectedMonthSummary = MutableLiveData<MonthlySummary?>()
    val selectedMonthSummary: LiveData<MonthlySummary?> get() = _selectedMonthSummary

    // 选中月份的恩格尔系数（食品支出/总支出）
    private val _engelCoefficient = MutableLiveData<Double?>()
    val engelCoefficient: LiveData<Double?> get() = _engelCoefficient

    init {
        val dao = AppDatabase.getInstance(application).recordDao()
        repository = RecordRepository(dao)
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            _allRecords.postValue(repository.getAllRecords())
            _totalIncome.postValue(repository.getTotalIncome() ?: 0.0)
            _totalExpense.postValue(repository.getTotalExpense() ?: 0.0)
            _expenseCategorySummary.postValue(repository.getCategorySummary(Record.TYPE_EXPENSE))
            _incomeCategorySummary.postValue(repository.getCategorySummary(Record.TYPE_INCOME))
            _monthlyTrend.postValue(repository.getMonthlyTrend())
        }
    }

    fun insert(record: Record) {
        viewModelScope.launch {
            repository.insert(record)
            refreshData()
        }
    }

    fun update(record: Record) {
        viewModelScope.launch {
            repository.update(record)
            refreshData()
        }
    }

    fun delete(record: Record) {
        viewModelScope.launch {
            repository.delete(record)
            refreshData()
        }
    }

    /** 设置选中的月份，并加载该月的统计数据 */
    fun setSelectedMonth(month: String) {
        _selectedMonth.value = month
        viewModelScope.launch {
            // 加载该月的收支汇总
            val summary = repository.getMonthlySummary(month)
            _selectedMonthSummary.postValue(summary)

            // 计算恩格尔系数：食品支出 / 总支出
            if (summary != null && summary.expense > 0) {
                val foodExpense = repository.getCategoryByMonth(month, "餐饮")
                val engel = foodExpense / summary.expense
                _engelCoefficient.postValue(engel)
            } else {
                _engelCoefficient.postValue(null)
            }
        }
    }
}

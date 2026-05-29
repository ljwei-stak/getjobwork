package edu.guigu.accountbook.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import edu.guigu.accountbook.R
import edu.guigu.accountbook.data.dao.CategorySummary
import edu.guigu.accountbook.databinding.FragmentStatisticsBinding
import edu.guigu.accountbook.ui.viewmodel.RecordViewModel
import edu.guigu.accountbook.util.DateUtils

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RecordViewModel
    private lateinit var pieChartExpense: PieChart
    private lateinit var pieChartIncome: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ★ 必须用 requireActivity() 才能和 BillsFragment 共享同一个 ViewModel
        viewModel = ViewModelProvider(requireActivity())[RecordViewModel::class.java]

        // 初始化饼图
        pieChartExpense = binding.pieChartExpense
        pieChartIncome = binding.pieChartIncome
        setupPieCharts()

        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** 初始化饼图样式 */
    private fun setupPieCharts() {
        // 支出饼图设置
        pieChartExpense.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            setCenterText("支出")
            setCenterTextSize(16f)
            setCenterTextColor(Color.BLACK)
            rotationAngle = 0f
            isRotationEnabled = true
            setHighlightPerTapEnabled(true)
            animateY(1000)
            legend.isEnabled = true
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            legend.xEntrySpace = 7f
            legend.yEntrySpace = 0f
            legend.yOffset = 0f
        }

        // 收入饼图设置
        pieChartIncome.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            setCenterText("收入")
            setCenterTextSize(16f)
            setCenterTextColor(Color.BLACK)
            rotationAngle = 0f
            isRotationEnabled = true
            setHighlightPerTapEnabled(true)
            animateY(1000)
            legend.isEnabled = true
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            legend.xEntrySpace = 7f
            legend.yEntrySpace = 0f
            legend.yOffset = 0f
        }
    }

    private fun observeData() {
        android.util.Log.d("StatisticsFragment", "Starting to observe data...")
        
        // 观察总收入 → 更新显示
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            android.util.Log.d("StatisticsFragment", "Total income updated: $income")
            binding.tvIncome.text = "¥${DateUtils.formatAmount(income)}"
            updateBalance()
        }

        // 观察总支出 → 更新显示
        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            android.util.Log.d("StatisticsFragment", "Total expense updated: $expense")
            binding.tvExpense.text = "¥${DateUtils.formatAmount(expense)}"
            updateBalance()
        }

        // 观察支出分类统计 → 更新支出饼图
        viewModel.expenseCategorySummary.observe(viewLifecycleOwner) { summaries ->
            android.util.Log.d("StatisticsFragment", "Expense category summary updated with ${summaries.size} items")
            updateExpensePieChart(summaries)
        }

        // 观察收入分类统计 → 更新收入饼图
        viewModel.incomeCategorySummary.observe(viewLifecycleOwner) { summaries ->
            android.util.Log.d("StatisticsFragment", "Income category summary updated with ${summaries.size} items")
            updateIncomePieChart(summaries)
        }
    }

    /** 计算结余 = 收入 - 支出 */
    private fun updateBalance() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expense = viewModel.totalExpense.value ?: 0.0
        val balance = income - expense
        binding.tvBalance.text = "¥${DateUtils.formatAmount(balance)}"
    }

    /** 更新支出饼图 */
    private fun updateExpensePieChart(summaries: List<CategorySummary>) {
        android.util.Log.d("StatisticsFragment", "updateExpensePieChart called with ${summaries.size} items")
        
        if (summaries.isEmpty()) {
            pieChartExpense.clear()
            pieChartExpense.setCenterText("暂无数据")
            android.util.Log.d("StatisticsFragment", "No expense data to display")
            return
        }

        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        
        // 使用不同的颜色区分不同分类
        val colorArray = resources.getIntArray(R.array.expense_colors)
        
        summaries.forEachIndexed { index, summary ->
            entries.add(PieEntry(summary.total.toFloat(), summary.category))
            colors.add(if (index < colorArray.size) colorArray[index] else Color.GRAY)
        }

        val dataSet = PieDataSet(entries, "支出分类")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueFormatter = PercentFormatter()

        val pieData = PieData(dataSet)
        pieChartExpense.data = pieData
        pieChartExpense.invalidate() // 刷新图表
        
        android.util.Log.d("StatisticsFragment", "Expense pie chart updated successfully")
    }

    /** 更新收入饼图 */
    private fun updateIncomePieChart(summaries: List<CategorySummary>) {
        android.util.Log.d("StatisticsFragment", "updateIncomePieChart called with ${summaries.size} items")
        
        if (summaries.isEmpty()) {
            pieChartIncome.clear()
            pieChartIncome.setCenterText("暂无数据")
            android.util.Log.d("StatisticsFragment", "No income data to display")
            return
        }

        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        
        // 使用不同的颜色区分不同分类
        val colorArray = resources.getIntArray(R.array.income_colors)
        
        summaries.forEachIndexed { index, summary ->
            entries.add(PieEntry(summary.total.toFloat(), summary.category))
            colors.add(if (index < colorArray.size) colorArray[index] else Color.GREEN)
        }

        val dataSet = PieDataSet(entries, "收入分类")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueFormatter = PercentFormatter()

        val pieData = PieData(dataSet)
        pieChartIncome.data = pieData
        pieChartIncome.invalidate() // 刷新图表
        
        android.util.Log.d("StatisticsFragment", "Income pie chart updated successfully")
    }
}

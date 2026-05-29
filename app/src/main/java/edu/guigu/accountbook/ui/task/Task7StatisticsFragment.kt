package edu.guigu.accountbook.ui.task

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

/**
 * Task7 统计页 - 财务数据统计
 * - 收支汇总
 * - 分类饼图
 */
class Task7StatisticsFragment : Fragment() {

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

        viewModel = ViewModelProvider(requireActivity())[RecordViewModel::class.java]

        // 初始化饼图
        pieChartExpense = binding.pieChartExpense
        pieChartIncome = binding.pieChartIncome
        setupPieCharts()

        // 观察数据
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 初始化饼图样式
     */
    private fun setupPieCharts() {
        listOf(pieChartExpense, pieChartIncome).forEach { chart ->
            chart.apply {
                description.isEnabled = false
                setUsePercentValues(true)
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)
                holeRadius = 58f
                transparentCircleRadius = 61f
                setDrawCenterText(true)
                rotationAngle = 0f
                isRotationEnabled = true
                animateY(1000)
                legend.isEnabled = true
            }
        }
        
        pieChartExpense.setCenterText("支出")
        pieChartIncome.setCenterText("收入")
    }

    /**
     * 观察数据变化
     */
    private fun observeData() {
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvIncome.text = "¥${DateUtils.formatAmount(income)}"
            updateBalance()
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.tvExpense.text = "¥${DateUtils.formatAmount(expense)}"
            updateBalance()
        }

        viewModel.expenseCategorySummary.observe(viewLifecycleOwner) { summaries ->
            updateExpensePieChart(summaries)
        }

        viewModel.incomeCategorySummary.observe(viewLifecycleOwner) { summaries ->
            updateIncomePieChart(summaries)
        }
    }

    /**
     * 计算结余
     */
    private fun updateBalance() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expense = viewModel.totalExpense.value ?: 0.0
        val balance = income - expense
        binding.tvBalance.text = "¥${DateUtils.formatAmount(balance)}"
    }

    /**
     * 更新支出饼图
     */
    private fun updateExpensePieChart(summaries: List<CategorySummary>) {
        if (summaries.isEmpty()) {
            pieChartExpense.clear()
            pieChartExpense.setCenterText("暂无数据")
            return
        }

        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        val colorArray = resources.getIntArray(R.array.expense_colors)
        
        summaries.forEachIndexed { index, summary ->
            entries.add(PieEntry(summary.total.toFloat(), summary.category))
            colors.add(if (index < colorArray.size) colorArray[index] else Color.GRAY)
        }

        val dataSet = PieDataSet(entries, "支出分类").apply {
            this.colors = colors
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter()
        }

        pieChartExpense.data = PieData(dataSet)
        pieChartExpense.invalidate()
    }

    /**
     * 更新收入饼图
     */
    private fun updateIncomePieChart(summaries: List<CategorySummary>) {
        if (summaries.isEmpty()) {
            pieChartIncome.clear()
            pieChartIncome.setCenterText("暂无数据")
            return
        }

        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        val colorArray = resources.getIntArray(R.array.income_colors)
        
        summaries.forEachIndexed { index, summary ->
            entries.add(PieEntry(summary.total.toFloat(), summary.category))
            colors.add(if (index < colorArray.size) colorArray[index] else Color.GREEN)
        }

        val dataSet = PieDataSet(entries, "收入分类").apply {
            this.colors = colors
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter()
        }

        pieChartIncome.data = PieData(dataSet)
        pieChartIncome.invalidate()
    }
}

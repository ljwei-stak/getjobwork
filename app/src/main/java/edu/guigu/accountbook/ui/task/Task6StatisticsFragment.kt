package edu.guigu.accountbook.ui.task

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import edu.guigu.accountbook.R
import edu.guigu.accountbook.data.dao.CategorySummary
import edu.guigu.accountbook.data.dao.MonthlyTrend
import edu.guigu.accountbook.databinding.FragmentStatisticsBinding
import edu.guigu.accountbook.ui.viewmodel.RecordViewModel
import edu.guigu.accountbook.util.DateUtils
import java.text.DecimalFormat
import java.util.Calendar

/**
 * Task6 统计页 - 包含扩展功能：
 * - X1: 饼图大师 - 支出/收入分类占比可视化
 * - X2: 趋势分析师 - 月度收支趋势折线图（显示数值）
 * - 新增: 图表切换 - 饼图和折线图可切换查看
 * - 新增: 月份选择器 - 选择任意月份查看收支结余
 * - 新增: 恩格尔系数 - 计算食品支出占比
 */
class Task6StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RecordViewModel
    private lateinit var pieChartExpense: PieChart
    private lateinit var pieChartIncome: PieChart
    private lateinit var lineChart: LineChart
    
    // 当前显示的图表类型：true=饼图, false=折线图
    private var isShowingPieChart = true
    
    // 当前选中的月份（格式："2025-05"）
    private var currentMonth: String = ""

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

        // 使用requireActivity()共享ViewModel
        viewModel = ViewModelProvider(requireActivity())[RecordViewModel::class.java]

        // 初始化饼图
        pieChartExpense = binding.pieChartExpense
        pieChartIncome = binding.pieChartIncome
        lineChart = binding.lineChart
        setupPieCharts()
        setupLineChart()
        
        // 设置图表切换
        setupChartTypeSwitch()
        
        // 设置月份选择器
        setupMonthSelector()

        // 观察数据
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 设置图表类型切换
     */
    private fun setupChartTypeSwitch() {
        binding.radioGroupChartType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_pie_chart -> {
                    // 显示饼图，隐藏折线图
                    pieChartExpense.visibility = View.VISIBLE
                    binding.tvIncomeChartTitle.visibility = View.VISIBLE
                    pieChartIncome.visibility = View.VISIBLE
                    lineChart.visibility = View.GONE
                    binding.tvChartTitle.text = "支出分类统计"
                    isShowingPieChart = true
                }
                R.id.radio_line_chart -> {
                    // 显示折线图，隐藏饼图
                    pieChartExpense.visibility = View.GONE
                    binding.tvIncomeChartTitle.visibility = View.GONE
                    pieChartIncome.visibility = View.GONE
                    lineChart.visibility = View.VISIBLE
                    binding.tvChartTitle.text = "月度收支趋势"
                    isShowingPieChart = false
                }
            }
        }
    }

    /**
     * 设置月份选择器
     */
    private fun setupMonthSelector() {
        binding.chipMonthSelector.setOnClickListener {
            showMonthPicker()
        }
        
        // 默认设置为当前月份
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        currentMonth = String.format("%04d-%02d", year, month)
        binding.chipMonthSelector.text = year.toString() + "年" + month + "月"
        viewModel.setSelectedMonth(currentMonth)
    }

    /**
     * 显示月份选择器
     */
    private fun showMonthPicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, _ ->
                currentMonth = String.format("%04d-%02d", year, month + 1)
                binding.chipMonthSelector.text = year.toString() + "年" + (month + 1) + "月"
                viewModel.setSelectedMonth(currentMonth)
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1
        ).apply {
            // 只显示年月选择器
            datePicker.findViewById<View>(
                resources.getIdentifier("day", "id", "android")
            )?.visibility = View.GONE
        }.show()
    }

    /**
     * X1: 初始化饼图样式
     */
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

    /**
     * 观察ViewModel中的数据变化
     */
    private fun observeData() {
        // 观察总收入
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvIncome.text = "¥${DateUtils.formatAmount(income)}"
            updateBalance()
        }

        // 观察总支出
        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.tvExpense.text = "¥${DateUtils.formatAmount(expense)}"
            updateBalance()
        }

        // X1: 观察支出分类统计 → 更新支出饼图
        viewModel.expenseCategorySummary.observe(viewLifecycleOwner) { summaries ->
            updateExpensePieChart(summaries)
        }

        // X1: 观察收入分类统计 → 更新收入饼图
        viewModel.incomeCategorySummary.observe(viewLifecycleOwner) { summaries ->
            updateIncomePieChart(summaries)
        }
        
        // X2: 观察月度趋势数据，绘制折线图
        viewModel.monthlyTrend.observe(viewLifecycleOwner) { trend ->
            updateLineChart(trend)
        }
        
        // 观察选中月份的收支汇总
        viewModel.selectedMonthSummary.observe(viewLifecycleOwner) { summary ->
            if (summary != null) {
                // 更新顶部卡片为选中月份的数据
                binding.tvIncome.text = "¥${DateUtils.formatAmount(summary.income)}"
                binding.tvExpense.text = "¥${DateUtils.formatAmount(summary.expense)}"
                val balance = summary.income - summary.expense
                binding.tvBalance.text = "¥${DateUtils.formatAmount(balance)}"
            }
        }
        
        // 观察恩格尔系数
        viewModel.engelCoefficient.observe(viewLifecycleOwner) { coefficient ->
            if (coefficient != null) {
                val percentage = String.format("%.2f%%", coefficient * 100)
                binding.tvEngel.text = percentage
                
                // 根据恩格尔系数设置颜色提示
                val color = when {
                    coefficient < 0.3 -> Color.parseColor("#2ECC71") // 富裕
                    coefficient < 0.4 -> Color.parseColor("#FFC107") // 小康
                    coefficient < 0.5 -> Color.parseColor("#FF9800") // 温饱
                    else -> Color.parseColor("#F44336") // 贫困
                }
                binding.tvEngel.setTextColor(color)
            } else {
                binding.tvEngel.text = "--"
                binding.tvEngel.setTextColor(Color.WHITE)
            }
        }
    }

    /**
     * 计算结余 = 收入 - 支出
     */
    private fun updateBalance() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expense = viewModel.totalExpense.value ?: 0.0
        val balance = income - expense
        binding.tvBalance.text = "¥${DateUtils.formatAmount(balance)}"
    }

    /**
     * X1: 更新支出饼图
     */
    private fun updateExpensePieChart(summaries: List<CategorySummary>) {
        if (summaries.isEmpty()) {
            pieChartExpense.clear()
            pieChartExpense.setCenterText("暂无数据")
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
    }

    /**
     * X1: 更新收入饼图
     */
    private fun updateIncomePieChart(summaries: List<CategorySummary>) {
        if (summaries.isEmpty()) {
            pieChartIncome.clear()
            pieChartIncome.setCenterText("暂无数据")
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
    }

    /**
     * X2: 初始化折线图样式
     */
    private fun setupLineChart() {
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            
            // X轴设置
            xAxis.apply {
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }
            
            // Y轴设置
            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = Color.LTGRAY
                gridLineWidth = 0.5f
            }
            axisRight.isEnabled = false
            
            // 图例设置
            legend.apply {
                verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
                orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }
    }

    /**
     * X2: 更新折线图数据（优化：在数据点下方显示金额）
     */
    private fun updateLineChart(trend: List<MonthlyTrend>) {
        if (trend.isEmpty()) {
            lineChart.clear()
            lineChart.setNoDataText("暂无数据")
            return
        }

        val incomeEntries = mutableListOf<Entry>()
        val expenseEntries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()

        trend.forEachIndexed { index, item ->
            incomeEntries.add(Entry(index.toFloat(), item.income.toFloat()))
            expenseEntries.add(Entry(index.toFloat(), item.expense.toFloat()))
            labels.add(item.month)
        }

        val incomeSet = LineDataSet(incomeEntries, "收入").apply {
            color = Color.parseColor("#2ECC71")
            setCircleColor(Color.parseColor("#2ECC71"))
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            valueTextColor = Color.parseColor("#2ECC71")
            setDrawValues(true) // 显示数值
            valueFormatter = object : ValueFormatter() {
                private val format = DecimalFormat("#,##0")
                override fun getFormattedValue(value: Float): String {
                    return if (value > 0) format.format(value) else ""
                }
            }
        }

        val expenseSet = LineDataSet(expenseEntries, "支出").apply {
            color = Color.parseColor("#E74C3C")
            setCircleColor(Color.parseColor("#E74C3C"))
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            valueTextColor = Color.parseColor("#E74C3C")
            setDrawValues(true) // 显示数值
            valueFormatter = object : ValueFormatter() {
                private val format = DecimalFormat("#,##0")
                override fun getFormattedValue(value: Float): String {
                    return if (value > 0) format.format(value) else ""
                }
            }
        }

        lineChart.apply {
            data = LineData(incomeSet, expenseSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            xAxis.labelRotationAngle = -45f // 旋转标签避免重叠
            axisRight.isEnabled = false
            animateX(800)
            invalidate()
        }
    }
}

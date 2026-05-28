package edu.guigu.accountbook.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.guigu.accountbook.data.dao.CategorySummary
import edu.guigu.accountbook.databinding.FragmentStatisticsBinding
import edu.guigu.accountbook.ui.viewmodel.RecordViewModel
import edu.guigu.accountbook.util.DateUtils

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RecordViewModel

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

        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        // 观察总收入 → 更新显示
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvIncome.text = "¥${DateUtils.formatAmount(income)}"
            updateBalance()
        }

        // 观察总支出 → 更新显示
        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.tvExpense.text = "¥${DateUtils.formatAmount(expense)}"
            updateBalance()
        }

        // 观察支出分类统计 → 更新饼图（这里先显示文本列表）
        viewModel.expenseCategorySummary.observe(viewLifecycleOwner) { summaries ->
            updateCategoryStats(summaries)
        }
    }

    /** 计算结余 = 收入 - 支出 */
    private fun updateBalance() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expense = viewModel.totalExpense.value ?: 0.0
        val balance = income - expense
        binding.tvBalance.text = "¥${DateUtils.formatAmount(balance)}"
    }

    /** 更新支出分类统计显示 */
    private fun updateCategoryStats(summaries: List<CategorySummary>) {
        if (summaries.isEmpty()) {
            // 没有数据时显示提示
            binding.pieChart.setBackgroundColor(0xFFEEEEEE.toInt())
            binding.tvCategoryStats.text = "暂无支出数据"
        } else {
            // TODO: 进阶篇 X1 会用 MPAndroidChart 替换为真正的饼图
            // 目前先保持灰色占位背景
            binding.pieChart.setBackgroundColor(0xFFDDDDDD.toInt())
            
            // 在文本列表中显示分类统计
            val totalExpense = viewModel.totalExpense.value ?: 0.0
            val sb = StringBuilder()
            summaries.forEach { summary ->
                val percentage = if (totalExpense > 0) {
                    (summary.amount / totalExpense * 100).toInt()
                } else {
                    0
                }
                sb.append("• ${summary.category}: ¥${DateUtils.formatAmount(summary.amount)} ($percentage%)\n")
            }
            binding.tvCategoryStats.text = sb.toString()
            
            // 可以在这里添加日志查看数据
            android.util.Log.d("StatisticsFragment", "支出分类统计: ${summaries.size} 个分类")
            summaries.forEach {
                android.util.Log.d("StatisticsFragment", "${it.category}: ¥${DateUtils.formatAmount(it.amount)}")
            }
        }
    }
}

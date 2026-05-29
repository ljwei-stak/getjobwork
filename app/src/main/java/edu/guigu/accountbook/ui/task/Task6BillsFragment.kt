package edu.guigu.accountbook.ui.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.guigu.accountbook.data.database.AppDatabase
import edu.guigu.accountbook.data.model.Record
import edu.guigu.accountbook.databinding.FragmentBillsBinding
import edu.guigu.accountbook.ui.adapter.RecordAdapter
import edu.guigu.accountbook.ui.dialog.AddEditRecordDialog
import edu.guigu.accountbook.ui.viewmodel.RecordViewModel
import edu.guigu.accountbook.util.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Task6 账单页 - 包含扩展功能：
 * - X3: 月度筛选器
 * - X4: 智能搜索  
 * - X5: DiffUtil极速刷新
 */
class Task6BillsFragment : Fragment() {
    
    private var _binding: FragmentBillsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RecordViewModel
    private lateinit var adapter: RecordAdapter
    
    // X3: 月度筛选相关
    private var currentFilterStart: Long? = null
    private var currentFilterEnd: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化ViewModel
        viewModel = ViewModelProvider(requireActivity())[RecordViewModel::class.java]

        // X5: 使用支持DiffUtil的RecordAdapter
        adapter = RecordAdapter(
            onItemClick = { record -> showEditDialog(record) },
            onItemLongClick = { record -> showDeleteConfirmDialog(record) }
        )
        
        binding.rvRecords.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecords.adapter = adapter

        // 观察数据变化
        viewModel.allRecords.observe(viewLifecycleOwner) { records ->
            // 如果有筛选条件，则过滤数据
            val filteredRecords = if (currentFilterStart != null && currentFilterEnd != null) {
                records.filter { it.date in currentFilterStart!!..currentFilterEnd!! }
            } else {
                records
            }
            // X5: updateRecords内部使用DiffUtil精准更新
            adapter.updateRecords(filteredRecords)
        }

        // FAB添加按钮
        binding.fabAdd.setOnClickListener { 
            showAddDialog() 
        }
        
        // X3: 设置筛选功能
        setupFilter()
        
        // X4: 设置搜索功能
        setupSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * X3: 设置月度筛选功能
     */
    private fun setupFilter() {
        binding.chipFilter.setOnClickListener { showMonthPicker() }

        binding.chipFilter.setOnCloseIconClickListener {
            // X 按钮清除筛选
            currentFilterStart = null
            currentFilterEnd = null
            binding.chipFilter.isChecked = false
            binding.chipFilter.text = "筛选月份"
            viewModel.allRecords.observe(viewLifecycleOwner) { records ->
                adapter.updateRecords(records)
            }
        }
    }

    /**
     * X3: 显示月份选择器
     */
    private fun showMonthPicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, _ ->
                // 选中某月，设置该月第一天和最后一天的时间戳
                cal.set(year, month, 1, 0, 0, 0)
                cal.set(Calendar.MILLISECOND, 0)
                currentFilterStart = cal.timeInMillis

                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                currentFilterEnd = cal.timeInMillis

                binding.chipFilter.isChecked = true
                binding.chipFilter.text = "${year}年${month + 1}月"
                loadRecords()
                
                // 关闭搜索框
                binding.searchView.visibility = View.GONE
                binding.searchView.setQuery("", false)
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
     * X3: 加载筛选后的记录
     */
    private fun loadRecords() {
        lifecycleScope.launch {
            val records = if (currentFilterStart != null && currentFilterEnd != null) {
                // 使用日期范围查询
                AppDatabase.getInstance(requireContext()).recordDao()
                    .getRecordsByDateRange(currentFilterStart!!, currentFilterEnd!!)
            } else {
                AppDatabase.getInstance(requireContext()).recordDao().getAllRecords()
            }
            adapter.updateRecords(records)
        }
    }

    /**
     * X4: 设置搜索功能
     */
    private fun setupSearch() {
        // 长按筛选按钮显示搜索框
        binding.chipFilter.setOnLongClickListener {
            if (binding.searchView.visibility == View.VISIBLE) {
                binding.searchView.visibility = View.GONE
                binding.searchView.setQuery("", false)
                loadRecords()
            } else {
                binding.searchView.visibility = View.VISIBLE
                binding.searchView.requestFocus()
            }
            true
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                // 每输入一个字就实时过滤
                val keyword = newText?.trim() ?: ""
                lifecycleScope.launch {
                    val results = if (keyword.isBlank()) {
                        AppDatabase.getInstance(requireContext()).recordDao().getAllRecords()
                    } else {
                        AppDatabase.getInstance(requireContext()).recordDao().searchRecords(keyword)
                    }
                    adapter.updateRecords(results)
                }
                return true
            }
        })
    }

    /**
     * 显示添加记录对话框
     */
    private fun showAddDialog() {
        AddEditRecordDialog(
            editRecord = null,
            onSave = { record ->
                viewModel.insert(record)
                Toast.makeText(requireContext(), "记录已添加", Toast.LENGTH_SHORT).show()
            }
        ).show(parentFragmentManager, "AddEditDialog")
    }

    /**
     * 显示编辑记录对话框
     */
    private fun showEditDialog(record: Record) {
        AddEditRecordDialog(
            editRecord = record,
            onSave = { updatedRecord ->
                viewModel.update(updatedRecord)
                Toast.makeText(requireContext(), "记录已更新", Toast.LENGTH_SHORT).show()
            }
        ).show(parentFragmentManager, "AddEditDialog")
    }

    /**
     * 显示删除确认对话框
     */
    private fun showDeleteConfirmDialog(record: Record) {
        AlertDialog.Builder(requireContext())
            .setTitle("删除确认")
            .setMessage("确定要删除「${record.category}」的这条记录吗？\n金额：¥${DateUtils.formatAmount(record.amount)}")
            .setPositiveButton("删除") { _, _ ->
                viewModel.delete(record)
                Toast.makeText(requireContext(), "记录已删除", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}

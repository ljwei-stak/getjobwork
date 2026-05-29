package edu.guigu.accountbook.ui.task

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import edu.guigu.accountbook.databinding.FragmentTask7ImportBinding
import edu.guigu.accountbook.data.database.AppDatabase
import edu.guigu.accountbook.data.model.Record
import edu.guigu.accountbook.util.AIService
import edu.guigu.accountbook.util.TransactionParser
import kotlinx.coroutines.launch

/**
 * Task7 导入页面
 */
class Task7ImportFragment : Fragment() {
    
    private var _binding: FragmentTask7ImportBinding? = null
    private val binding get() = _binding!!
    
    // 文件选择器
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleFileImport(uri)
            }
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTask7ImportBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
    }
    
    private fun setupUI() {
        // API配置按钮
        binding.btnApiConfig.setOnClickListener {
            showApiConfigDialog()
        }
        
        // 选择文件按钮
        binding.btnSelectFile.setOnClickListener {
            showFilePicker()
        }
        
        // 开始导入按钮
        binding.btnStartImport.setOnClickListener {
            startImport()
        }
    }
    
    /**
     * 显示API配置对话框
     */
    private fun showApiConfigDialog() {
        val context = requireContext()
        
        // 创建输入框
        val apiKeyInput = EditText(context).apply {
            hint = "输入OpenAI API密钥"
            setPadding(32, 32, 32, 32)
        }
        
        val baseUrlInput = EditText(context).apply {
            setText("https://api.openai.com/v1")
            hint = "API基础URL"
            setPadding(32, 16, 32, 32)
        }
        
        val modelInput = EditText(context).apply {
            setText("gpt-3.5-turbo")
            hint = "模型名称"
            setPadding(32, 16, 32, 32)
        }
        
        // 构建对话框
        AlertDialog.Builder(context)
            .setTitle("⚙️ AI API配置")
            .setView(android.widget.LinearLayout(context).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                addView(apiKeyInput)
                addView(baseUrlInput)
                addView(modelInput)
                setPadding(32, 16, 32, 16)
            })
            .setPositiveButton("保存") { _, _ ->
                val apiKey = apiKeyInput.text.toString().trim()
                val baseUrl = baseUrlInput.text.toString().trim()
                val model = modelInput.text.toString().trim()
                
                if (apiKey.isNotEmpty()) {
                    edu.guigu.accountbook.util.AIService.configure(apiKey, baseUrl, model)
                    Toast.makeText(context, "API配置成功！", Toast.LENGTH_SHORT).show()
                    binding.btnApiConfig.text = "✅ API已配置"
                } else {
                    Toast.makeText(context, "API密钥不能为空", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .setNeutralButton("不使用AI") { _, _ ->
                Toast.makeText(
                    context,
                    "将使用本地关键词分类\n准确率较低但无需网络",
                    Toast.LENGTH_LONG
                ).show()
            }
            .show()
    }
    
    /**
     * 显示文件选择器
     */
    private fun showFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"  // 允许选择所有文件类型
            addCategory(Intent.CATEGORY_OPENABLE)
            // 不限制MIME类型，让用户可以选择任何文件
        }
        filePickerLauncher.launch(intent)
    }
    
    /**
     * 处理文件导入
     */
    private fun handleFileImport(uri: Uri) {
        val fileName = getFileName(uri)
        binding.tvSelectedFile.text = "已选择: $fileName"
        
        // 根据文件扩展名判断类型
        when {
            fileName.endsWith(".csv", ignoreCase = true) -> {
                parseCSVFile(uri)
            }
            fileName.endsWith(".xlsx", ignoreCase = true) || fileName.endsWith(".xls", ignoreCase = true) -> {
                Toast.makeText(
                    context, 
                    "Excel文件解析功能开发中...\n目前仅支持CSV格式",
                    Toast.LENGTH_LONG
                ).show()
                binding.tvStatus.text = "Excel解析功能待实现"
            }
            fileName.endsWith(".pdf", ignoreCase = true) -> {
                Toast.makeText(
                    context,
                    "PDF文件解析功能开发中...\n目前仅支持CSV格式",
                    Toast.LENGTH_LONG
                ).show()
                binding.tvStatus.text = "PDF解析功能待实现"
            }
            else -> {
                Toast.makeText(
                    context, 
                    "不支持的文件格式：${fileName.substringAfterLast('.')}\n请使用CSV/Excel/PDF文件",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    /**
     * 解析CSV文件
     */
    private fun parseCSVFile(uri: Uri) {
        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvStatus.text = "正在解析文件..."
                
                val records = TransactionParser.parseAlipayCSV(requireContext(), uri)
                
                binding.tvStatus.text = "解析完成！共 ${records.size} 条记录"
                binding.tvRecordCount.text = "${records.size} 条"
                
                // 保存解析结果到临时列表（实际应用中应该用ViewModel管理）
                importedRecords = records
                
                binding.progressBar.visibility = View.GONE
                binding.btnStartImport.isEnabled = true
                
                Toast.makeText(context, "解析成功！点击“开始导入”录入账单", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                binding.tvStatus.text = "解析失败: ${e.message}"
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    /**
     * 开始导入（AI分析并录入数据库）
     */
    private fun startImport() {
        if (importedRecords.isNotEmpty()) {
            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvStatus.text = "正在进行AI分析..."
                
                var successCount = 0
                var failCount = 0
                
                importedRecords.forEachIndexed { index, transaction ->
                    try {
                        // AI分析分类
                        val analysis = AIService.analyzeTransaction(
                            transaction.description,
                            transaction.merchant,
                            transaction.amount
                        )
                        
                        // 转换为Record并插入数据库
                        val record = Record(
                            type = transaction.type,
                            category = analysis.suggestedCategory,
                            amount = transaction.amount,
                            note = "${transaction.merchant} - ${transaction.description}",
                            date = transaction.date
                        )
                        
                        AppDatabase.getInstance(requireContext()).recordDao().insert(record)
                        successCount++
                        
                        // 更新进度
                        binding.tvStatus.text = "正在导入... ${index + 1}/${importedRecords.size}"
                    } catch (e: Exception) {
                        e.printStackTrace()
                        failCount++
                    }
                }
                
                binding.progressBar.visibility = View.GONE
                binding.tvStatus.text = "导入完成！成功: $successCount, 失败: $failCount"
                
                Toast.makeText(
                    context,
                    "导入完成！成功 $successCount 条，失败 $failCount 条",
                    Toast.LENGTH_LONG
                ).show()
                
                // 清空临时数据
                importedRecords = emptyList()
            }
        } else {
            Toast.makeText(context, "请先选择文件", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * 获取文件名
     */
    private fun getFileName(uri: Uri): String {
        var name = "未知文件"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex("_display_name")
                if (displayNameIndex >= 0) {
                    name = it.getString(displayNameIndex)
                }
            }
        }
        return name
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private var importedRecords: List<edu.guigu.accountbook.data.model.TransactionRecord> = emptyList()
    }
}

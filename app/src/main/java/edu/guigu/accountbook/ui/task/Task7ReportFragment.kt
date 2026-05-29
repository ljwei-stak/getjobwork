package edu.guigu.accountbook.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import edu.guigu.accountbook.databinding.FragmentTask7ReportBinding
import kotlinx.coroutines.launch

/**
 * Task7 财报页面
 */
class Task7ReportFragment : Fragment() {
    
    private var _binding: FragmentTask7ReportBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTask7ReportBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
    }
    
    private fun setupUI() {
        // 生成报告按钮
        binding.btnGenerateReport.setOnClickListener {
            generateReport()
        }
        
        // 导出PDF按钮
        binding.btnExportPdf.setOnClickListener {
            exportPDF()
        }
    }
    
    /**
     * 生成财务报告（简化版）
     */
    private fun generateReport() {
        lifecycleScope.launch {
            binding.tvStatus.text = "正在生成报告..."
            
            // TODO: 实现完整的财报统计逻辑
            // 1. 根据选择的类型（周/月/季/年）查询数据
            // 2. 计算总收入、总支出、分类统计等
            // 3. 调用AI生成洞察建议
            
            // 临时提示
            binding.tvStatus.text = "报告生成功能开发中...\n\n" +
                    "已完成：\n" +
                    "✅ PDF报告生成器\n" +
                    "✅ iText7集成\n\n" +
                    "待实现：\n" +
                    "⏳ 数据聚合查询\n" +
                    "⏳ AI洞察生成"
            
            Toast.makeText(
                context,
                "财报统计逻辑待实现\n目前仅支持PDF模板",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    /**
     * 导出PDF（演示版）
     */
    private fun exportPDF() {
        Toast.makeText(
            context,
            "PDF导出需要先生成报告\n请先完成财报统计逻辑",
            Toast.LENGTH_LONG
        ).show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

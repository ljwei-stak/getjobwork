package edu.guigu.accountbook.util

import android.content.Context
import android.os.Environment
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import edu.guigu.accountbook.data.model.FinancialReport
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * PDF报告生成器
 */
object PDFReportGenerator {
    
    private val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
    
    /**
     * 生成财报PDF
     */
    fun generateReport(context: Context, report: FinancialReport): File? {
        return try {
            // 创建保存目录
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val reportDir = File(downloadsDir, "AccountBookReports")
            if (!reportDir.exists()) {
                reportDir.mkdirs()
            }
            
            // 生成文件名
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
            val fileName = "${report.reportType}_report_$timestamp.pdf"
            val pdfFile = File(reportDir, fileName)
            
            // 创建PDF文档
            val writer = PdfWriter(pdfFile)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument, PageSize.A4)
            
            // 设置边距
            document.setMargins(40f, 40f, 40f, 40f)
            
            // 添加内容
            addTitle(document, report)
            addSummary(document, report)
            addCategoryBreakdown(document, report)
            addTopExpenses(document, report)
            addInsights(document, report)
            
            // 关闭文档
            document.close()
            
            pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 添加标题
     */
    private fun addTitle(document: Document, report: FinancialReport) {
        val titleMap = mapOf(
            "weekly" to "周财务报告",
            "monthly" to "月度财务报告",
            "quarterly" to "季度财务报告",
            "yearly" to "年度财务报告"
        )
        
        val titleText = titleMap[report.reportType] ?: "财务报告"
        
        val title = Paragraph(titleText)
            .setFontSize(24f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(ColorConstants.BLACK)
        
        document.add(title)
        
        val dateRange = Paragraph("${dateFormat.format(report.startDate)} - ${dateFormat.format(report.endDate)}")
            .setFontSize(12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20f)
        
        document.add(dateRange)
    }
    
    /**
     * 添加汇总信息
     */
    private fun addSummary(document: Document, report: FinancialReport) {
        val summary = Paragraph("财务汇总")
            .setFontSize(16f)
            .setBold()
            .setMarginTop(20f)
            .setMarginBottom(10f)
        
        document.add(summary)
        
        val summaryTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
            .useAllAvailableWidth()
            .setMarginBottom(20f)
        
        summaryTable.addCell(createCell("总收入", true))
        summaryTable.addCell(createCell("¥${String.format("%.2f", report.totalIncome)}", true))
        summaryTable.addCell(createCell("总支出", true))
        summaryTable.addCell(createCell("¥${String.format("%.2f", report.totalExpense)}", true))
        summaryTable.addCell(createCell("结余", true))
        summaryTable.addCell(createCell("¥${String.format("%.2f", report.balance)}", true))
        summaryTable.addCell(createCell("恩格尔系数", true))
        summaryTable.addCell(createCell("${String.format("%.2f", report.engelCoefficient * 100)}%", true))
        
        document.add(summaryTable)
    }
    
    /**
     * 添加分类统计
     */
    private fun addCategoryBreakdown(document: Document, report: FinancialReport) {
        val title = Paragraph("分类统计")
            .setFontSize(16f)
            .setBold()
            .setMarginTop(20f)
            .setMarginBottom(10f)
        
        document.add(title)
        
        val categoryTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
            .useAllAvailableWidth()
            .setMarginBottom(20f)
        
        // 表头
        categoryTable.addCell(createCell("分类", true))
        categoryTable.addCell(createCell("金额", true))
        
        // 数据行
        report.categoryBreakdown.forEach { (category, amount) ->
            categoryTable.addCell(createCell(category, false))
            categoryTable.addCell(createCell("¥${String.format("%.2f", amount)}", false))
        }
        
        document.add(categoryTable)
    }
    
    /**
     * 添加TOP支出
     */
    private fun addTopExpenses(document: Document, report: FinancialReport) {
        val title = Paragraph("最大支出TOP 10")
            .setFontSize(16f)
            .setBold()
            .setMarginTop(20f)
            .setMarginBottom(10f)
        
        document.add(title)
        
        val expenseTable = Table(UnitValue.createPercentArray(floatArrayOf(2f, 2f, 1f)))
            .useAllAvailableWidth()
            .setMarginBottom(20f)
        
        // 表头
        expenseTable.addCell(createCell("商户/说明", true))
        expenseTable.addCell(createCell("日期", true))
        expenseTable.addCell(createCell("金额", true))
        
        // 数据行（最多10条）
        report.topExpenses.take(10).forEach { record ->
            val description = if (record.description.isNotBlank()) {
                "${record.merchant} - ${record.description}"
            } else {
                record.merchant
            }
            
            expenseTable.addCell(createCell(description, false))
            expenseTable.addCell(createCell(dateFormat.format(record.date), false))
            expenseTable.addCell(createCell("¥${String.format("%.2f", record.amount)}", false))
        }
        
        document.add(expenseTable)
    }
    
    /**
     * 添加AI洞察建议
     */
    private fun addInsights(document: Document, report: FinancialReport) {
        val title = Paragraph("AI智能建议")
            .setFontSize(16f)
            .setBold()
            .setMarginTop(20f)
            .setMarginBottom(10f)
        
        document.add(title)
        
        report.insights.forEachIndexed { index, insight ->
            val insightParagraph = Paragraph("${index + 1}. $insight")
                .setFontSize(11f)
                .setMarginBottom(5f)
                .setMultipliedLeading(1.5f) // 行间距
            
            document.add(insightParagraph)
        }
    }
    
    /**
     * 创建表格单元格
     */
    private fun createCell(text: String, isHeader: Boolean): com.itextpdf.layout.element.Cell {
        val cell = com.itextpdf.layout.element.Cell().add(Paragraph(text))
        
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
            cell.setBold()
        }
        
        cell.setPadding(8f)
        return cell
    }
}

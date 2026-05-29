package edu.guigu.accountbook.util

import android.content.Context
import android.net.Uri
import com.opencsv.CSVReader
import edu.guigu.accountbook.data.model.TransactionRecord
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

/**
 * 流水单文件解析器
 * 
 * 支持格式：
 * - 支付宝 CSV
 * - 微信 Excel (待实现)
 * - 银行 PDF (待实现)
 */
object TransactionParser {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    
    /**
     * 解析支付宝CSV文件
     */
    fun parseAlipayCSV(context: Context, uri: Uri): List<TransactionRecord> {
        val records = mutableListOf<TransactionRecord>()
        
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = CSVReader(InputStreamReader(inputStream, "GBK")) // 支付宝CSV使用GBK编码
                
                // 跳过前24行表头信息
                repeat(24) { reader.readNext() }
                
                var line: Array<String>?
                while (reader.readNext().also { line = it } != null) {
                    if (line == null || line!!.size < 10) continue
                    
                    try {
                        val record = parseAlipayLine(line!!)
                        if (record != null) {
                            records.add(record)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // 跳过无法解析的行
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return records
    }
    
    /**
     * 解析支付宝CSV单行数据
     */
    private fun parseAlipayLine(line: Array<String>): TransactionRecord? {
        // 支付宝CSV格式：
        // [0]交易时间, [1]交易分类, [2]交易对方, [3]对方账号, [4]商品说明, 
        // [5]收/支, [6]金额, [7]收/付款方式, [8]交易状态, [9]交易订单号...
        
        if (line.size < 10) return null
        
        val dateStr = line[0].trim()
        val category = line[1].trim()
        val merchant = line[2].trim()
        val description = line[4].trim()
        val typeStr = line[5].trim()
        val amountStr = line[6].trim()
        val paymentMethod = line[7].trim()
        val status = line[8].trim()
        
        // 解析日期
        val date = try {
            dateFormat.parse(dateStr)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
        
        // 解析类型
        val type = when (typeStr) {
            "支出" -> 0
            "收入" -> 1
            else -> 0
        }
        
        // 解析金额
        val amount = try {
            amountStr.replace(",", "").toDouble()
        } catch (e: Exception) {
            0.0
        }
        
        return TransactionRecord(
            date = date,
            type = type,
            category = mapAlipayCategory(category),
            amount = amount,
            merchant = merchant,
            description = description,
            paymentMethod = paymentMethod,
            status = status,
            source = "alipay"
        )
    }
    
    /**
     * 映射支付宝分类到应用内分类
     */
    private fun mapAlipayCategory(alipayCategory: String): String {
        return when {
            alipayCategory.contains("餐饮") || alipayCategory.contains("美食") -> "餐饮"
            alipayCategory.contains("交通") || alipayCategory.contains("出行") -> "交通"
            alipayCategory.contains("购物") || alipayCategory.contains("百货") -> "购物"
            alipayCategory.contains("娱乐") || alipayCategory.contains("游戏") -> "娱乐"
            alipayCategory.contains("住房") || alipayCategory.contains("酒店") -> "住房"
            alipayCategory.contains("医疗") -> "医疗"
            alipayCategory.contains("教育") || alipayCategory.contains("培训") -> "教育"
            alipayCategory.contains("转账") || alipayCategory.contains("红包") -> "其他"
            else -> "其他"
        }
    }
    
    /**
     * 解析微信Excel文件（TODO: 需要Apache POI库）
     */
    fun parseWechatExcel(context: Context, uri: Uri): List<TransactionRecord> {
        // TODO: 实现微信Excel解析
        // 需要使用 Apache POI 库读取 .xlsx 文件
        return emptyList()
    }
    
    /**
     * 解析银行PDF文件（TODO: 需要PDFBox库）
     */
    fun parseBankPDF(context: Context, uri: Uri): List<TransactionRecord> {
        // TODO: 实现银行PDF解析
        // 需要使用 Apache PDFBox 库读取 PDF 文件
        return emptyList()
    }
}

package edu.guigu.accountbook.data.model

/**
 * 流水单记录（从支付宝/微信/银行导入的原始数据）
 */
data class TransactionRecord(
    val id: Long = 0,
    val date: Long,              // 交易时间戳
    val type: Int,               // 类型：0=支出, 1=收入
    val category: String,        // 分类（AI分析后得出）
    val amount: Double,          // 金额
    val merchant: String,        // 商户名称
    val description: String,     // 商品说明
    val paymentMethod: String,   // 支付方式
    val status: String,          // 交易状态
    val source: String           // 来源：alipay/wechat/bank
)

/**
 * AI分析结果
 */
data class AIAnalysisResult(
    val originalText: String,           // 原始描述
    val suggestedCategory: String,      // 建议分类
    val confidence: Float,              // 置信度 (0-1)
    val tags: List<String>,             // 标签列表
    val analysis: String                // AI分析说明
)

/**
 * 财报统计数据
 */
data class FinancialReport(
    val reportType: String,             // 报告类型：weekly/monthly/quarterly/yearly
    val startDate: Long,                // 开始日期
    val endDate: Long,                  // 结束日期
    val totalIncome: Double,            // 总收入
    val totalExpense: Double,           // 总支出
    val balance: Double,                // 结余
    val categoryBreakdown: Map<String, Double>,  // 分类统计
    val topExpenses: List<TransactionRecord>,    // 最大支出TOP10
    val insights: List<String>,         // AI洞察建议
    val engelCoefficient: Double        // 恩格尔系数
)

/**
 * AI API配置
 */
data class AIConfig(
    var apiKey: String = "",
    var baseUrl: String = "https://api.openai.com/v1",
    var model: String = "gpt-3.5-turbo",
    var isEnabled: Boolean = false
)

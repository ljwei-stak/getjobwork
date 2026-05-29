package edu.guigu.accountbook.util

import edu.guigu.accountbook.data.model.AIAnalysisResult
import edu.guigu.accountbook.data.model.AIConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject

/**
 * AI API服务
 * 
 * 支持OpenAI兼容的API接口
 */
object AIService {
    
    private var config = AIConfig()
    
    /**
     * 配置AI API
     */
    fun configure(apiKey: String, baseUrl: String = "https://api.openai.com/v1", model: String = "gpt-3.5-turbo") {
        config = AIConfig(
            apiKey = apiKey,
            baseUrl = baseUrl,
            model = model,
            isEnabled = apiKey.isNotBlank()
        )
    }
    
    /**
     * 分析交易记录，智能分类
     */
    suspend fun analyzeTransaction(description: String, merchant: String, amount: Double): AIAnalysisResult {
        return withContext(Dispatchers.IO) {
            if (!config.isEnabled) {
                // 如果未配置API，返回默认分析
                return@withContext getDefaultAnalysis(description, merchant)
            }
            
            try {
                val prompt = buildAnalysisPrompt(description, merchant, amount)
                val response = callAIAPI(prompt)
                parseAIResponse(response, description)
            } catch (e: Exception) {
                e.printStackTrace()
                getDefaultAnalysis(description, merchant)
            }
        }
    }
    
    /**
     * 生成财报洞察建议
     */
    suspend fun generateInsights(reportData: String): List<String> {
        return withContext(Dispatchers.IO) {
            if (!config.isEnabled) {
                return@withContext listOf("请配置AI API以获取智能建议")
            }
            
            try {
                val prompt = """
                    请分析以下财务数据并提供3条简洁的理财建议：
                    $reportData
                    
                    要求：
                    1. 每条建议不超过50字
                    2. 用中文回答
                    3. 直接列出建议，不要其他说明
                """.trimIndent()
                
                val response = callAIAPI(prompt)
                parseInsightsResponse(response)
            } catch (e: Exception) {
                e.printStackTrace()
                listOf("分析失败，请检查API配置")
            }
        }
    }
    
    /**
     * 构建分析提示词
     */
    private fun buildAnalysisPrompt(description: String, merchant: String, amount: Double): String {
        return """
            请分析以下交易记录，判断其消费分类：
            
            商户：$merchant
            说明：$description
            金额：¥$amount
            
            可选分类：餐饮、交通、购物、娱乐、住房、医疗、教育、工资、兼职、理财、红包、其他
            
            请以JSON格式返回：
            {
                "category": "分类名称",
                "confidence": 0.95,
                "tags": ["标签1", "标签2"],
                "analysis": "简短分析说明"
            }
        """.trimIndent()
    }
    
    /**
     * 调用AI API
     */
    private fun callAIAPI(prompt: String): String {
        val url = URL("${config.baseUrl}/chat/completions")
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer ${config.apiKey}")
            connection.doOutput = true
            
            val jsonRequest = JSONObject().apply {
                put("model", config.model)
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
                put("temperature", 0.7)
                put("max_tokens", 500)
            }
            
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonRequest.toString())
                writer.flush()
            }
            
            val responseCode = connection.responseCode
            if (responseCode == 200) {
                return connection.inputStream.bufferedReader().readText()
            } else {
                throw Exception("API请求失败: $responseCode")
            }
        } finally {
            connection.disconnect()
        }
    }
    
    /**
     * 解析AI响应
     */
    private fun parseAIResponse(response: String, originalText: String): AIAnalysisResult {
        val jsonObject = JSONObject(response)
        val choices = jsonObject.getJSONArray("choices")
        if (choices.length() == 0) {
            return getDefaultAnalysis(originalText, "")
        }
        
        val message = choices.getJSONObject(0).getJSONObject("message")
        val content = message.getString("content")
        
        // 尝试解析JSON内容
        return try {
            val jsonStart = content.indexOf("{")
            val jsonEnd = content.lastIndexOf("}")
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                val jsonStr = content.substring(jsonStart, jsonEnd + 1)
                val result = JSONObject(jsonStr)
                
                AIAnalysisResult(
                    originalText = originalText,
                    suggestedCategory = result.optString("category", "其他"),
                    confidence = result.optDouble("confidence", 0.8).toFloat(),
                    tags = parseTags(result.optJSONArray("tags")),
                    analysis = result.optString("analysis", "AI分析完成")
                )
            } else {
                getDefaultAnalysis(originalText, "")
            }
        } catch (e: Exception) {
            getDefaultAnalysis(originalText, "")
        }
    }
    
    /**
     * 解析洞察建议响应
     */
    private fun parseInsightsResponse(response: String): List<String> {
        val jsonObject = JSONObject(response)
        val choices = jsonObject.getJSONArray("choices")
        if (choices.length() == 0) {
            return listOf("无法生成建议")
        }
        
        val message = choices.getJSONObject(0).getJSONObject("message")
        val content = message.getString("content")
        
        // 按行分割，过滤空行
        return content.split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("{") && !it.startsWith("}") }
            .take(5) // 最多5条建议
    }
    
    /**
     * 解析标签数组
     */
    private fun parseTags(tagsArray: JSONArray?): List<String> {
        val tags = mutableListOf<String>()
        if (tagsArray != null) {
            for (i in 0 until tagsArray.length()) {
                tags.add(tagsArray.getString(i))
            }
        }
        return tags
    }
    
    /**
     * 默认分析（无API时使用）
     */
    private fun getDefaultAnalysis(description: String, merchant: String): AIAnalysisResult {
        val category = when {
            description.contains("餐饮") || description.contains("美食") || description.contains("饭") -> "餐饮"
            description.contains("交通") || description.contains("地铁") || description.contains("公交") -> "交通"
            description.contains("购物") || description.contains("超市") || description.contains("商城") -> "购物"
            description.contains("游戏") || description.contains("娱乐") -> "娱乐"
            else -> "其他"
        }
        
        return AIAnalysisResult(
            originalText = description,
            suggestedCategory = category,
            confidence = 0.6f,
            tags = listOf("自动分类"),
            analysis = "基于关键词的简单分类（未使用AI）"
        )
    }
}

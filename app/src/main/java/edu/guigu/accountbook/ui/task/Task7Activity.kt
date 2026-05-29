package edu.guigu.accountbook.ui.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import edu.guigu.accountbook.R
import edu.guigu.accountbook.databinding.ActivityTaskBinding

/**
 * Task7: AI智能账单助手
 * 
 * 功能模块：
 * 1. AI-API配置 - 用户输入API密钥
 * 2. 流水单导入 - 支持支付宝/微信/银行流水
 * 3. AI智能录入 - 自动解析并录入账单
 * 4. 财报分析 - 生成周/月/季/年报
 * 5. PDF导出 - 导出财务报告
 */
class Task7Activity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTaskBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 设置标题
        binding.toolbar.title = "Task7 - AI智能账单助手"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // 设置ViewPager2和TabLayout
        setupViewPager()
    }
    
    private fun setupViewPager() {
        val fragments = listOf(
            Task7ImportFragment(),          // Tab 1: 导入
            Task7AnalysisFragment(),        // Tab 2: AI分析
            Task7BillsFragment(),           // Tab 3: 账单
            Task7StatisticsFragment(),      // Tab 4: 统计
            Task7ReportFragment()           // Tab 5: 财报
        )
        
        val titles = listOf("导入", "AI分析", "账单", "统计", "财报")
        
        val adapter = Task7ViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

package edu.guigu.accountbook.ui.task

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import edu.guigu.accountbook.R
import edu.guigu.accountbook.databinding.ActivityTaskBinding
import edu.guigu.accountbook.ui.fragment.BillsFragment
import edu.guigu.accountbook.ui.fragment.StatisticsFragment

/**
 * Task6: 让记账本更专业 —— 可选扩展任务集合
 * 
 * 包含以下扩展功能：
 * - X1: 饼图大师 - 支出分类占比可视化（已完成）
 * - X2: 趋势分析师 - 月度收支趋势折线图
 * - X3: 月度筛选器 - 只看某个月的记录
 * - X4: 智能搜索 - 按关键词搜索记录
 * - X5: 极速刷新 - DiffUtil精准更新列表
 */
class Task6Activity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 设置ViewPager2适配器
        binding.viewPager.adapter = ViewPagerAdapter(this)
        
        // TabLayout和ViewPager2联动
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_bills)
                1 -> getString(R.string.tab_statistics)
                else -> ""
            }
        }.attach()
    }

    /**
     * ViewPager2适配器
     * 提供两个Fragment：账单页和统计页
     */
    class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> Task6BillsFragment()      // 带筛选、搜索、DiffUtil优化的账单页
                1 -> Task6StatisticsFragment()  // 带饼图、趋势图的统计页
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}

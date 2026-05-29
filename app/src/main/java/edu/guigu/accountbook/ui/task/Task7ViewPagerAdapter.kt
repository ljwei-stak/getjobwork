package edu.guigu.accountbook.ui.task

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Task7 ViewPager适配器
 */
class Task7ViewPagerAdapter(
    activity: AppCompatActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {
    
    override fun getItemCount(): Int = fragments.size
    
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

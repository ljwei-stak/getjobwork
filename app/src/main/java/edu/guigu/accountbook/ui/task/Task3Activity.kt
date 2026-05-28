package edu.guigu.accountbook.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import edu.guigu.accountbook.R
import edu.guigu.accountbook.databinding.ActivityTaskBinding
import edu.guigu.accountbook.databinding.FragmentBillsBinding
import edu.guigu.accountbook.ui.adapter.RecordAdapter
import edu.guigu.accountbook.ui.viewmodel.RecordViewModel

class Task3Activity : AppCompatActivity() {

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

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_bills)
                1 -> getString(R.string.tab_statistics)
                else -> ""
            }
        }.attach()
    }

    class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> Task3BillsFragment()
                1 -> Task1Activity.Task1StatisticsFragment()
                else -> throw IllegalArgumentException()
            }
        }
    }

    class Task3BillsFragment : Fragment() {
        private var _binding: FragmentBillsBinding? = null
        private val binding get() = _binding!!
        private lateinit var viewModel: RecordViewModel
        private lateinit var adapter: RecordAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding = FragmentBillsBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            viewModel = ViewModelProvider(requireActivity())[RecordViewModel::class.java]

            adapter = RecordAdapter(
                onItemClick = { /* 编辑功能在 Task4 实现 */ },
                onItemLongClick = { /* 删除功能在 Task4 实现 */ }
            )
            binding.rvRecords.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRecords.adapter = adapter

            viewModel.allRecords.observe(viewLifecycleOwner) { records ->
                adapter.updateRecords(records)
            }

            binding.fabAdd.setOnClickListener {
                Toast.makeText(requireContext(), "添加功能明日解锁！", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
}

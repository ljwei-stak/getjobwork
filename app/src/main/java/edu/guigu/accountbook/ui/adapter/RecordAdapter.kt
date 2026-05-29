package edu.guigu.accountbook.ui.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.guigu.accountbook.R
import edu.guigu.accountbook.data.model.Record
import edu.guigu.accountbook.databinding.ItemRecordBinding
import edu.guigu.accountbook.util.DateUtils

/**
 * RecyclerView 适配器 - 支持 DiffUtil 精准更新
 * 
 * X5: 使用 DiffUtil 替代 notifyDataSetChanged()
 * - 只刷新变化的部分
 * - 自动播放插入/删除动画
 * - 提升列表性能
 */
class RecordAdapter(
    private val onItemClick: (Record) -> Unit,
    private val onItemLongClick: (Record) -> Unit
) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    private val records = mutableListOf<Record>()

    /**
     * X5: 使用 DiffUtil 精准更新列表
     * 
     * 对比旧列表和新列表，只通知 RecyclerView 变化的部分，
     * 未变化的 item 不会重新 onBindViewHolder。
     */
    fun updateRecords(newRecords: List<Record>) {
        val diffResult = DiffUtil.calculateDiff(
            RecordDiffCallback(records, newRecords)
        )
        records.clear()
        records.addAll(newRecords)
        diffResult.dispatchUpdatesTo(this)  // ← 内部调用 notifyItemInserted/Removed/Changed
    }

    override fun getItemCount(): Int = records.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    /**
     * X5: 利用 Payload 做局部刷新（可选优化）
     * 
     * 当 areItemsTheSame 为 true 但 areContentsTheSame 为 false 时，
     * 会调用这个带 payloads 的方法，可以只更新变化的字段。
     */
    override fun onBindViewHolder(
        holder: RecordViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            // 没有 payload，完整绑定
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        // 有 payload，只更新变化的字段
        val record = records[position]
        for (payload in payloads) {
            (payload as? Bundle)?.let { bundle ->
                bundle.getDouble("amount", -1.0).let {
                    if (it >= 0) holder.updateAmount(record)
                }
                bundle.getString("category")?.let { holder.updateCategory(record) }
                bundle.getString("note")?.let { holder.updateNote(record) }
            }
        }
    }

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRecordBinding.bind(itemView)

        fun bind(record: Record) {
            val color = Record.getCategoryColor(record.category)

            binding.tvCategoryIcon.text = record.category.first().toString()
            (binding.tvCategoryIcon.background.mutate() as? android.graphics.drawable.GradientDrawable)
                ?.setColor(color)

            binding.tvCategoryName.text = record.category
            binding.tvDate.text = DateUtils.formatDate(record.date)

            if (!record.note.isNullOrBlank()) {
                binding.tvNote.text = "备注：${record.note}"
                binding.tvNote.visibility = View.VISIBLE
            } else {
                binding.tvNote.visibility = View.GONE
            }

            val isIncome = record.type == Record.TYPE_INCOME
            val prefix = if (isIncome) "+" else "-"
            binding.tvAmount.text = "${prefix}¥${DateUtils.formatAmount(record.amount)}"
            binding.tvAmount.setTextColor(Color.parseColor(if (isIncome) "#2ECC71" else "#E74C3C"))

            itemView.setOnClickListener { onItemClick(record) }
            itemView.setOnLongClickListener { onItemLongClick(record); true }
        }

        /**
         * X5: 局部更新金额
         */
        fun updateAmount(record: Record) {
            val isIncome = record.type == Record.TYPE_INCOME
            val prefix = if (isIncome) "+" else "-"
            binding.tvAmount.text = "${prefix}¥${DateUtils.formatAmount(record.amount)}"
            binding.tvAmount.setTextColor(
                Color.parseColor(if (isIncome) "#2ECC71" else "#E74C3C")
            )
        }

        /**
         * X5: 局部更新分类
         */
        fun updateCategory(record: Record) {
            binding.tvCategoryIcon.text = record.category.first().toString()
            binding.tvCategoryName.text = record.category
        }

        /**
         * X5: 局部更新备注
         */
        fun updateNote(record: Record) {
            if (!record.note.isNullOrBlank()) {
                binding.tvNote.text = "备注：${record.note}"
                binding.tvNote.visibility = View.VISIBLE
            } else {
                binding.tvNote.visibility = View.GONE
            }
        }
    }

    /**
     * X5: DiffUtil 回调 - 告诉 DiffUtil 如何判断两条记录是否相同
     */
    class RecordDiffCallback(
        private val oldList: List<Record>,
        private val newList: List<Record>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        /**
         * 判断两个 position 是否是同一条记录（用 id 比较）
         */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        /**
         * 判断同一条记录的内容是否发生变化（所有字段比较）
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]
            return old.type == new.type &&
                    old.category == new.category &&
                    old.amount == new.amount &&
                    old.note == new.note &&
                    old.date == new.date
        }

        /**
         * 当 areItemsTheSame 为 true 但 areContentsTheSame 为 false 时，
         * 返回变化的内容用于局部刷新（可选优化）
         */
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            // 只返回变化的字段，避免整条重新绑定
            val bundle = Bundle()
            if (old.amount != new.amount) bundle.putDouble("amount", new.amount)
            if (old.type != new.type) bundle.putInt("type", new.type)
            if (old.category != new.category) bundle.putString("category", new.category)
            if (old.note != new.note) bundle.putString("note", new.note)
            if (old.date != new.date) bundle.putLong("date", new.date)
            return if (bundle.isEmpty) null else bundle
        }
    }
}

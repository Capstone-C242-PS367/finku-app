package com.capstone.finku.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.finku.R
import com.capstone.finku.data.response.ResultItem
import com.capstone.finku.databinding.ItemRecapBinding
import com.capstone.finku.ui.customview.DatePickerView

class ListOcrResultAdapter(
    private val listResult: List<ResultItem?>?,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<ListOcrResultAdapter.ListViewHolder>() {
    private var dateSelectionMap: MutableMap<Int, String> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRecapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listResult?.size ?: 0

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val result = listResult?.get(position)
        holder.binding.apply {
            result?.let {
                edTitle.setText(it.title)
                edAmount.setText(it.amount.toString())
                rbType.check(
                    when (it.type) {
                        "DB" -> R.id.rbDb
                        "CR" -> R.id.rbCr
                        else -> -1
                    }
                )
                edDate.setText(dateSelectionMap[position] ?: it.date)

                edTitle.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        it.title = s.toString()
                    }
                })

                edAmount.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        it.amount = s.toString()
                    }
                })

                rbType.setOnCheckedChangeListener{_, checkedId ->
                    it.type = when (checkedId) {
                        R.id.rbCr -> "CR"
                        R.id.rbDb -> "DB"
                        else -> ""
                    }
                }

                edDate.setOnClickListener {
                    val dateFragment = DatePickerView { selectedDate ->
                        dateSelectionMap[position] = selectedDate
                        notifyItemChanged(position)
                    }
                    dateFragment.show(fragmentManager, "datePicker")
                }
            }
        }
    }

    fun getUpdatedResults(): List<ResultItem>? {
        return listResult?.mapIndexed { index, result ->
            ResultItem(
                date = dateSelectionMap[index] ?: result?.date.orEmpty(),
                title = result?.title.orEmpty(),
                type = result?.type.orEmpty(),
                amount = result?.amount?.replace(".", "")?.toInt().toString(),
                currency = result?.currency.orEmpty(),
                category = result?.category.orEmpty()
            )
        }
    }

    class ListViewHolder(var binding: ItemRecapBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
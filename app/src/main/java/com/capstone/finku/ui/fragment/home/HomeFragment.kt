package com.capstone.finku.ui.fragment.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.finku.data.TransactionViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.databinding.FragmentHomeBinding
import com.capstone.finku.utils.formatToRupiah
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var pieChart: PieChart
    private val homeViewModel: HomeViewModel by viewModels {
        TransactionViewModelFactory(Injection.provideTransactionRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initHome()
    }

    private fun setupRecyclerView() {
        binding.rvRecaps.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initHome() {
        val pref = UserPreference.getInstance(requireContext().dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
        pieChart = binding.summaryPieChart;

        homeViewModel.apply {
            getTransactions(user.id)

            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.summaryPieChart.visibility = View.GONE
                    binding.loadingSummary.visibility = View.VISIBLE
                    binding.tvLoading.visibility = View.VISIBLE
                    binding.tvEmptyState.visibility = View.GONE
                } else {
                    binding.loadingSummary.visibility = View.GONE
                    binding.tvLoading.visibility = View.GONE
                    binding.summaryPieChart.visibility = View.VISIBLE
                }
            }

            totalCredit.observe(viewLifecycleOwner) {
                pieChart.addPieSlice(
                    PieModel(
                        "Total Credit", it.toFloat(), Color.parseColor(
                            "#17C653"
                        )
                    )
                )
                binding.tvTotalCreditValue.text = formatToRupiah(it)
            }

            totalDebt.observe(viewLifecycleOwner) {
                pieChart.addPieSlice(
                    PieModel(
                        "Total Debt", it.toFloat(), Color.parseColor(
                            "#F8285A"
                        )
                    )
                )
                binding.tvTotalDebtValue.text = formatToRupiah(it)
            }

            message.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            transactionList.observe(viewLifecycleOwner) {
                if (it?.data?.isEmpty() == true || it?.data == null) {
                    binding.tvEmptyState.visibility = View.VISIBLE
                    binding.summaryPieChart.visibility = View.GONE
                } else {
                    binding.tvEmptyState.visibility = View.GONE
                    binding.summaryPieChart.visibility = View.VISIBLE
                }
            }
        }

        binding.apply {
            val name = if (user.name.isEmpty()) {
                user.email
            } else {
                user.name
            }
            tvGreeting.text = "Hi, $name"
        }

        pieChart.startAnimation()
    }

}
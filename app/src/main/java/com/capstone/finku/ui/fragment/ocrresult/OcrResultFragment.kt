package com.capstone.finku.ui.fragment.ocrresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.finku.R
import com.capstone.finku.data.TransactionViewModelFactory
import com.capstone.finku.data.di.Injection
import com.capstone.finku.data.pref.UserPreference
import com.capstone.finku.data.pref.dataStore
import com.capstone.finku.data.request.PostTransactionRequest
import com.capstone.finku.data.response.PredictResponse
import com.capstone.finku.data.response.ResultItem
import com.capstone.finku.databinding.FragmentOcrResultBinding
import com.capstone.finku.ui.adapter.ListOcrResultAdapter
import com.capstone.finku.ui.fragment.home.HomeFragment
import com.capstone.finku.utils.formatToRupiah
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class OcrResultFragment : Fragment() {
    private lateinit var binding: FragmentOcrResultBinding
    private val ocrResultViewModel: OcrResultViewModel by viewModels {
        TransactionViewModelFactory(Injection.provideTransactionRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOcrResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.title = "Result"

        val predictResult: PredictResponse? = arguments?.getParcelable(EXTRA_PREDICT_RESULT)

        binding.apply {
            btnSave.setOnClickListener {
                val userPref = UserPreference.getInstance(requireContext().dataStore)
                val userId = runBlocking { userPref.getSession().first() }.id

                val adapter = binding.rvResult.adapter as ListOcrResultAdapter
                val updatedResults = adapter.getUpdatedResults()

                val resultItems: List<ResultItem> = updatedResults?.mapNotNull { result ->
                    result?.let {
                        ResultItem(
                            date = it.date,
                            title = it.title,
                            type = it.type,
                            amount = it.amount?.replace(".", "")?.toInt().toString(),
                            currency = it.currency,
                            category = it.category
                        )
                    }
                } ?: emptyList()
                val requestBody = PostTransactionRequest(
                    userId = userId,
                    data = resultItems
                )

                ocrResultViewModel.storeTransaction(requestBody)
            }

            predictResult?.data?.apply {
                tvTotalDebitAmount.text = formatToRupiah(totalDebit)
                tvTotalCreditAmount.text = formatToRupiah(totalCredit)
                tvTotalDifferenceAmount.text = formatToRupiah(difference)

                setupRecyclerView(result)
            }
        }

        ocrResultViewModel.message.observe(viewLifecycleOwner) {
            showToast(it)
        }

        ocrResultViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.btnSave.isEnabled = !it
            binding.loadingSubmit.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        ocrResultViewModel.transactionResult.observe(viewLifecycleOwner) {
            if (it != null) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment())
                    .commit()
            }
        }
    }

    private fun setupRecyclerView(listResult: List<ResultItem?>?) {
        binding.rvResult.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListOcrResultAdapter(listResult, childFragmentManager)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        var EXTRA_PREDICT_RESULT = "extra_predict_result"
    }
}
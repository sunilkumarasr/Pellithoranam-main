package com.stranger_sparks.view.activities.ui.fragments.dating_matches

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.adapterrs.DatingMatchesAdapter
import com.stranger_sparks.databinding.FragmentAllDatingMatchesBinding
import com.stranger_sparks.datamodels.TransactionDataModel
import com.stranger_sparks.view.activities.ui.activities.dating_matches.PageViewModel

class AllDatingMatchesFragment : Fragment() {
    private var _binding: FragmentAllDatingMatchesBinding? = null
    private lateinit var pageViewModel: PageViewModel
    private lateinit var  datingMatchesAdapter: DatingMatchesAdapter
    private var dataList = mutableListOf<TransactionDataModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): AllDatingMatchesFragment {
            return AllDatingMatchesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private lateinit var viewModel: AllDatingMatchesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAllDatingMatchesBinding.inflate(inflater, container, false)
        val root = binding.root

        /*val textView: TextView = binding.sectionLabel
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvAllDatingMatches.layoutManager = LinearLayoutManager(requireContext())
        datingMatchesAdapter = DatingMatchesAdapter(requireContext())
        binding.rcvAllDatingMatches.adapter = datingMatchesAdapter

        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "21,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Debit Card", "22,659", "Cancel"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Internet Bank", "23,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Mobile Wallet", "27,659", "Cancel"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "20,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "5,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Debit Card", "24,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Internet Bank", "8,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "6,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Debit Card", "3,659", "Cancel"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Wallet", "1,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Reward Points", "10,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Promotional Points", "659", "Completed"))
        datingMatchesAdapter.setDataList(dataList)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
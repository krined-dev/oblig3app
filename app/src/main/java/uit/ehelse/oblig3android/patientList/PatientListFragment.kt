package uit.ehelse.oblig3android.patientList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.adapters.PatientListAdapter
import uit.ehelse.oblig3android.databinding.FragmentPatientListBinding
import uit.ehelse.oblig3android.databinding.PatientRowItemBinding
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class PatientListFragment() : Fragment() {

    private var _binding: FragmentPatientListBinding? = null

    private val viewModel by viewModels<PatientListViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPatientListBinding.inflate(inflater, container, false)
        // get patients async in viewmodelscope
        lifecycleScope.launch {
            viewModel.getPatientsAsync()
        }

        // observe live data
        viewModel.patients.observe(viewLifecycleOwner) {
            // set adapter
            binding.patientList.adapter = PatientListAdapter(it)

//            binding.patientList.adapter = PatientListAdapter(PatientListAdapter.OnClickListener)
//            binding.patientList.adapter = PatientListAdapter(PatientListAdapter.OnClickListener {
//                viewModel.patientsList
//            })
            binding.patientList.layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.patientList.setOnClickListener {
//            viewModel.patientsList.lastIndex
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.refreshButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getPatientsAsync()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
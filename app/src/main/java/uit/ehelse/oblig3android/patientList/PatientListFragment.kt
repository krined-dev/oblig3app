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
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.adapters.PatientListAdapter
import uit.ehelse.oblig3android.api.TokenManager
import uit.ehelse.oblig3android.api.httpClient
import uit.ehelse.oblig3android.databinding.FragmentPatientListBinding

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
    ): View {

        if (TokenManager.username == null) {
            val action = PatientListFragmentDirections.actionFirstFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        _binding = FragmentPatientListBinding.inflate(inflater, container, false)
        // get patients async in viewmodelscope
        lifecycleScope.launch {
            viewModel.getPatientsAsync()
        }

        // observe live data
        viewModel.patients.observe(viewLifecycleOwner) {
            // set adapter
            val adapter = PatientListAdapter(it)
            binding.patientList.adapter = adapter

//            binding.patientList.adapter = PatientListAdapter(PatientListAdapter.OnClickListener)
//            binding.patientList.adapter = PatientListAdapter(PatientListAdapter.OnClickListener {
//                viewModel.patientsList
//            })
            binding.patientList.layoutManager = LinearLayoutManager(context)

            adapter.registerSymptomOnClickListener = { patient ->
//                Toast.makeText(context, "Clicked ${patient.id}", Toast.LENGTH_SHORT).show()
                val action = PatientListFragmentDirections.actionFirstFragmentToSecondFragment(patient.id)
                findNavController().navigate(action)
            }

            adapter.deletePatientOnClickListener = { patient ->
                val deleteLink = patient.links.find { link -> link.method.rel == "delete" }
                if (deleteLink != null) {
                    lifecycleScope.launch {
                        val result = httpClient().deletePatient(deleteLink.href)
                        result.map {
                            viewModel.getPatientsAsync()
                        }.mapLeft {
                            Toast.makeText(context, "Error: Insufficient privileges", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }

            adapter.updatePatientOnClickListener = { patient ->
                val updateLink = patient.links.find { link -> link.method.rel == "update" }
                if (updateLink != null) {
                    val action = PatientListFragmentDirections.actionFirstFragmentToUpdateWardId(patient.id, updateLink.href)
                    findNavController().navigate(action)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.patientList.setOnClickListener {
//            viewModel.patientsList.lastIndex
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.addPatient.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_newPatient)
        }

        binding.refreshButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getPatientsAsync()
            }
        }

        binding.logout.setOnClickListener {
            TokenManager.username = null
            TokenManager.password = null
            TokenManager.token = null
            val action = PatientListFragmentDirections.actionFirstFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
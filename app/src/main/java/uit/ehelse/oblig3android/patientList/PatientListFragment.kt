package uit.ehelse.oblig3android.patientList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.databinding.FragmentPatientListBinding
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
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.patientList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.refreshButton.setOnClickListener {
            viewModel.getPatients()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
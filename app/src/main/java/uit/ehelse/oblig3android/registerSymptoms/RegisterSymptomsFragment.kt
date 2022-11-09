package uit.ehelse.oblig3android.registerSymptoms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.adapters.PatientListAdapter
import uit.ehelse.oblig3android.databinding.FragmentRegisterSymptomsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterSymptomsFragment : Fragment() {

    private var _binding: FragmentRegisterSymptomsBinding? = null
    private val viewModel by viewModels<RegisterSymptomsViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterSymptomsBinding.inflate(inflater, container, false)

//        lifecycleScope.launch {
//            viewModel.getPatient()
//        }

        viewModel.selectedPatient.observe(viewLifecycleOwner) {

        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


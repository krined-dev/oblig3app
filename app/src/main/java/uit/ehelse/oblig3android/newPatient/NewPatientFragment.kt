package uit.ehelse.oblig3android.newPatient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.databinding.FragmentNewPatientBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewPatientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewPatientFragment() : Fragment() {

    private var _binding: FragmentNewPatientBinding? = null

    private val viewModel by viewModels<NewPatientViewModel>()


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentNewPatientBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPatientButton.setOnClickListener {
            val ssn = viewModel.setPatientSsn(binding.newPatSsn.text.toString())
            viewModel.setWardId(binding.newWardId.text.toString())
            if (viewModel.patientSsn.value.isNullOrEmpty() || viewModel.wardId.value.isNullOrEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                if (!ssn) {
                    Toast.makeText(context, "Invalid ssn", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.setWardId(binding.newWardId.text.toString())
                    lifecycleScope.launch {
                        viewModel.registerPatient()
                        Toast.makeText(context, "Patient already registered", Toast.LENGTH_SHORT).show() // Not always the case, but ok for now
                        // navigate back
                        findNavController().navigate(R.id.action_newPatient_to_FirstFragment)
                    }
                }
            }
        }
    }
}
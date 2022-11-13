package uit.ehelse.oblig3android.registerSymptoms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uit.ehelse.oblig3android.databinding.FragmentRegisterSymptomsBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterSymptomsFragment : Fragment() {

    private var _binding: FragmentRegisterSymptomsBinding? = null
    private val viewModel by viewModels<RegisterSymptomsViewModel>()
    val args: RegisterSymptomsFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterSymptomsBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val patient = args.ssn

        binding.checkboxAchesAndPains.setOnClickListener {
            viewModel.setAchesAndPains(binding.checkboxAchesAndPains.isChecked)
        }

        binding.checkboxCoughing.setOnClickListener {
            viewModel.setCough(binding.checkboxCoughing.isChecked)
        }

        binding.checkboxDiarrhoea.setOnClickListener {
            viewModel.setDiarrhea(binding.checkboxDiarrhoea.isChecked)
        }

        binding.checkboxFever.setOnClickListener {
            viewModel.setFever(binding.checkboxFever.isChecked)
        }

        binding.checkboxHeadache.setOnClickListener {
            viewModel.setHeadache(binding.checkboxHeadache.isChecked)
        }

        binding.checkboxLossOfTasteAndSmell.setOnClickListener {
            viewModel.setLossOfSmellAndTaste(binding.checkboxLossOfTasteAndSmell.isChecked)
        }

        binding.checkboxBreathingComplication.setOnClickListener {
            viewModel.setBreathingComplication(binding.checkboxBreathingComplication.isChecked)
        }

        binding.checkboxSoreThroat.setOnClickListener {
            viewModel.setSoreThroat(binding.checkboxSoreThroat.isChecked)
        }

        binding.checkboxTiredness.setOnClickListener {
            viewModel.setTiredness(binding.checkboxTiredness.isChecked)
        }

        binding.checkboxRashesOrDiscoloration.setOnClickListener {
            viewModel.setRashesOrDiscoloration(binding.checkboxRashesOrDiscoloration.isChecked)
        }

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                    viewModel.submitSymptomsAsync(patient)
                    val response = viewModel.response.value

                // Toast response
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                Log.d("RegisterSymptoms", "Response: $response")
                // navigate back with nav

                findNavController().navigate(RegisterSymptomsFragmentDirections.actionSecondFragmentToFirstFragment())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


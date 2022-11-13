package uit.ehelse.oblig3android.updateWardId

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.api.httpClient
import uit.ehelse.oblig3android.databinding.FragmentUpdateWardIdBinding


@AndroidEntryPoint
class UpdateWardId : Fragment() {

    private var _binding: FragmentUpdateWardIdBinding? = null
    private val binding get() = _binding!!
    val args: UpdateWardIdArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateWardIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val href = args.updateUrl

        binding.updatePatient.setOnClickListener {
            if (binding.updateWardId.text.toString().isNotEmpty()) {
                val wardId = binding.updateWardId.text.toString()
                lifecycleScope.launch {
                    val response = httpClient().updateWardIdForPatient(href, wardId)
                    if (response.isRight()) {
                        Toast.makeText(context, "Ward ID updated", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_updateWardId_to_FirstFragment)
                    } else {
                        Toast.makeText(context, "Ward ID not updated", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_updateWardId_to_FirstFragment)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a ward id", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
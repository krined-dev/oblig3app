package uit.ehelse.oblig3android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.patientList.PatientModel
import javax.inject.Inject
import javax.inject.Singleton

class PatientListAdapter (private val data: List<PatientModel>) : RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.patient_id)
        val button: View = view.findViewById(R.id.register_symtpom_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.patient_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patient = data[position]
        val textView = holder.id
        textView.text = patient.id
        val button = holder.button
        button.isEnabled = true
    }

    override fun getItemCount() = data.size
}

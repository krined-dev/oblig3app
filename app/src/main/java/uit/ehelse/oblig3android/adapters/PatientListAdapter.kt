package uit.ehelse.oblig3android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.patientList.PatientModel

class PatientListAdapter (private val data: List<PatientModel>,
                          //private val onClickListener: OnClickListener
     )
    : RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.patient_id)
        val button: View = view.findViewById(R.id.register_symptom_button)
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
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick()
//        }

    }

    override fun getItemCount() = data.size

//    class OnClickListener(val clickListener: (patient: )-> Unit)  {
//        fun onClick() = clickListener()
//    }
}

package uit.ehelse.oblig3android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uit.ehelse.oblig3android.R
import uit.ehelse.oblig3android.patientList.PatientModel

class PatientListAdapter(
    //private val onClickListener: OnClickListener,
    private val data: List<PatientModel>
) : RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {
//    private val onClickListener: OnClickListener
//) : androidx.recyclerview.widget.ListAdapter<PatientModel, PatientListAdapter.ViewHolder>(Diffcallback) {



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
//            onClickListener.onClick(patient)
//        }
    }

    companion object Diffcallback : DiffUtil.ItemCallback<PatientModel>() {
        override fun areItemsTheSame(oldItem: PatientModel, newItem: PatientModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PatientModel, newItem: PatientModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemCount() = data.size

//    class OnClickListener(val clickListener: (patient: PatientModel) -> Unit) {
//        fun onClick(patient: PatientModel) = clickListener(patient)
//    }
}

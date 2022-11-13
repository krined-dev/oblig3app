package uit.ehelse.oblig3android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uit.ehelse.oblig3android.R

class RegisterSymptomsAdapter(
) : RecyclerView.Adapter<RegisterSymptomsAdapter.ViewHolder>() {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.patient_name)
        val fever: TextView = view.findViewById(R.id.checkbox_fever)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_register_symptoms, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
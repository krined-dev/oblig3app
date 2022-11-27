package uit.ehelse.oblig3android.registerSymptoms

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import uit.ehelse.oblig3android.api.IdResource
import uit.ehelse.oblig3android.api.RegisterNewSymptomsRequest
import uit.ehelse.oblig3android.api.Symptom
import uit.ehelse.oblig3android.api.httpClient
import javax.inject.Inject

@HiltViewModel
class RegisterSymptomsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _fever = MutableLiveData<Boolean>()
    val fever: LiveData<Boolean>
        get() = _fever

    fun setFever(value: Boolean) = viewModelScope.launch {
        _fever.value = value
    }

    private val _cough = MutableLiveData<Boolean>()
    val cough: LiveData<Boolean>
        get() = _cough

    fun setCough(value: Boolean) = viewModelScope.launch {
        _cough.value = value
    }

    private val _tiredness = MutableLiveData<Boolean>()
    val tiredness: LiveData<Boolean>
        get() = _tiredness

    fun setTiredness(value: Boolean) = viewModelScope.launch {
        _tiredness.value = value
    }


    private val _soreThroat = MutableLiveData<Boolean>()
    val soreThroat: LiveData<Boolean>
        get() = _soreThroat

    fun setSoreThroat(value: Boolean) = viewModelScope.launch {
        _soreThroat.value = value
    }

    private val _headache = MutableLiveData<Boolean>()
    val headache: LiveData<Boolean>
        get() = _headache

    fun setHeadache(value: Boolean) = viewModelScope.launch {
        _headache.value = value
    }

    private val _achesAndPains = MutableLiveData<Boolean>()
    val achesAndPains: LiveData<Boolean>
        get() = _achesAndPains

    fun setAchesAndPains(value: Boolean) = viewModelScope.launch {
        _achesAndPains.value = value
    }

    private val _rashesOrDiscoloration = MutableLiveData<Boolean>()
    val rashesOrDiscoloration: LiveData<Boolean>
        get() = _rashesOrDiscoloration

    fun setRashesOrDiscoloration(value: Boolean) = viewModelScope.launch {
        _rashesOrDiscoloration.value = value
    }

    private val _lossOfSmellAndTaste = MutableLiveData<Boolean>()
    val lossOfSmellAndTaste: LiveData<Boolean>
        get() = _lossOfSmellAndTaste

    fun setLossOfSmellAndTaste(value: Boolean) = viewModelScope.launch {
        _lossOfSmellAndTaste.value = value
    }

    private val _diarrhea = MutableLiveData<Boolean>()
    val diarrhea: LiveData<Boolean>
        get() = _diarrhea

    fun setDiarrhea(value: Boolean) = viewModelScope.launch {
        _diarrhea.value = value
    }

    private val _breathingComplication = MutableLiveData<Boolean>()
    val breathingComplication: LiveData<Boolean>
        get() = _breathingComplication

    fun setBreathingComplication(value: Boolean) = viewModelScope.launch {
        _breathingComplication.value = value
    }

    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response


    suspend fun submitSymptomsAsync(ssn: String) {
        val wardId = "1"
        withContext(Dispatchers.IO) {
            val symptoms = SymptomInput(
                fever = _fever.value ?: false,
                cough = _cough.value ?: false,
                tiredness = _tiredness.value ?: false,
                soreThroat = _soreThroat.value ?: false,
                headache = _headache.value ?: false,
                achesAndPains = _achesAndPains.value ?: false,
                rashesOrDiscoloration = _rashesOrDiscoloration.value ?: false,
                lossOfSmellAndTaste = _lossOfSmellAndTaste.value ?: false,
                diarrhea = _diarrhea.value ?: false,
                breathingComplication = _breathingComplication.value ?: false
            ).toListOfSymptom()

            val response = httpClient().registerNewSymptoms(RegisterNewSymptomsRequest(ssn, symptoms, wardId))
            Log.i("Symptoms", response)
            _response.postValue(response)
        }
    }

}

data class SymptomInput(
    val fever: Boolean,
    val cough: Boolean,
    val tiredness: Boolean,
    val soreThroat: Boolean,
    val headache: Boolean,
    val achesAndPains: Boolean,
    val rashesOrDiscoloration: Boolean,
    val lossOfSmellAndTaste: Boolean,
    val diarrhea: Boolean,
    val breathingComplication: Boolean
) {
    fun toListOfSymptom(): List<Symptom> {
        val symptoms = mutableListOf<Symptom>()
        if (fever) symptoms.add(Symptom.FEVER)
        if (cough) symptoms.add(Symptom.COUGHING)
        if (tiredness) symptoms.add(Symptom.TIREDNESS)
        if (soreThroat) symptoms.add(Symptom.SORE_THROAT)
        if (headache) symptoms.add(Symptom.HEADACHE)
        if (achesAndPains) symptoms.add(Symptom.ACHES_AND_PAINS)
        if (rashesOrDiscoloration) symptoms.add(Symptom.RASHES_OR_DISCOLORATION)
        if (lossOfSmellAndTaste) symptoms.add(Symptom.LOSS_OF_TASTE_AND_SMELL)
        if (diarrhea) symptoms.add(Symptom.DIARRHOEA)
        if (breathingComplication) symptoms.add(Symptom.BREATHING_COMPLICATIONS)
        return symptoms
    }
}

@Serializable
data class RegisterNewSymptomsRequest(
    val patientId: String,
    val symptoms: List<Symptom>,
    val wardId: String
) {


}

package kg.nurtelecom.ostories.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorySharedViewModel: ViewModel() {

    private val _vpScrollStateLD = MutableLiveData<Int>()
    val vpScrollStateLD: LiveData<Int> get() = _vpScrollStateLD

    fun onScrollStateChange(state: Int) {
        _vpScrollStateLD.postValue(state)
    }

}
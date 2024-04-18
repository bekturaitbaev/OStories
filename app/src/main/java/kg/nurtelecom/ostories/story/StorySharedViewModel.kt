package kg.nurtelecom.ostories.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorySharedViewModel: ViewModel() {

    private val _vpScrollStateLD = MutableLiveData<Int>()
    val vpScrollStateLD: LiveData<Int> get() = _vpScrollStateLD

    private val _transitionAnimationLD = MutableLiveData<TransitionState>()
    val transitionAnimationLD: LiveData<TransitionState> get() = _transitionAnimationLD

    fun setTransitionState(state: TransitionState) {
        _transitionAnimationLD.postValue(state)
    }

    fun onScrollStateChange(state: Int) {
        _vpScrollStateLD.postValue(state)
    }

}

enum class TransitionState {
    ANIMATING,
    ENDED
}
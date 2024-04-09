package kg.nurtelecom.ostories.progress

/**
 * Created by Tiago Ornelas on 18/04/2020.
 * Model that holds the segment state
 */
class Segment {

    private var animationProgress: Float = 0f

    var animationState: AnimationState = AnimationState.IDLE
        set(value) {
            animationProgress = when (value) {
                AnimationState.ANIMATED -> 100f
                AnimationState.IDLE -> 0f
                else -> animationProgress
            }
            field = value
        }

    /**
     * Represents possible drawing states of the segment
     */
    enum class AnimationState {
        ANIMATED,
        ANIMATING,
        IDLE
    }

    val progressPercentage: Float
        get() = animationProgress / 100

    fun setProgress(value: Float) {
        animationProgress = value
    }
}
package kg.nurtelecom.ostories.stories.model

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
package kg.nurtelecom.ostories.story

interface OStoriesListener {

    fun onStoryCompleted()

    fun onStoryStartReached()

    fun onSwipeDownEnd(dismissDialog: Boolean, posY: Int)

    fun onSwipeDown()
}
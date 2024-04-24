package kg.nurtelecom.ostories.stories.story

interface OStoriesListener {

    fun onStoryCompleted()

    fun onStoryStartReached()

    fun onSwipeDownEnd(dismissDialog: Boolean, posY: Int)

    fun onSwipeDown()

    fun onStoryViewed(storyId: Long)
}
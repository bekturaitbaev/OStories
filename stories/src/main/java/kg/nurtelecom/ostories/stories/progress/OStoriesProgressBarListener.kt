package kg.nurtelecom.ostories.stories.progress

interface OStoriesProgressBarListener {

    fun onSegmentChange(oldSegmentIndex: Int, newSegmentIndex: Int)

    fun onCompleted()

    fun onStartReached()
}
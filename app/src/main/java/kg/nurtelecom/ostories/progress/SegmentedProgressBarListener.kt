package kg.nurtelecom.ostories.progress

interface OStoriesProgressBarListener {

    fun onSegmentChange(oldSegmentIndex: Int, newSegmentIndex: Int)

    fun onCompleted()

    fun onStartReached()
}
package kg.nurtelecom.ostories.story

import android.os.Parcelable
import java.io.Serializable

interface OStoriesListener {

    fun onStoryCompleted()

    fun onStoryStartReached()
}
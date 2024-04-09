package kg.nurtelecom.ostories.story

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kg.nurtelecom.ostories.R
import kg.nurtelecom.ostories.databinding.FragmentStoryBinding
import kg.nurtelecom.ostories.databinding.FragmentStoryViewBinding
import kg.nurtelecom.ostories.extensions.loadImage
import kg.nurtelecom.ostories.model.Highlight
import kg.nurtelecom.ostories.model.Story
import kg.nurtelecom.ostories.progress.OStoriesProgressBarListener

class StoryViewFragment : Fragment(), View.OnTouchListener {

    private lateinit var binding: FragmentStoryViewBinding
    private var highlight: Highlight? = null
    private var listener: OStoriesListener? = null
    private var touchStartTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryViewBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        val indexToShow = highlight?.stories?.indexOfFirst { !it.isWatched }?.let {
            if (it == -1) 0 else it
        } ?: 0
        setUpOStoriesProgressBar(indexToShow)
    }

    private fun setUpClicks() = with(binding) {
        viewLeft.setOnClickListener { progress.previous() }
        viewRight.setOnClickListener { progress.next() }
        viewLeft.setOnTouchListener(this@StoryViewFragment)
        viewRight.setOnTouchListener(this@StoryViewFragment)
    }

    private fun setUpOStoriesProgressBar(position: Int) = with(binding.progress) {
        segmentCount = highlight?.stories?.count() ?: 0
        binding.progress.listener = oStoriesProgressBarListener
        setPosition(position)
        pause()
    }

    private val oStoriesProgressBarListener = object : OStoriesProgressBarListener {
        override fun onSegmentChange(oldSegmentIndex: Int, newSegmentIndex: Int) {
            loadImage(highlight?.stories?.get(newSegmentIndex))
        }
        override fun onCompleted() {
            listener?.onStoryCompleted()
        }

        override fun onStartReached() {
            listener?.onStoryStartReached()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progress.listener = oStoriesProgressBarListener
        binding.progress.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.progress.pause()
        binding.progress.listener = null
    }

    private fun loadImage(story: Story?) = with(binding) {
        progress.pause()
        storyImage.loadImage(story?.image) {
            story?.isWatched = true
            progress.resume()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartTime = event.eventTime
                binding.progress.pause()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val touchEndTime = event.eventTime
                binding.progress.resume()
                if (touchEndTime - touchStartTime > TOUCH_DURATION) return true
                v?.performClick()
            }
        }
        return true
    }

    companion object {

        private const val TOUCH_DURATION = 500L

        @JvmStatic
        fun newInstance(item: Highlight, listener: OStoriesListener) =
            StoryViewFragment().apply {
                this.listener = listener
                highlight = item
            }
    }
}
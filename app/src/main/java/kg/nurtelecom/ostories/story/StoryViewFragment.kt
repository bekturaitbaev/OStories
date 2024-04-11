package kg.nurtelecom.ostories.story

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import kg.nurtelecom.ostories.databinding.FragmentStoryViewBinding
import kg.nurtelecom.ostories.extensions.loadImage
import kg.nurtelecom.ostories.model.Highlight
import kg.nurtelecom.ostories.model.Story
import kg.nurtelecom.ostories.progress.OStoriesProgressBarListener

class StoryViewFragment : Fragment(), View.OnTouchListener {

    private lateinit var binding: FragmentStoryViewBinding
    private val viewModel: StorySharedViewModel by viewModels({ requireParentFragment() })
    private var highlight: Highlight? = null
    private var listener: OStoriesListener? = null
    private var touchStartTime = 0L
    private var lastFocusX = 0f
    private var lastFocusY = 0f
    private var touchStillDown = false

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
        setUpOStoriesProgressBar(getIndexToShow())

        viewModel.vpScrollStateLD.observe(requireParentFragment().viewLifecycleOwner) {
            when(it) {
                ViewPager2.SCROLL_STATE_DRAGGING -> binding.progress.pause()
                ViewPager2.SCROLL_STATE_IDLE -> binding.progress.resume()
            }
        }
    }

    private fun getIndexToShow() = highlight?.stories?.indexOfFirst { !it.isWatched }?.let {
        if (it == -1) 0 else it
    } ?: 0

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
        binding.progress.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.progress.setPosition(getIndexToShow())
        binding.progress.pause()
    }

    private fun loadImage(story: Story?) = with(binding) {
        progress.pause()
        ivStory.loadImage(story?.image) {
            if (this@StoryViewFragment.isResumed) {
                story?.isWatched = true
                progress.resume()
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastFocusX = event.x
                lastFocusY = event.y
                touchStartTime = event.eventTime
                binding.progress.pause()
                touchStillDown = true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val touchEndTime = event.eventTime
                binding.progress.resume()
                if (touchEndTime - touchStartTime > TOUCH_DURATION) return false
                val touchSlopeSquare = ViewConfiguration.get(requireContext()).scaledTouchSlop
                val deltaX = event.x - lastFocusX
                val deltaY = event.y - lastFocusY
                val slope = deltaX * deltaX + deltaY * deltaY
                if (slope > touchSlopeSquare) return false
                v?.performClick()
                return true
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

class MyView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): View(context, attrs, defStyle) {



}
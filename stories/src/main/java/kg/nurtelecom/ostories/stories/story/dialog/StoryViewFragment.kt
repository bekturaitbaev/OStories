package kg.nurtelecom.ostories.stories.story.dialog

import android.animation.ObjectAnimator
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import kg.nurtelecom.ostories.stories.R
import kg.nurtelecom.ostories.stories.databinding.FragmentStoryViewBinding
import kg.nurtelecom.ostories.stories.extensions.loadImage
import kg.nurtelecom.ostories.stories.model.Highlight
import kg.nurtelecom.ostories.stories.model.Story
import kg.nurtelecom.ostories.stories.progress.OStoriesProgressBarListener
import kg.nurtelecom.ostories.stories.story.OStoriesListener
import kg.nurtelecom.ostories.stories.story.StorySharedViewModel
import kg.nurtelecom.ostories.stories.story.TransitionState
import kotlin.math.abs

class StoryViewFragment : Fragment(), View.OnTouchListener {

    private lateinit var binding: FragmentStoryViewBinding
    private val viewModel: StorySharedViewModel by viewModels({ requireParentFragment() })
    private var highlight: Highlight? = null
    private var listener: OStoriesListener? = null
    private var touchStartTime = 0L
    private var lastFocusX = 0f
    private var lastFocusY = 0f
    private var isSwiping = false
    private var isDescriptionExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryViewBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeDescriptionColor()
        setUpClicks()
        setUpOStoriesProgressBar(getIndexToShow())
        observe()
    }

    private fun observe() = with(binding) {
        viewModel.vpScrollStateLD.observe(requireParentFragment().viewLifecycleOwner) {
            when(it) {
                ViewPager2.SCROLL_STATE_DRAGGING -> progress.pause()
                ViewPager2.SCROLL_STATE_IDLE -> progress.resume()
            }
        }

        viewModel.transitionAnimationLD.observe(requireParentFragment().viewLifecycleOwner) {
            val visible = it == TransitionState.EXPANDED
            progress.isVisible = visible
            tvDescription.isVisible = visible
            tvTitle.isVisible = visible
            btnAction.isVisible = visible
            ivClose.isVisible = visible
            tvForYou.isVisible = visible && highlight?.isMarketingCenter == true
            clStoryView.background = if (visible && highlight?.isMarketingCenter == true) {
                ContextCompat.getDrawable(requireContext(), R.drawable.background_story_view_stroke)
            } else null
        }
    }

    private fun getIndexToShow() = highlight?.stories?.indexOfFirst { !it.isViewed }?.let {
        if (it == -1) 0 else it
    } ?: 0

    private fun setUpClicks() = with(binding) {
        ivClose.setOnSingleClickListener {
            listener?.onSwipeDownEnd(true, cvStory.top)
        }
        viewLeft.setOnClickListener { progress.previous() }
        viewRight.setOnClickListener { progress.next() }
        viewLeft.setOnTouchListener(this@StoryViewFragment)
        viewRight.setOnTouchListener(this@StoryViewFragment)
    }

    private fun changeDescriptionColor() {
        if (isDescriptionExpanded) {
            binding.tvDescription.paint.shader = null
            return
        }
        val gradient = LinearGradient(
            0f,
            0f,
            0f,
            DESCRIPTION_GRADIENT_HEIGHT.dpF,
            ContextCompat.getColor(requireContext(), R.color.white),
            ContextCompat.getColor(requireContext(), android.R.color.transparent),
            Shader.TileMode.CLAMP
        )
        binding.tvDescription.paint.shader = gradient
    }

    private fun animateDescription() = with(binding) {
        val lineCount = if (isDescriptionExpanded) LINE_COUNT else tvDescription.lineCount
        viewStory.background = ContextCompat.getDrawable(
            requireContext(),
            if (isDescriptionExpanded) R.drawable.background_story_view_gradient
            else R.drawable.background_story_view_alpha70
        )
        isDescriptionExpanded = !isDescriptionExpanded
        val animator = ObjectAnimator.ofInt(binding.tvDescription, "maxLines", lineCount)
        animator.run {
            duration = RETURN_ANIMATION_DURATION
            if (!isDescriptionExpanded) doOnEnd { changeDescriptionColor() }
            else doOnStart { changeDescriptionColor() }
            start()
        }
    }

    private fun setUpOStoriesProgressBar(position: Int) = with(binding.progress) {
        segmentCount = highlight?.stories?.count() ?: 0
        binding.progress.listener = oStoriesProgressBarListener
        setPosition(position)
        pause()
    }

    private val oStoriesProgressBarListener = object : OStoriesProgressBarListener {
        override fun onSegmentChange(oldSegmentIndex: Int, newSegmentIndex: Int) {
            loadData(highlight?.stories?.get(newSegmentIndex))
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

    private fun loadData(story: Story?) = with(binding) {
        progress.pause()
        tvDescription.setTextOrHide(story?.description)
        ivStory.loadImage(story?.image) {
            if (this@StoryViewFragment.isResumed) {
                story?.isViewed = true
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
                isSwiping = false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val deltaX = event.x - lastFocusX
                val deltaY = event.y - lastFocusY
                if (isDescriptionExpanded) {
                    animateDescription()
                    binding.progress.resume()
                    return true
                }
                if (binding.cvStory.y > 0) {
                    val dismissDialog = binding.cvStory.y >= binding.root.height/4
                    if (dismissDialog) binding.progress.reset()
                    else binding.progress.resume()
                    listener?.onSwipeDownEnd(dismissDialog, binding.cvStory.top)
                    animateToInitialPosition()
                    return false
                }
                if (deltaY < -SWIPE_DISTANCE && abs(deltaY) > abs(deltaX) && binding.tvDescription.isVisible) {
                    animateDescription()
                    return true
                }
                val touchEndTime = event.eventTime
                binding.progress.resume()
                if (touchEndTime - touchStartTime > TOUCH_DURATION) return false
                val touchSlopeSquare = ViewConfiguration.get(requireContext()).scaledTouchSlop
                val slope = deltaX * deltaX + deltaY * deltaY
                if (slope > touchSlopeSquare) return false
                v?.performClick()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = event.y - lastFocusY
                val deltaX = event.x - lastFocusX
                if (!isSwiping && !isDescriptionExpanded && deltaY > REPEL_HEIGHT && abs(deltaY) > abs(deltaX)) {
                    listener?.onSwipeDown()
                    isSwiping = true
                }
                if (binding.cvStory.y >= binding.root.height/3 && deltaY > 0) return false
                if (binding.cvStory.y <= 0 && deltaY < 0) return false
                if (isSwiping) binding.cvStory.y += deltaY
            }
        }
        return true
    }

    private fun animateToInitialPosition() {
        binding.cvStory
            .animate()
            .setDuration(RETURN_ANIMATION_DURATION)
            .translationY(0f)
            .start()
    }

    companion object {

        private const val TOUCH_DURATION = 500L
        private const val RETURN_ANIMATION_DURATION = 200L
        private const val REPEL_HEIGHT = 15
        private const val SWIPE_DISTANCE = 60
        private const val LINE_COUNT = 3
        private const val DESCRIPTION_GRADIENT_HEIGHT = 60

        @JvmStatic
        fun newInstance(item: Highlight, listener: OStoriesListener) =
            StoryViewFragment().apply {
                this.listener = listener
                highlight = item
            }
    }
}
package kg.nurtelecom.ostories.stories.story.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.design2.chili2.extensions.dp
import kg.nurtelecom.ostories.stories.R
import kg.nurtelecom.ostories.stories.databinding.FragmentStoryBinding
import kg.nurtelecom.ostories.stories.model.Highlight
import kg.nurtelecom.ostories.stories.model.StoryMock
import kg.nurtelecom.ostories.stories.story.OStoriesListener
import kg.nurtelecom.ostories.stories.story.StorySharedViewModel
import kg.nurtelecom.ostories.stories.story.TransitionState
import kg.nurtelecom.ostories.stories.story.highlight.OStoriesRecyclerViewListener

class StoryDialogFragment : DialogFragment(), OStoriesListener {

    private lateinit var binding: FragmentStoryBinding
    private val viewModel: StorySharedViewModel by viewModels()
    private var listener: OStoriesRecyclerViewListener? = null
    private var handler = Handler(Looper.getMainLooper())
    private var highlights: List<Highlight> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)

        setUpViewPager()

        binding.rootLayout.setTransitionListener(transitionListener)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            val posX = bundle.getInt(POS_X)
            val posY = bundle.getInt(POS_Y)

            changeStoryStartConstraintSet(Pair(posX, posY))
        }
    }

    private fun changeStoryStartConstraintSet(pair: Pair<Int, Int>?) {
        binding.rootLayout.getConstraintSet(R.id.story_minimized_set)?.let {
            it.setMargin(
                binding.viewPager.id,
                ConstraintSet.START,
                (pair?.first ?: 0) + DEFAULT_STORY_MARGIN.dp
            )
            it.setMargin(
                binding.viewPager.id,
                ConstraintSet.TOP,
                (pair?.second ?: 0) + DEFAULT_STORY_MARGIN.dp
            )
            binding.viewPager.requestLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed({ binding.rootLayout.transitionToEnd() }, ANIMATION_DELAY)
    }

    private fun setUpViewPager() = with(binding) {
        val storyAdapter = StoryViewViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            this@StoryDialogFragment,
            highlights
        )
        val itemPosition = arguments?.getInt(ITEM_POSITION) ?: 0
        with(viewPager) {
            isSaveEnabled = false
            adapter = storyAdapter
            offscreenPageLimit = 1
            setCurrentItem(itemPosition, false)
            registerOnPageChangeCallback(onPageChangeCallback)
        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            viewModel.onScrollStateChange(state)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            listener?.scrollToPosition(position)
        }
    }

    override fun onDestroy() = with(binding) {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        rootLayout.removeTransitionListener(transitionListener)
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onStoryCompleted() = with(binding.viewPager) {
        if (currentItem == highlights.size - 1) onSwipeDownEnd(true, 0)
        else currentItem += 1
    }

    override fun onStoryStartReached() {
        binding.viewPager.currentItem -= 1
    }

    override fun onSwipeDownEnd(dismissDialog: Boolean, posY: Int) {
        with(binding) {
            viewPager.isUserInputEnabled = true
            if (!dismissDialog) return@with
            val pair = listener?.getItemViewBounds(viewPager.currentItem)
            changeStoryStartConstraintSet(pair)
            rootLayout.post {
                rootLayout.performClick()
            }
        }
    }

    override fun onSwipeDown() {
        binding.viewPager.isUserInputEnabled = false
    }

    private val transitionListener = object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
            viewModel.setTransitionState(TransitionState.ANIMATING)
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            viewModel.setTransitionState(TransitionState.ENDED)
            when (currentId) {
                R.id.story_minimized_set -> {
                    dismiss()
                }
            }
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    }

    companion object {
        private const val TAG = "StoryDialogFragment"
        private const val ITEM_POSITION = "ITEM_POSITION"
        private const val POS_X = "POS_X"
        private const val POS_Y = "POS_Y"
        private const val ANIMATION_DELAY = 200L
        private const val DEFAULT_STORY_MARGIN = 5

        fun showDialog(
            fragmentManager: FragmentManager,
            itemPosition: Int,
            highlights: List<Highlight>,
            posX: Int,
            posY: Int,
            listener: OStoriesRecyclerViewListener
        ) {
            val args = Bundle().apply {
                putInt(POS_X, posX)
                putInt(POS_Y, posY)
                putInt(ITEM_POSITION, itemPosition)
            }
            val storyDialogFragment = StoryDialogFragment().apply {
                this.highlights = highlights
                arguments = args
            }
            storyDialogFragment.listener = listener
            storyDialogFragment.show(fragmentManager, TAG)
        }
    }
}
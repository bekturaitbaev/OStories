package kg.nurtelecom.ostories.story

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import kg.nurtelecom.ostories.R
import kg.nurtelecom.ostories.databinding.FragmentStoryBinding
import kg.nurtelecom.ostories.model.StoryMock

class StoryDialogFragment: DialogFragment(), OStoriesListener {

    private lateinit var binding: FragmentStoryBinding
    private val viewModel: StorySharedViewModel by viewModels()

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

            binding.rootLayout.getConstraintSet(R.id.story_minimized_set)?.let {
                it.setMargin(binding.viewPager.id, ConstraintSet.START, posX + 10)
                it.setMargin(binding.viewPager.id, ConstraintSet.TOP, posY + 10)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rootLayout.post { binding.root.performClick() }
    }

    private fun setUpViewPager() = with(binding) {
        val highlights = StoryMock.fetchHighlights()
        val storyAdapter = StoryViewViewPagerAdapter(childFragmentManager, lifecycle, this@StoryDialogFragment, highlights)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.rootLayout.removeTransitionListener(transitionListener)
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onStoryCompleted() {
        binding.viewPager.currentItem += 1
    }

    override fun onStoryStartReached() {
        binding.viewPager.currentItem -= 1
    }

    override fun onSwipeDownEnd(dismissDialog: Boolean, posY: Int) {
        with(binding) {
            viewPager.isUserInputEnabled = true
            if (!dismissDialog) return@with
            animateBackgroundColor()
            rootLayout.getConstraintSet(R.id.story_expanded_set)
                .setMargin(viewPager.id, ConstraintSet.TOP, posY)
            rootLayout.post { rootLayout.transitionToStart() }
        }
    }

    private fun animateBackgroundColor() {
        ObjectAnimator.ofObject(
            binding.viewPager,
            "backgroundColor",
            ArgbEvaluator(),
            com.design2.chili2.R.color.black_1,
            Color.TRANSPARENT
        ).setDuration(50L).start()
    }

    override fun onSwipeDown() {
        binding.viewPager.isUserInputEnabled = false
    }

    private val transitionListener = object: MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {}
        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            when(currentId) {
                R.id.story_minimized_set -> { dismiss() }
            }
        }
        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {}
    }

    companion object {
        const val TAG = "StoryDialogFragment"
        private const val ITEM_POSITION = "ITEM_POSITION"
        private const val POS_X = "POS_X"
        private const val POS_Y = "POS_Y"

        fun showDialog(fragmentManager: FragmentManager, itemPosition: Int, posX: Int, posY: Int) {
            val args = Bundle().apply {
                putInt(POS_X, posX)
                putInt(POS_Y, posY)
                putInt(ITEM_POSITION, itemPosition)
            }
            val storyDialogFragment = StoryDialogFragment().apply {
                arguments = args
            }
            storyDialogFragment.show(fragmentManager, TAG)
        }
    }
}
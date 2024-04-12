package kg.nurtelecom.ostories.story

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
import kg.nurtelecom.ostories.PlayScreenFragment
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
        setUpTransitionListener()

        return binding.root
    }

    private fun setUpTransitionListener() {
        binding.rootLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when(currentId) {
                    R.id.play_screen_expanded_normal -> {
                        binding.rootLayout.setTransition(R.id.mid_transition)
                    }
                    R.id.play_screen_bottom_minimized -> {
                        binding.rootLayout.setTransition(R.id.end_transition)
                    }
                    R.id.play_screen_minimized -> {
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

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val posX = it.getInt(PlayScreenFragment.POS_X)
            val posY = it.getInt(PlayScreenFragment.POS_Y)

            binding.rootLayout.getConstraintSet(R.id.play_screen_minimized)?.let {
                it.setMargin(binding.playerBackgroundView.id, ConstraintSet.START, posX)
                it.setMargin(binding.playerBackgroundView.id, ConstraintSet.BOTTOM, posY)
            }
        }
    }

    private fun setUpViewPager() = with(binding) {
        val highlights = StoryMock.fetchHighlights()
        val adapter = StoryViewViewPagerAdapter(childFragmentManager, lifecycle, this@StoryDialogFragment, highlights)
        viewPager.isSaveEnabled = false
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        viewPager.setPageTransformer(CubeTransformer())
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            viewModel.onScrollStateChange(state)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onStoryCompleted() {
        binding.viewPager.currentItem += 1
    }

    override fun onStoryStartReached() {
        binding.viewPager.currentItem -= 1
    }

    companion object {
        const val TAG = "StoryDialogFragment"
        const val POS_X = "POS_X"
        const val POS_Y = "POS_Y"

        fun showDialog(fragmentManager: FragmentManager, posX: Int, posY: Int) {
            val args = Bundle().apply {
                putInt(POS_X, posX)
                putInt(POS_Y, posY)
            }
            val storyDialogFragment = StoryDialogFragment().apply {
                arguments = args
            }
            storyDialogFragment.show(fragmentManager, TAG)
        }
    }
}
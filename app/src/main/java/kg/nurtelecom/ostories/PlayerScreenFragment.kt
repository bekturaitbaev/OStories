package kg.nurtelecom.ostories
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import kg.nurtelecom.ostories.databinding.FragmentPlayerScreenBinding

class PlayScreenFragment: Fragment() {

    companion object {
        const val TAG = "PlayScreenFragment"
        const val POS_X = "POS_X"
        const val POS_Y = "POS_Y"
        fun newInstance(posX: Int, posY: Int): PlayScreenFragment {
            val args = Bundle()
            args.putInt(POS_X, posX)
            args.putInt(POS_Y, posY)
            val playScreenFragment = PlayScreenFragment()
            playScreenFragment.arguments = args
            return playScreenFragment
        }
    }

    private lateinit var binding: FragmentPlayerScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerScreenBinding.inflate(inflater, container, false)

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
                        activity?.onBackPressed()
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val posX = it.getInt(POS_X)
            val posY = it.getInt(POS_Y)

            binding.rootLayout.getConstraintSet(R.id.play_screen_minimized)?.let {
                it.setMargin(binding.playerBackgroundView.id, ConstraintSet.START, posX)
                it.setMargin(binding.playerBackgroundView.id, ConstraintSet.BOTTOM, posY)
            }
        }
    }
}
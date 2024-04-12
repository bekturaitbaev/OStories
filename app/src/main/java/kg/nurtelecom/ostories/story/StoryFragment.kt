package kg.nurtelecom.ostories.story

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kg.nurtelecom.ostories.R
import kg.nurtelecom.ostories.databinding.FragmentStoryBinding
import kg.nurtelecom.ostories.model.StoryMock

class StoryFragment() : Fragment(), OStoriesListener {

    private lateinit var binding: FragmentStoryBinding
    private val viewModel: StorySharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(inflater, container, false)

        setUpViewPager()

        return binding.root
    }

    private fun setUpViewPager() = with(binding) {
        val highlights = StoryMock.fetchHighlights()
        val adapter = StoryViewViewPagerAdapter(childFragmentManager, lifecycle, this@StoryFragment, highlights)
//        viewPager.adapter = adapter
//        viewPager.offscreenPageLimit = 1
//        viewPager.setPageTransformer(CubeTransformer())
//        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//                viewModel.onScrollStateChange(state)
//            }
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {

        const val TAG = "StoryFragment"
        @JvmStatic
        fun newInstance() =
            StoryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onStoryCompleted() {
//        binding.viewPager.currentItem += 1
    }

    override fun onStoryStartReached() {
//        binding.viewPager.currentItem -= 1
    }
}
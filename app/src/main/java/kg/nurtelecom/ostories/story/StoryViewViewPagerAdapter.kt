package kg.nurtelecom.ostories.story

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kg.nurtelecom.ostories.model.Highlight

class StoryViewViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var listener: OStoriesListener,
    private var storiesList: List<Highlight>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return storiesList.size - 1
    }

    override fun createFragment(position: Int): Fragment {
        return StoryViewFragment.newInstance(storiesList[position], listener)
    }
}

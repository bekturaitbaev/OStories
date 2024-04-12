package kg.nurtelecom.ostories.story

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kg.nurtelecom.ostories.databinding.ItemStoryBinding
import kg.nurtelecom.ostories.extensions.loadImage
import kg.nurtelecom.ostories.model.Highlight
import kg.nurtelecom.ostories.model.Story
import kg.nurtelecom.ostories.progress.OStoriesProgressBarListener
import kotlinx.coroutines.processNextEventInCurrentThread

class StoryViewPagerAdapter: RecyclerView.Adapter<StoryViewPagerAdapter.StoryViewPagerVH>() {

    private var items: List<Highlight> = emptyList()

    var viewPager: ViewPager2? = null
    private var onStoryEndListener: (() -> Unit)? = null
    private var onStartReachedListener: (() -> Unit)? = null
    private var onViewPagerPageSelected: ((position: Int) -> Unit)? = null

    fun setOnStartReachedListener(listener: () -> Unit) {
        this.onStartReachedListener = listener
    }

    fun setOnStoryEndListener(listener: () -> Unit) {
        this.onStoryEndListener = listener
    }

    private fun setOnViewPagerPageSelected(listener: (position: Int) -> Unit) {
        this.onViewPagerPageSelected = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<Highlight>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewPagerVH {
        return StoryViewPagerVH(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: StoryViewPagerVH, position: Int) {
        holder.bind(items[position])
    }

    var pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            onViewPagerPageSelected?.invoke(position)
        }
    }

    inner class StoryViewPagerVH(
        private val binding: ItemStoryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private var stories = emptyList<Story>()

        fun bind(item: Highlight) = with(binding) {
            stories = item.stories
            setUpClicks()
            setUpViewPagerCallback()
            val indexToShow = stories.indexOfFirst { !it.isWatched }.let {
                if (it == -1) 0 else it
            }
            setUpOStoriesProgressBar(indexToShow)
        }

        private fun setUpClicks() = with(binding) {
            viewLeft.setOnClickListener { progress.previous() }
            viewRight.setOnClickListener { progress.next() }
            setOnViewPagerPageSelected {

            }
        }

        private fun setUpViewPagerCallback() {
            viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val indexToShow = stories.indexOfFirst { !it.isWatched }.let {
                        if (it == -1) 0 else it
                    }
                    binding.progress.setPosition(indexToShow)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    binding.progress.pause()
                }
            })
        }

        private fun setUpOStoriesProgressBar(position: Int) = with(binding.progress) {
            segmentCount = stories.count()
            listener = oStoriesProgressBarListener
            setPosition(position)
        }

        private val oStoriesProgressBarListener = object : OStoriesProgressBarListener {
            override fun onSegmentChange(oldSegmentIndex: Int, newSegmentIndex: Int) {
                loadImage(stories[newSegmentIndex])
            }
            override fun onCompleted() {
                onStoryEndListener?.invoke()
            }

            override fun onStartReached() {
                onStartReachedListener?.invoke()
            }
        }

        private fun loadImage(story: Story) = with(binding) {
            progress.pause()
        }

    }
}
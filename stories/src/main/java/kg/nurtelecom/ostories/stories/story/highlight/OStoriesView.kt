package kg.nurtelecom.ostories.stories.story.highlight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import kg.nurtelecom.ostories.stories.R
import kg.nurtelecom.ostories.stories.databinding.ViewOstoriesBinding
import kg.nurtelecom.ostories.stories.model.Highlight
import kg.nurtelecom.ostories.stories.story.dialog.StoryDialogFragment
import java.io.Serializable

class OStoriesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), OStoriesRecyclerViewListener {

    private val binding: ViewOstoriesBinding = ViewOstoriesBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var onMoreItemsClickedListener: (() -> Unit)? = null
    private var onStoryViewedListener: ((storyId: Long) -> Unit)? = null

    private val adapter: HighlightsAdapter by lazy { HighlightsAdapter() }

    private var highlights: ArrayList<Highlight> = arrayListOf()
    private var fragmentManager: FragmentManager? = null
    private var isMoreItemVisible = true

    init {
        setUpClicks()
    }

    fun setOnStoryViewedListener(listener: ((storyId: Long) -> Unit)) {
        this.onStoryViewedListener = listener
    }

    fun setOnMoreItemsClickedListener(listener: (() -> Unit)) {
        this.onMoreItemsClickedListener = listener
    }

    fun setUp(highlights: List<Highlight>, fragmentManager: FragmentManager, isMoreItemVisible: Boolean = true) {
        this.isMoreItemVisible = isMoreItemVisible
        with(this.highlights) {
            clear()
            addAll(highlights)
            if (isMoreItemVisible) add(Highlight(id = -1L, title = context.getString(R.string.all_events)))
        }
        this.fragmentManager = fragmentManager
        binding.recyclerView.adapter = adapter
        adapter.submitList(this.highlights)
    }

    private fun setUpClicks() {
        adapter.setOnHighlightClickListener { position, view ->
            val highlight = highlights.getOrNull(position) ?: return@setOnHighlightClickListener
            if (highlight.id == -1L) {
                onMoreItemsClickedListener?.invoke()
                return@setOnHighlightClickListener
            }
            showStoryDialogFragment(position, view)
        }
    }

    private fun showStoryDialogFragment(position: Int, view: View) {
        val items: ArrayList<Highlight> = if (highlights.isNotEmpty() && isMoreItemVisible) ArrayList(highlights.subList(0, highlights.size - 1))
        else highlights
        StoryDialogFragment.showDialog(
            fragmentManager = fragmentManager
                ?: throw NullPointerException("FragmentManager is null"),
            position,
            items,
            view.left,
            view.top,
            this
        )
    }

    override fun scrollToPosition(position: Int) {
        binding.recyclerView.smoothScrollToPosition(position)
    }

    override fun getItemViewBounds(position: Int): Pair<Int, Int> {
        val view = binding.recyclerView.findViewHolderForAdapterPosition(position)?.itemView
        return Pair(view?.left ?: 0, view?.top ?: 0)
    }

    override fun onStoryViewed(storyId: Long) {
        onStoryViewedListener?.invoke(storyId)
    }
}

interface OStoriesRecyclerViewListener: Serializable {
    fun scrollToPosition(position: Int)
    fun getItemViewBounds(position: Int): Pair<Int, Int>

    fun onStoryViewed(storyId: Long)
}
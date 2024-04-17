package kg.nurtelecom.ostories.story

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import kg.nurtelecom.ostories.databinding.ViewOstoriesBinding
import kg.nurtelecom.ostories.model.Highlight
import java.lang.NullPointerException

class OStoriesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), OStoriesRecyclerViewListener {

    private val binding: ViewOstoriesBinding = ViewOstoriesBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val adapter: HighlightsAdapter by lazy { HighlightsAdapter() }

    private var highlights: List<Highlight> = emptyList()
    private var fragmentManager: FragmentManager? = null

    init {
        setUpClicks()
    }

    fun setUp(highlights: List<Highlight>, fragmentManager: FragmentManager) {
        this.highlights = highlights
        this.fragmentManager = fragmentManager
        binding.recyclerView.adapter = adapter
        adapter.submitList(highlights)
    }

    private fun setUpClicks() {
        adapter.setOnHighlightClickListener { position, view ->
            StoryDialogFragment.showDialog(
                fragmentManager = fragmentManager ?: throw NullPointerException("FragmentManager is null"),
                position,
                view.left,
                view.top,
                this
            )
        }
    }

    override fun scrollToPosition(position: Int) {
        binding.recyclerView.smoothScrollToPosition(position)
    }

    override fun getItemViewBounds(position: Int): Pair<Int, Int> {
        val view = binding.recyclerView.findViewHolderForAdapterPosition(position)?.itemView
        return Pair(view?.left ?: 0, view?.top ?: 0)
    }
}

interface OStoriesRecyclerViewListener {
    fun scrollToPosition(position: Int)
    fun getItemViewBounds(position: Int): Pair<Int, Int>
}
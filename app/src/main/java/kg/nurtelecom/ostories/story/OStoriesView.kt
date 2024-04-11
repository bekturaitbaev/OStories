package kg.nurtelecom.ostories.story

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kg.nurtelecom.ostories.databinding.ViewOstoriesBinding
import kg.nurtelecom.ostories.model.Highlight

class OStoriesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
): FrameLayout(context, attrs, defStyle) {

    private val binding: ViewOstoriesBinding = ViewOstoriesBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val adapter: HighlightsAdapter = HighlightsAdapter()

    private var highlights: List<Highlight> = emptyList()

    fun setUp(highlights: List<Highlight>) {
        this.highlights = highlights
        binding.recyclerView.adapter = adapter
        adapter.submitList(highlights)
    }
}
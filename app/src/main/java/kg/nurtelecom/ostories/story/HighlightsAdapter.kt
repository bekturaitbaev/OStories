package kg.nurtelecom.ostories.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.ostories.databinding.ItemHighlightBinding
import kg.nurtelecom.ostories.databinding.ItemHighlightMarketingBinding
import kg.nurtelecom.ostories.databinding.ItemHighlightMoreBinding
import kg.nurtelecom.ostories.extensions.loadImage
import kg.nurtelecom.ostories.model.Highlight

class HighlightsAdapter: ListAdapter<Highlight, RecyclerView.ViewHolder>(DiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MARKETING_CENTER -> MCHighlightsVH.create(parent)
            MORE_HIGHLIGHTS -> MoreHighlightsVH.create(parent)
            else -> HighlightsVH.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position)?.isMarketingCenter == true -> MARKETING_CENTER
            getItem(position)?.id == -1L -> MORE_HIGHLIGHTS
            else -> NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            MARKETING_CENTER -> (holder as MCHighlightsVH).bind(getItem(position))
            MORE_HIGHLIGHTS -> (holder as MoreHighlightsVH).bind(getItem(position))
            else -> (holder as HighlightsVH).bind(getItem(position))
        }
    }

    class DiffUtils: DiffUtil.ItemCallback<Highlight>() {
        override fun areItemsTheSame(oldItem: Highlight, newItem: Highlight): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Highlight, newItem: Highlight): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val MARKETING_CENTER = 0
        private const val NORMAL = 1
        private const val MORE_HIGHLIGHTS = 2
    }
}

class HighlightsVH(
    private val binding: ItemHighlightBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Highlight) = with(binding) {
        tvTitle.text = item.title
        ivHighlight.loadImage(item.image)
    }

    companion object {
        fun create(parent: ViewGroup) = HighlightsVH(
            ItemHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class MoreHighlightsVH(
    private val binding: ItemHighlightMoreBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Highlight) = with(binding) {
        tvTitle.text = item.title
    }

    companion object {
        fun create(parent: ViewGroup) = MoreHighlightsVH(
            ItemHighlightMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class MCHighlightsVH(
    private val binding: ItemHighlightMarketingBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Highlight) = with(binding) {
        tvTitle.text = item.title
        ivHighlight.loadImage(item.image)
    }

    companion object {
        fun create(parent: ViewGroup) = MCHighlightsVH(
            ItemHighlightMarketingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}
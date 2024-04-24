package kg.nurtelecom.ostories.stories.story.highlight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.extensions.setOnSingleClickListener
import kg.nurtelecom.ostories.stories.databinding.ItemHighlightBinding
import kg.nurtelecom.ostories.stories.databinding.ItemHighlightMarketingBinding
import kg.nurtelecom.ostories.stories.databinding.ItemHighlightMoreBinding
import kg.nurtelecom.ostories.stories.extensions.loadImage
import kg.nurtelecom.ostories.stories.model.Highlight

class HighlightsAdapter: ListAdapter<Highlight, RecyclerView.ViewHolder>(DiffUtils()) {

    private var onHighlightClickListener: ((position: Int, view: View) -> Unit)? = null

    fun setOnHighlightClickListener(listener: ((position: Int, view: View) -> Unit)) {
        this.onHighlightClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MARKETING_CENTER -> MCHighlightsVH.create(parent, onHighlightClickListener)
            MORE_HIGHLIGHTS -> MoreHighlightsVH.create(parent, onHighlightClickListener)
            else -> HighlightsVH.create(parent, onHighlightClickListener)
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
    private val binding: ItemHighlightBinding,
    private val onHighlightClickListener: ((position: Int, view: View) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Highlight) = with(binding) {
        tvTitle.text = item.title
        ivHighlight.loadImage(item.image)
        if (!item.borderColors.isNullOrEmpty()) highlighter.setHighlighterGradientColors(item.borderColors)

        root.setOnSingleClickListener {
            onHighlightClickListener?.invoke(bindingAdapterPosition, root)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onHighlightClickListener: ((position: Int, view: View) -> Unit)?) = HighlightsVH(
            ItemHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onHighlightClickListener
        )
    }
}

class MoreHighlightsVH(
    private val binding: ItemHighlightMoreBinding,
    private val onHighlightClickListener: ((position: Int, view: View) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Highlight) = with(binding) {
        tvTitle.text = item.title

        root.setOnSingleClickListener {
            onHighlightClickListener?.invoke(bindingAdapterPosition, root)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onHighlightClickListener: ((position: Int, view: View) -> Unit)?) = MoreHighlightsVH(
            ItemHighlightMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onHighlightClickListener
        )
    }
}

class MCHighlightsVH(
    private val binding: ItemHighlightMarketingBinding,
    private val onHighlightClickListener: ((position: Int, view: View) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Highlight) = with(binding) {
        tvTitle.text = item.title
        ivHighlight.loadImage(item.image)

        root.setOnSingleClickListener {
            onHighlightClickListener?.invoke(bindingAdapterPosition, root)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onHighlightClickListener: ((position: Int, view: View) -> Unit)?) = MCHighlightsVH(
            ItemHighlightMarketingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onHighlightClickListener
        )
    }
}
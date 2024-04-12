package kg.nurtelecom.ostories.progress


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import kg.nurtelecom.ostories.R

class OStoriesProgressBar : View {

    var segmentCount: Int = resources.getInteger(R.integer.default_segments_count)
        set(value) {
            field = value
            this.initSegments()
        }

    var margin: Int = resources.getDimensionPixelSize(R.dimen.default_segment_margin)
        private set
    var radius: Int = resources.getDimensionPixelSize(R.dimen.default_corner_radius)
        private set

    var segmentBackgroundColor: Int = R.color.grey_progress_background
        private set
    var segmentSelectedBackgroundColor: Int = R.color.grey_progress_color
        private set

    var timePerSegmentMs: Long =
        resources.getInteger(R.integer.default_time_per_segment_ms).toLong()
        private set

    private var segments = mutableListOf<Segment>()
    private val selectedSegment: Segment?
        get() = segments.firstOrNull { it.animationState == Segment.AnimationState.ANIMATING }
    private val selectedSegmentIndex: Int
        get() = segments.indexOf(this.selectedSegment)

    private var animator = ValueAnimator().apply {
        duration = timePerSegmentMs
        setFloatValues(0f, 100f)
        interpolator = LinearInterpolator()
        addUpdateListener {
            selectedSegment?.setProgress(it.animatedValue as Float)
            invalidate()
        }
        doOnEnd {
            loadSegment(offset = 1)
        }
    }

    val segmentWidth: Float
        get() = (measuredWidth - margin * (segmentCount - 1)).toFloat() / segmentCount

    var listener: OStoriesProgressBarListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SegmentedProgressBar, 0, 0)
        typedArray.run {
            segmentCount =
                getInt(R.styleable.SegmentedProgressBar_totalSegments, segmentCount)
            margin =
                getDimensionPixelSize(
                    R.styleable.SegmentedProgressBar_segmentMargins,
                    margin
                )
            radius =
                getDimensionPixelSize(
                    R.styleable.SegmentedProgressBar_segmentCornerRadius,
                    radius
                )
            segmentBackgroundColor =
                getResourceId(
                    R.styleable.SegmentedProgressBar_segmentBackgroundColor,
                    segmentBackgroundColor
                )
            segmentSelectedBackgroundColor =
                getResourceId(
                    R.styleable.SegmentedProgressBar_segmentSelectedBackgroundColor,
                    ContextCompat.getColor(context, segmentSelectedBackgroundColor)
                )
            timePerSegmentMs =
                getInt(
                    R.styleable.SegmentedProgressBar_timePerSegment,
                    timePerSegmentMs.toInt()
                ).toLong()
            recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        segments.forEachIndexed { index, segment ->
            val drawingComponents = getDrawingComponents(segment, index)
            drawingComponents.first.forEachIndexed { drawingIndex, rectangle ->
                canvas.drawRoundRect(
                    rectangle,
                    radius.toFloat(),
                    radius.toFloat(),
                    drawingComponents.second[drawingIndex]
                )
            }
        }
    }

    fun start() {
        loadSegment(offset = 1)
    }

    fun pause() {
        animator.pause()
    }

    fun resume() {
        animator.resume()
    }

    fun reset() {
        this.segments.map { it.animationState = Segment.AnimationState.IDLE }
        this.invalidate()
    }

    fun next() {
        loadSegment(offset = 1)
    }

    fun previous() {
        loadSegment(offset = -1)
    }

    fun restartSegment() {
        loadSegment(offset = 0)
    }

    fun setPosition(position: Int) {
        loadSegment(offset = position - this.selectedSegmentIndex)
    }

    private fun loadSegment(offset: Int) {
        val oldSegmentIndex = this.segments.indexOf(this.selectedSegment)
        val nextSegmentIndex = oldSegmentIndex + offset

        when {
            nextSegmentIndex < 0 -> {
                this.listener?.onStartReached()
                return
            }
            nextSegmentIndex >= segmentCount -> {
                this.listener?.onCompleted()
                return
            }
        }

        segments.mapIndexed { index, segment ->
            if (index <= nextSegmentIndex) segment.animationState = Segment.AnimationState.ANIMATED
            else segment.animationState = Segment.AnimationState.IDLE
        }

        val nextSegment = this.segments.getOrNull(nextSegmentIndex)

        if (nextSegment != null) {
            nextSegment.animationState = Segment.AnimationState.ANIMATING
            animator.start()
            this.listener?.onSegmentChange(oldSegmentIndex, this.selectedSegmentIndex)
        } else {
            animator.cancel()
            this.listener?.onCompleted()
        }
    }

    private fun initSegments() {
        this.segments.clear()
        segments.addAll(List(segmentCount) { Segment() })
        reset()
    }
}
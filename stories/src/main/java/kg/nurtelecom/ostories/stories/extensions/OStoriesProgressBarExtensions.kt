package kg.nurtelecom.ostories.stories.extensions

import android.graphics.Paint
import android.graphics.RectF
import kg.nurtelecom.ostories.stories.progress.OStoriesProgressBar
import kg.nurtelecom.ostories.stories.model.Segment

fun OStoriesProgressBar.getDrawingComponents(
    segment: Segment,
    segmentIndex: Int
): Pair<MutableList<RectF>, MutableList<Paint>> {

    val rectangles = mutableListOf<RectF>()
    val paints = mutableListOf<Paint>()
    val segmentWidth = segmentWidth
    val startBound = segmentIndex * segmentWidth + ((segmentIndex) * margin)
    val endBound = startBound + segmentWidth

    val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = segmentBackgroundColor
    }

    val selectedBackgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = segmentSelectedBackgroundColor
    }

    //Background component
    if (segment.animationState == Segment.AnimationState.ANIMATED) {
        rectangles.add(RectF(startBound, height.toFloat(), endBound, 0f))
        paints.add(selectedBackgroundPaint)
    } else {
        rectangles.add(RectF(startBound, height.toFloat(), endBound, 0f))
        paints.add(backgroundPaint)
    }

    //Progress component
    if (segment.animationState == Segment.AnimationState.ANIMATING) {
        rectangles.add(
            RectF(
                startBound,
                height.toFloat(),
                startBound + segment.progressPercentage * segmentWidth,
                0f
            )
        )
        paints.add(selectedBackgroundPaint)
    }

    return Pair(rectangles, paints)
}
package kg.nurtelecom.ostories.story

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CubeTransformer: ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val deltaY = 0.5f
        val pivotX = if (position < 0f) page.width.toFloat() else 0f
        val pivotY = page.height * deltaY
        val rotationY = 45f * position
        page.pivotX = pivotX
        page.pivotY = pivotY
        page.rotationY = rotationY
    }
}
package kg.nurtelecom.ostories

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import kg.nurtelecom.ostories.databinding.ActivityMainBinding
import kg.nurtelecom.ostories.model.StoryMock
import kg.nurtelecom.ostories.story.StoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.oStories.setUp(StoryMock.fetchHighlights(), supportFragmentManager)
        with(binding) {
            btn.setOnClickListener {
//                showFragment(StoryFragment.newInstance(), StoryFragment.TAG)
                setupPlayScreenFragment(it.left, it.top)
            }
        }


        setContentView(binding.root)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.play_screen_frame_layout, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun setupPlayScreenFragment(posX: Int, posY: Int) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.play_screen_frame_layout, PlayScreenFragment.newInstance(posX, posY), PlayScreenFragment.TAG)
            .addToBackStack(PlayScreenFragment.TAG)
            .commit()
    }
}

fun Context.toDp(px: Float): Int {
    val density = resources.displayMetrics.density
    return (px*density).toInt()
}
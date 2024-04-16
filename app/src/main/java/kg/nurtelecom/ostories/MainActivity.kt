package kg.nurtelecom.ostories

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kg.nurtelecom.ostories.databinding.ActivityMainBinding
import kg.nurtelecom.ostories.model.StoryMock

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.oStories.setUp(StoryMock.fetchHighlights(), supportFragmentManager)

        setContentView(binding.root)
    }

}

fun Context.toDp(px: Float): Int {
    val density = resources.displayMetrics.density
    return (px*density).toInt()
}
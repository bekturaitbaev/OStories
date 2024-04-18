package kg.nurtelecom.ostories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kg.nurtelecom.ostories.databinding.ActivityMainBinding
import kg.nurtelecom.ostories.stories.model.StoryMock

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.oStories.setUp(StoryMock.fetchHighlights(), supportFragmentManager)

        setContentView(binding.root)
    }

}
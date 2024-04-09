package kg.nurtelecom.ostories.model

data class Story(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    var isWatched: Boolean = false
)

data class Highlight(
    val id: Long? = null,
    val image: String? = null,
    val stories: List<Story> = emptyList()
)

object StoryMock {
    fun fetchHighlights() = listOf(
        Highlight(
            1,
            "https://img.freepik.com/free-photo/luxurious-car-parked-highway-with-illuminated-headlight-sunset_181624-60607.jpg",
            fetchStories()
        ),
        Highlight(
            2,
            "https://img.freepik.com/premium-photo/car-positioned-against-solid-backdrop_931878-223622.jpg",
            fetchStories()
        ),
        Highlight(
            3,
            "https://img.freepik.com/free-photo/view-3d-car-with-city_23-2151005377.jpg",
            fetchStories()
        ),
        Highlight(
            4,
            "https://img.freepik.com/free-photo/view-three-dimensional-car-with-nature-landscape_23-2151005321.jpg",
            fetchStories()
        ),
        Highlight(
            5,
            "https://img.freepik.com/premium-photo/black-ford-sedan-parked-cobblestone-street_899870-20049.jpg",
            fetchStories()
        )
    )

    fun fetchStories() = listOf(
        Story(
            1,
            "Title",
            "Description",
            "https://img.freepik.com/free-photo/businesswoman-getting-taxi-cab_23-2149236752.jpg",
            true
        ),
        Story(
            2,
            "Title",
            "Description",
            "https://img.freepik.com/free-psd/vehicle-sales-social-media-stories-template_621600-79.jpg",
            true
        ),
        Story(
            3,
            "Title",
            "Description",
            "https://img.freepik.com/free-psd/car-rental-automotive-instagram-facebook-story-template_106176-2507.jpg",
            false
        ),
        Story(
            4,
            "Title",
            "Description",
            "https://img.freepik.com/free-photo/red-vehicle-street_417767-545.jpg",
            false
        ),
        Story(
            5,
            "Title",
            "Description",
            "https://img.freepik.com/free-photo/businesswoman-getting-taxi-cab_23-2149236752.jpg",
            false
        )
    )
}
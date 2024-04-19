package kg.nurtelecom.ostories.stories.model

data class Story(
    val id: Long? = null,
    val orderNumber: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val buttonModel: ButtonModel? = null,
    var isViewed: Boolean = false,
    val bannerImage: String? = null
)

data class Highlight(
    val id: Long? = null,
    val orderNumber: Int? = null,
    val title: String? = null,
    val image: String? = null,
    val stories: List<Story> = emptyList(),
    val isMarketingCenter: Boolean = false,
    val borderColors: List<String> = emptyList()
)

data class ButtonModel(
    val title: String? = null,
    val deepLink: String? = null,
    val titleColor: String? = null,
    val backgroundColor: String? = null
)

object StoryMock {
    fun fetchHighlights() = listOf(
        Highlight(
            1,
            image = "https://img.freepik.com/free-photo/luxurious-car-parked-highway-with-illuminated-headlight-sunset_181624-60607.jpg",
            stories = fetchStories(),
            isMarketingCenter = true
        ),
        Highlight(
            2,
            image = "https://img.freepik.com/premium-photo/car-positioned-against-solid-backdrop_931878-223622.jpg",
            stories = fetchStories()
        ),
        Highlight(
            3,
            image = "https://img.freepik.com/free-photo/view-3d-car-with-city_23-2151005377.jpg",
            stories = fetchStories()
        ),
        Highlight(
            4,
            image = "https://img.freepik.com/free-photo/view-three-dimensional-car-with-nature-landscape_23-2151005321.jpg",
            stories = fetchStories()
        ),
        Highlight(
            5,
            image = "https://img.freepik.com/premium-photo/black-ford-sedan-parked-cobblestone-street_899870-20049.jpg",
            stories = fetchStories()
        )
    )

    fun fetchStories() = listOf(
        Story(
            1,
            0,
            "Title",
            "Description",
            "https://img.freepik.com/free-photo/businesswoman-getting-taxi-cab_23-2149236752.jpg",
            null,
            true,
            null
        ),
        Story(
            2,
            1,
            "Title",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "https://img.freepik.com/free-psd/vehicle-sales-social-media-stories-template_621600-79.jpg",
            null,
            true,
            null
        ),
        Story(
            3,
            2,
            "Title",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "https://img.freepik.com/free-psd/car-rental-automotive-instagram-facebook-story-template_106176-2507.jpg",
            null,
            false,
            null
        ),
        Story(
            4,
            3,
            "Title",
            null,
            "https://img.freepik.com/free-photo/red-vehicle-street_417767-545.jpg",
            null,
            false,
            null
        ),
        Story(
            5,
            4,
            "Title",
            "Description",
            "https://img.freepik.com/free-photo/businesswoman-getting-taxi-cab_23-2149236752.jpg",
            null,
            false,
            null
        )
    )
}
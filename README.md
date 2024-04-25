# OStories

[![](https://jitpack.io/v/bekturaitbaev/OStories.svg)](https://jitpack.io/#bekturaitbaev/OStories)


## Setting up library

Add Jitpack repository to your project

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add library to your app's `build.gradle` dependencies

```
dependencies {
    implementation 'com.github.bekturaitbaev:OStories:${latest_version}'
}
```

## Usage

Usage in xml layout file

```
<kg.nurtelecom.ostories.stories.story.highlight.OStoriesView
    android:id="@+id/story_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

You can change ProgressBar attributes by overriding attributes

```
<attr name="segmentCount" format="integer" /> // Number of segments in ProgressBar. Default: 1
<attr name="segmentDuration" format="integer" /> // Duration of each segment. Default: 10 000 ms
<attr name="segmentSpace" format="dimension" /> // Margin between segments. Default: 8dp
<attr name="segmentCornerRadius" format="dimension" /> // Radius of progressBar. Default: 4dp
<attr name="segmentBackgroundColor" format="color" /> // Background color of ProgressBar. Default: #66EFEFF4
<attr name="segmentSelectedBackgroundColor" format="color" /> // Highlighted ProgressBar color. Default: #EFEFF4
```

Setting up OStoriesView inside Fragment/Activity

```
story_view.setUp(
    listOf<Highlight>(), // List of stories
    fragmentManager,
    isMoreItemVisible = true // To show More Items button at the end. Default: true
)
```
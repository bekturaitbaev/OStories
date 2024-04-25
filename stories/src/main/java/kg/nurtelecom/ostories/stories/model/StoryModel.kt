package kg.nurtelecom.ostories.stories.model

import android.os.Parcel
import android.os.Parcelable

data class Story(
    val id: Long? = null,
    val orderNumber: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val buttonModel: ButtonModel? = null,
    var isViewed: Boolean = false,
    val bannerImage: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ButtonModel::class.java.classLoader),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(orderNumber)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeParcelable(buttonModel, flags)
        parcel.writeByte(if (isViewed) 1 else 0)
        parcel.writeString(bannerImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Story> {
        override fun createFromParcel(parcel: Parcel): Story {
            return Story(parcel)
        }

        override fun newArray(size: Int): Array<Story?> {
            return arrayOfNulls(size)
        }
    }
}
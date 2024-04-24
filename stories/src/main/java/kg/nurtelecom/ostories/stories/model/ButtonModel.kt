package kg.nurtelecom.ostories.stories.model

import android.os.Parcel
import android.os.Parcelable

data class ButtonModel(
    val title: String? = null,
    val deepLink: String? = null,
    val titleColor: String? = null,
    val backgroundColor: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(deepLink)
        parcel.writeString(titleColor)
        parcel.writeString(backgroundColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ButtonModel> {
        override fun createFromParcel(parcel: Parcel): ButtonModel {
            return ButtonModel(parcel)
        }

        override fun newArray(size: Int): Array<ButtonModel?> {
            return arrayOfNulls(size)
        }
    }
}
package id.nns.githubuser.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
        var id: Long? = 0,
        var username: String? = "",
        var avatar: String? = ""
) : Parcelable
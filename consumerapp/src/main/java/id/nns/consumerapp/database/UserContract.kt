package id.nns.consumerapp.database

import android.net.Uri
import android.provider.BaseColumns

object UserContract {
    const val AUTHORITY = "id.nns.githubuser"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            private const val TABLE_NAME = "user_favorite"
            const val COLUMN_ID = "_id"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}
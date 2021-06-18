package id.nns.githubuser.database

import android.database.Cursor
import id.nns.githubuser.data.DataUser

object MappingHelper {
    fun mapCursorToUserArrayList(cursor: Cursor?) : ArrayList<DataUser> {
        val usersList = ArrayList<DataUser>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_ID))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_AVATAR_URL))
                usersList.add(DataUser(id, username, avatar))
            }
        }
        return usersList
    }
}
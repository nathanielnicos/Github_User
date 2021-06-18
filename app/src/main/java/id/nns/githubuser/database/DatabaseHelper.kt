package id.nns.githubuser.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.nns.githubuser.database.UserContract.UserColumns.Companion.COLUMN_AVATAR_URL
import id.nns.githubuser.database.UserContract.UserColumns.Companion.COLUMN_ID
import id.nns.githubuser.database.UserContract.UserColumns.Companion.COLUMN_USERNAME
import id.nns.githubuser.database.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbUserFavorite"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " ($COLUMN_ID INTEGER PRIMARY KEY," +
                " $COLUMN_USERNAME TEXT NOT NULL," +
                " $COLUMN_AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
}
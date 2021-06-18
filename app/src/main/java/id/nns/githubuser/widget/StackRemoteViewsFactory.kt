package id.nns.githubuser.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import id.nns.githubuser.R

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() = Unit

    override fun onDataSetChanged() {
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user1))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user2))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user3))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user4))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user5))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user6))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user7))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user8))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user9))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.user10))
    }

    override fun onDestroy() = Unit

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        val extras = bundleOf(
            FavoriteWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}
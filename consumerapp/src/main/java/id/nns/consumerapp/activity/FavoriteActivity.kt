package id.nns.consumerapp.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.nns.consumerapp.adapter.UserAdapter
import id.nns.consumerapp.data.DataUser
import id.nns.consumerapp.database.MappingHelper
import id.nns.consumerapp.database.UserContract.UserColumns.Companion.CONTENT_URI
import id.nns.consumerapp.databinding.ActivityFavoriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Consumer Favorite"

        binding.rvUserFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvUserFavorite.setHasFixedSize(true)

        adapter = UserAdapter()
        binding.rvUserFavorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUsersAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<DataUser>(EXTRA_STATE)
            if (list != null) {
                adapter.mData = list
            }
        }
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null,null)
                MappingHelper.mapCursorToUserArrayList(cursor)
            }

            val users = deferredUsers.await()
            if (users.size > 0) {
                adapter.mData = users
            } else {
                adapter.mData = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }

    override fun onResume() {
        super.onResume()
        loadUsersAsync()
    }

}
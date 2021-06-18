package id.nns.githubuser.activity

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.nns.githubuser.R
import id.nns.githubuser.adapter.SectionsPagerAdapter
import id.nns.githubuser.data.DataUser
import id.nns.githubuser.database.MappingHelper
import id.nns.githubuser.database.UserContract
import id.nns.githubuser.database.UserContract.UserColumns.Companion.CONTENT_URI
import id.nns.githubuser.databinding.ActivityDetailBinding
import id.nns.githubuser.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2
        )

        const val KEY_USER = "key_user"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri

    private var user: DataUser? = null
    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)
        Log.d("DetailActivity", "statusFavorite: $statusFavorite")

        // Get intent
        user = intent.getParcelableExtra(KEY_USER)

        lookForUser()
    }

    private fun lookForUser() {
        Glide.with(this@DetailActivity)
            .load(user?.avatar)
            .into(binding.civAvatarDetail)

        // Check favorite state
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.id)
        Log.d("DetailActivity", "Delete url: $uriWithId")

        val cursorUser = contentResolver.query(uriWithId, null, null, null, null)
        val arrayUser = MappingHelper.mapCursorToUserArrayList(cursorUser)

        for (i in arrayUser) {
            if (i.id == user?.id) {
                statusFavorite = true
                setStatusFavorite(statusFavorite)
            } else {
                statusFavorite = false
                setStatusFavorite(statusFavorite)
            }
        }

        // View Model
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        detailViewModel.setDetail(
            user?.username.toString(),
            binding.tvName,
            binding.tvCompany,
            binding.tvLocation,
            binding.tvFollowers,
            binding.tvFollowing
        )
        showLoading(false)

        // View Pager and Tabs
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user?.username
        Log.d("DetailActivity", sectionsPagerAdapter.username.toString())

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        // Click favorite fab
        binding.fabFavorite.setOnClickListener {
            statusFavorite = !statusFavorite

            if (statusFavorite) {
                val values = ContentValues()
                values.put(UserContract.UserColumns.COLUMN_ID, user?.id)
                values.put(UserContract.UserColumns.COLUMN_USERNAME, user?.username)
                values.put(UserContract.UserColumns.COLUMN_AVATAR_URL, user?.avatar)

                val url = contentResolver.insert(CONTENT_URI, values)
                Log.d("DetailActivity", "Insert url: $url")

                setStatusFavorite(statusFavorite)
                Toast.makeText(this, "Mark ${user?.username} as favorite!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                contentResolver.delete(uriWithId, null, null)

                setStatusFavorite(statusFavorite)
                Toast.makeText(this, "Unmark ${user?.username} as favorite!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
        }
    }

}
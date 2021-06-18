package id.nns.githubuser.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.nns.githubuser.R
import id.nns.githubuser.adapter.UserAdapter
import id.nns.githubuser.viewmodel.UserViewModel
import id.nns.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUser.adapter = adapter

        showLoading(false)

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(UserViewModel::class.java)

        userViewModel.getList().observe(this, { userItems ->
            showLoading(true)
            if (userItems != null) {
                adapter.mData = userItems
                showLoading(false)
            }
        })

        binding.fabGotoFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                userViewModel.setList(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.change_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
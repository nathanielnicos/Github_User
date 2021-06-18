package id.nns.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.nns.githubuser.BuildConfig
import id.nns.githubuser.data.DataUser
import org.json.JSONObject

class UserViewModel : ViewModel() {
    val listDataUser = MutableLiveData<ArrayList<DataUser>>()

    fun setList(userName: String) {
        val listUser = ArrayList<DataUser>()

        val myToken = BuildConfig.GITHUB_TOKEN
        val url = "https://api.github.com/search/users?q=${userName}"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $myToken")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                val result = String(responseBody)

                Log.d("MainActivity", result)

                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val id = item.getLong("id")
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val user = DataUser()

                        user.id = id
                        user.username = username
                        user.avatar = avatar

                        listUser.add(user)
                    }

                    listDataUser.postValue(listUser)

                } catch (e: Exception) {
                    Log.d("MainActivity", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                Log.d("MainActivity", error?.message.toString())
            }

        })
    }

    fun getList(): LiveData<ArrayList<DataUser>> {
        return listDataUser
    }
}
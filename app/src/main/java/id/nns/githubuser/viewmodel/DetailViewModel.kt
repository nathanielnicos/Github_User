package id.nns.githubuser.viewmodel

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.nns.githubuser.BuildConfig
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    fun setDetail(
        userName: String,
        tvName: TextView,
        tvCompany: TextView,
        tvLocation: TextView,
        tvFollowers: TextView,
        tvFollowing: TextView
    ) {
        val myToken = BuildConfig.GITHUB_TOKEN
        val url = "https://api.github.com/users/$userName"

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

                    val name = responseObject.getString("name")
                    val company = responseObject.getString("company")
                    val location = responseObject.getString("location")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")

                    if (name != "null") {
                        tvName.text = name
                    } else {
                        tvName.text = "-"
                    }

                    if (company != "null") {
                        tvCompany.text = company
                    } else {
                        tvCompany.text = "-"
                    }

                    if (location != "null") {
                        tvLocation.text = location
                    } else {
                        tvLocation.text = "-"
                    }

                    tvFollowers.text = followers.toString()
                    tvFollowing.text = following.toString()

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
}
package id.nns.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.nns.githubuser.adapter.UserAdapter
import id.nns.githubuser.databinding.FragmentFollowingBinding
import id.nns.githubuser.viewmodel.FollowViewModel

class FollowingFragment : Fragment() {

    companion object {
        const val ARG_USERNAME = "username"

        fun newInstance(username: String) : FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private var userName: String? = null
    private lateinit var followViewModel: FollowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName = arguments?.getString(ARG_USERNAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter

        Log.d("FollowingFragment", userName.toString())

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowViewModel::class.java)

        showLoading(true)

        followViewModel.setList(userName.toString(), "following")

        activity?.let {
            followViewModel.getList().observe(it, { userItems ->
                if (userItems != null) {
                    adapter.mData = userItems
                    showLoading(false)
                }
            })
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
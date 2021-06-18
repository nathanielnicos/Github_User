package id.nns.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import id.nns.githubuser.data.DataUser
import id.nns.githubuser.activity.DetailActivity
import id.nns.githubuser.databinding.ListUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var mData = ArrayList<DataUser>()
        set(mData) {
            if (mData.size > 0) {
                this.mData.clear()
            }
            this.mData.addAll(mData)

            notifyDataSetChanged()
        }

    private lateinit var binding: ListUserBinding

    inner class UserViewHolder(binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root) {

        private val username: TextView = binding.tvUsername
        private val avatar: CircleImageView = binding.civAvatar

        fun bind(dataUser: DataUser) {
            username.text = dataUser.username

            with(itemView) {
                Glide.with(context)
                        .load(dataUser.avatar)
                        .into(avatar)

                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.KEY_USER, dataUser)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}
package org.wit.blocky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.card_user.view.*
import org.wit.blocky.R
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.user.UserModel
import org.wit.blocky.views.users.UserViewModel

class UserAdapter(
    private val viewModel: UserViewModel,
    private val listener: UserListener,
    private val app: MainApp
) :
    RecyclerView.Adapter<UserAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_user, parent, false),
            app
        )
    }

    override fun getItemCount(): Int {
        val size = viewModel.users.value?.size
        return if (size != null) size else {
            0
        }
    }

    override fun onBindViewHolder(holder: UserAdapter.MainHolder, position: Int) {
        val user = viewModel.users.value!![holder.adapterPosition]
        holder.bind(user, listener)
    }

    class MainHolder constructor(
        itemView: View,
        val app: MainApp
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserModel, listener: UserListener) {
            itemView.user_email.text = user.email
            itemView.user_name.text = user.displayName
            itemView.user_follow.isChecked = app.currentUser.following.contains(user.authId)

            itemView.user_follow.setOnClickListener {
                val following = app.currentUser.following.contains(user.authId)
                if (!following) {
                    app.currentUser.following.add(user.authId)
                    app.users.update(app.currentUser)
                } else {
                    app.currentUser.following.remove(user.authId)
                    app.users.update(app.currentUser)
                }
            }

            itemView.setOnClickListener {
                listener.onUserClick(user)
            }

            if (user.photoUrl.isNotEmpty()) {
                Glide
                    .with(itemView.context)
                    .load(user.photoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemView.user_image)
            }
        }
    }
}

interface UserListener {
    fun onUserClick(user: UserModel)
}
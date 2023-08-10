package com.mzc.mzti.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemUserProfileMbtiCardBinding
import com.mzc.mzti.profile.viewholder.UserProfileMbtiCardViewHolder

private const val TAG: String = "UserProfileAdapter"

class UserProfileAdapter(
    private val viewHolderList: List<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            // MBTI증
            R.layout.item_user_profile_mbti_card -> {
                UserProfileMbtiCardViewHolder(
                    ItemUserProfileMbtiCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                UserProfileMbtiCardViewHolder(
                    ItemUserProfileMbtiCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.item_user_profile_mbti_card -> {

            }
        }
    }

    override fun getItemCount(): Int = viewHolderList.size

    override fun getItemViewType(position: Int): Int = when (viewHolderList[position]) {
        else -> R.layout.item_user_profile_mbti_card
    }

}
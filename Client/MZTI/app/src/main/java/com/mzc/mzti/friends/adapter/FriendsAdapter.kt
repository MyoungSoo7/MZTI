package com.mzc.mzti.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemFriendsCountBinding
import com.mzc.mzti.databinding.ItemFriendsMyProfileBinding
import com.mzc.mzti.databinding.ItemFriendsOtherProfileBinding
import com.mzc.mzti.friends.viewholder.FriendsCountViewHolder
import com.mzc.mzti.friends.viewholder.FriendsMyProfileViewHolder
import com.mzc.mzti.friends.viewholder.FriendsOtherProfileViewHolder
import com.mzc.mzti.model.data.friends.FriendsDataWrapper
import com.mzc.mzti.model.data.friends.FriendsLayoutType
import com.mzc.mzti.model.data.friends.FriendsMyProfileData
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.MBTI

class FriendsAdapter(
    private val items: ArrayList<FriendsDataWrapper>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var friendsListener: FriendsListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            // 내 프로필 아이템
            R.layout.item_friends_my_profile -> {
                FriendsMyProfileViewHolder(
                    ItemFriendsMyProfileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ) { pos ->
                    val data = items[pos].data
                    if (data is FriendsMyProfileData) {
                        friendsListener?.showMbtiInfo(data.mbti)
                    }
                }
            }

            // 친구 N명
            R.layout.item_friends_count -> {
                FriendsCountViewHolder(
                    ItemFriendsCountBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            // 친구 프로필 아이템
            R.layout.item_friends_other_profile -> {
                FriendsOtherProfileViewHolder(
                    ItemFriendsOtherProfileBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onFriendsItemClicked = { pos ->
                        val data = items[pos].data
                        if (data is FriendsOtherProfileData) {
                            friendsListener?.showMbtiInfo(data.mbti)
                        }
                    },
                    onFriendsItemLongClicked = { pos ->
                        val data = items[pos].data
                        if (data is FriendsOtherProfileData) {
                            friendsListener?.showRemoveFriendsDialog(data.id)
                        }
                    }
                )
            }

            else -> {
                FriendsCountViewHolder(
                    ItemFriendsCountBinding.inflate(
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
            // 내 프로필 아이템
            R.layout.item_friends_my_profile -> {
                val data = items[position].data as FriendsMyProfileData
                if (holder is FriendsMyProfileViewHolder) {
                    holder.bindData(data)
                }
            }

            // 친구 N명
            R.layout.item_friends_count -> {
                val friendsCnt = items[position].data as Int
                if (holder is FriendsCountViewHolder) {
                    holder.bindData(friendsCnt)
                }
            }

            // 친구 프로필 아이템
            R.layout.item_friends_other_profile -> {
                val data = items[position].data as FriendsOtherProfileData
                if (holder is FriendsOtherProfileViewHolder) {
                    holder.bindData(data)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position].type) {
        FriendsLayoutType.MY_PROFILE -> R.layout.item_friends_my_profile
        FriendsLayoutType.FRIEND_COUNT -> R.layout.item_friends_count
        FriendsLayoutType.OTHER_PROFILE -> R.layout.item_friends_other_profile
    }

    interface FriendsListener {
        fun showMbtiInfo(mbti: MBTI)

        fun showRemoveFriendsDialog(friendId: String)
    }

}
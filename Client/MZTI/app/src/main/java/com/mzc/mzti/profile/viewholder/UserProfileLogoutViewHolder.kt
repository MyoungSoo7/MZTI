package com.mzc.mzti.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.databinding.ItemUserProfileLogoutBinding

class UserProfileLogoutViewHolder(
    private val binding: ItemUserProfileLogoutBinding,
    private val onLogoutBtnClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            clLogout.setOnClickListener {
                onLogoutBtnClicked()
            }
        }
    }

    fun bindData() {

    }

}
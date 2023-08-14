package com.mzc.mzti.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.databinding.ItemUserProfileEditBinding

class UserProfileEditViewHolder(
    private val binding: ItemUserProfileEditBinding,
    private val onProfileEditBtnClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            clProfileEdit.setOnClickListener {
                onProfileEditBtnClicked()
            }
        }
    }

    fun bindData() {
    }

}
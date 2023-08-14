package com.mzc.mzti.profile.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.databinding.ItemUserProfileMbtiCardBinding

class UserProfileMbtiCardViewHolder(
    private val binding: ItemUserProfileMbtiCardBinding,
    private val onCardSaveBtnClicked: (cardView: View) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            ibMbtiCardDownload.setOnClickListener {
                onCardSaveBtnClicked(clMbtiCard)
            }
        }
    }

    fun bindData() {
        val pos = bindingAdapterPosition

        binding.apply {

        }
    }

}
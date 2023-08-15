package com.mzc.mzti.friends.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity

class FriendMbtiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_bottom_200, R.anim.exit_to_bottom_200)

        setContentView(R.layout.activity_friend_mbti)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_from_bottom_200, R.anim.exit_to_bottom_200)
    }

}
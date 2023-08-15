package com.mzc.mzti.model.data.mbti

import com.mzc.mzti.R

enum class MBTI {
    ENFJ,
    ENFP,
    ENTJ,
    ENTP,
    ESFJ,
    ESFP,
    ESTJ,
    ESTP,
    INFJ,
    INFP,
    INTJ,
    INTP,
    ISFJ,
    ISFP,
    ISTJ,
    ISTP,

    MZTI
}

fun getMBTI(strMBTI: String): MBTI {
    val mbtiList = MBTI.values()

    for (mbti in mbtiList) {
        if (mbti.name == strMBTI.uppercase()) {
            return mbti
        }
    }

    return MBTI.MZTI
}

fun getProfileImgResId(mbti: MBTI): Int = when (mbti) {
    MBTI.ENFJ -> R.drawable.mbti_profile_enfj
    MBTI.ENFP -> R.drawable.mbti_profile_enfp
    MBTI.ENTJ -> R.drawable.mbti_profile_entj
    MBTI.ENTP -> R.drawable.mbti_profile_entp
    MBTI.ESFJ -> R.drawable.mbti_profile_esfj
    MBTI.ESFP -> R.drawable.mbti_profile_esfp
    MBTI.ESTJ -> R.drawable.mbti_profile_estj
    MBTI.ESTP -> R.drawable.mbti_profile_esfp
    MBTI.INFJ -> R.drawable.mbti_profile_infj
    MBTI.INFP -> R.drawable.mbti_profile_infp
    MBTI.INTJ -> R.drawable.mbti_profile_intj
    MBTI.INTP -> R.drawable.mbti_profile_intp
    MBTI.ISFJ -> R.drawable.mbti_profile_isfj
    MBTI.ISFP -> R.drawable.mbti_profile_isfp
    MBTI.ISTJ -> R.drawable.mbti_profile_istj
    MBTI.ISTP -> R.drawable.mbti_profile_istp
    MBTI.MZTI -> R.drawable.mbti_profile_mzti
}
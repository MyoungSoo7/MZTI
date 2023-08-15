package com.mzc.mzti.model.data.mbti

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
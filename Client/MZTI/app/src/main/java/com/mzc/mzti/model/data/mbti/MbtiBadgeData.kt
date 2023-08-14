package com.mzc.mzti.model.data.mbti

private const val TAG: String = "MbtiBadgeData"

data class MbtiBadgeData(
    val mbti: MBTI,
    val mbtiSize0: MbtiSize,
    val mbtiSize1: MbtiSize,
    val mbtiSize2: MbtiSize,
    val mbtiSize3: MbtiSize
) {

    override fun toString(): String {
        return "$TAG{" +
                "mbti=${mbti.name}, " +
                "mbtiSize0=${mbtiSize0}, " +
                "mbtiSize1=${mbtiSize1}, " +
                "mbtiSize2=${mbtiSize2}, " +
                "mbtiSize3=${mbtiSize3}}"
    }

}
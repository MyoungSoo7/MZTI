package com.mzc.mzti.model.data.compare

enum class CompareMbtiType {
    /**
     * 요약
     */
    DESCRIPTION,

    /**
     * 키워드
     */
    KEYWORD,

    /**
     * 어울리는 일
     */
    GOOD_JOB,

    /**
     * 연애 성향
     */
    LOVE_STYLE,

    /**
     * 잘 맞는 사람
     */
    GOOD_PEOPLE,

    /**
     * Best 조합
     */
    GOOD_MBTI,

    /**
     * Worst 조합
     */
    WORST_MBTI,

    /**
     * 자주하는 말
     */
    TALKING_HABIT,

    /**
     * 가상 인물
     */
    VIRTUAL_PEOPLE
}

fun getCompareMbtiType(strType: String): CompareMbtiType = when (strType) {
    "descriptions" -> CompareMbtiType.DESCRIPTION
    "keyword" -> CompareMbtiType.KEYWORD
    "goodJob" -> CompareMbtiType.GOOD_JOB
    "loveStyle" -> CompareMbtiType.LOVE_STYLE
    "goodPeople" -> CompareMbtiType.GOOD_PEOPLE
    "goodMBTIs" -> CompareMbtiType.GOOD_MBTI
    "badMBTIs" -> CompareMbtiType.WORST_MBTI
    "talkingHabbit" -> CompareMbtiType.TALKING_HABIT
    "virtualPeople" -> CompareMbtiType.VIRTUAL_PEOPLE
    else -> CompareMbtiType.VIRTUAL_PEOPLE
}
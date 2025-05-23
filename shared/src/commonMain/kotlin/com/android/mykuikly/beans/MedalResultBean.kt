package com.android.mykuikly.beans

import com.tencent.kuikly.core.log.KLog
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject

/**
 * Created by KeithLee on 2025/5/23.
 * Introduction:
 */
class MedalResultBean(var data: JSONObject) {
    var medalList: List<MedalBean> = emptyList()
    var commentH5Url: String? = null
    var grantCount = 0
    var totalCount = 0
    var wearMedal: MedalBean? = null

    init {
        commentH5Url = data.optString("commentH5Url")
        var arr = data.optJSONArray("medalList")
        if (arr != null) {
            var list = mutableListOf<MedalBean>()
            for (i in 0 until arr.length()) {
                list.add(MedalBean(arr.optJSONObject(i)))
            }
            medalList = list
        }

        medalList =  medalList.sortedBy { it.grantTime }.reversed()

        for (bean in medalList) {
            if (bean.wearing) {
                wearMedal = bean
            }
            if (bean.isGranted) {
                grantCount++
            }
        }
        totalCount = medalList.size
    }

    override fun toString(): String {
        return """
            commentH5Url: $commentH5Url
            medalList: ${medalList.joinToString("\n")}
        """.trimIndent()
    }
}

class MedalBean(var data: JSONObject?) {
    var medalId: Int = 0
    var name: String
    var icon: String
    var activeIcon: String
    var url: String
    var shareUrl: String
    var shareTitle: String
    var shareDesc: String
    var comment: String
    var text: String
    var studentId: Long
    var grantTime: Long
    var isGranted: Boolean
    var wearing: Boolean

    init {
        medalId = data?.optInt("medalId") ?: 0
        name = data?.optString("name") ?: ""
        icon = data?.optString("icon") ?: ""
        activeIcon = data?.optString("activeIcon") ?: ""
        url = data?.optString("url") ?: ""
        shareUrl = data?.optString("shareUrl") ?: ""
        shareTitle = data?.optString("shareTitle") ?: ""
        shareDesc = data?.optString("shareDesc") ?: ""
        comment = data?.optString("comment") ?: ""
        text = data?.optString("text") ?: ""
        studentId = data?.optLong("studentId") ?: 0
        grantTime = data?.optLong("grantTime") ?: 0
        isGranted = data?.optBoolean("isGranted") == true
        wearing = data?.optBoolean("wearing") == true
    }

    override fun toString(): String {
        return """
            medalId: $medalId
            name: $name
            icon: $icon
            activeIcon: $activeIcon
            url: $url
            shareUrl: $shareUrl
            shareTitle: $shareTitle
            shareDesc: $shareDesc
            comment: $comment
            text: $text
            studentId: $studentId
            grantTime: $grantTime
            isGranted: $isGranted
            wearing: $wearing
        """.trimIndent()
    }
}

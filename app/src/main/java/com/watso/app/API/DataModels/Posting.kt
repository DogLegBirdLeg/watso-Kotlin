package com.watso.app.API

import com.google.gson.annotations.SerializedName

/** 게시글 API 관련 데이터 모델 */

data class PostComment(
    val content: String
)

data class GetComments(
    val comments: List<Comment>
)

data class Comment(
    val _id: Long,
    @SerializedName("post_id")
    val postId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user_id")
    val userId: Long,
    val nickname: String,
    val status: String,
    val content: String,
    @SerializedName("sub_comments")
    val subComments: List<Comment>?
)

data class PostingResponse(
    val post_id: String,
    val success: Boolean
)
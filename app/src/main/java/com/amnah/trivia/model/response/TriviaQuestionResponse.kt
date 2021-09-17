package com.amnah.trivia.model.response


import com.google.gson.annotations.SerializedName

data class TriviaQuestionResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<Result>
)
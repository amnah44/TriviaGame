package com.amnah.trivia.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.amnah.trivia.R
import com.amnah.trivia.databinding.FragmentQuestionBinding
import com.amnah.trivia.model.response.TriviaQuestionResponse
import com.amnah.trivia.utils.Constant.BASE_URL
import com.amnah.trivia.utils.Constant.TAG
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class QuestionFragment : Fragment() {

    lateinit var binding: FragmentQuestionBinding

    var index = 0
    var point: Int = 0
    var correctAnswer = ""
    var answerQuestion = mutableListOf<String?>()

    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeRequestByUsingOkhttpToGetQuestion()
        getNextQuestion()
        getCorrectAnswer()

    }

    private fun makeRequestByUsingOkhttpToGetQuestion() {
        if (index >= 10) {
            displayWinFragment()
        } else {
            val request = Request.Builder().url(BASE_URL).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i(TAG, "Fail: ${e.message}")
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call, response: Response) {
                    response.body?.string().let { jsonString ->

                        val result = Gson().fromJson(jsonString, TriviaQuestionResponse::class.java)
                        val triviaData = result.results.toMutableList()[index]

                        answerQuestion = mutableListOf(
                            triviaData.incorrectAnswers[0],
                            triviaData.incorrectAnswers[1],
                            triviaData.incorrectAnswers[2],
                            triviaData.correctAnswer
                        ).shuffled().toMutableList()
                        //checkup the data
                        Log.i(TAG, answerQuestion.toString())

                        correctAnswer = triviaData.correctAnswer
                        requireActivity().runOnUiThread {

                            binding.apply {
                                questionBox.text = triviaData.question
                                option1.text = answerQuestion[0]
                                option2.text = answerQuestion[1]
                                option3.text = answerQuestion[2]
                                option4.text = answerQuestion[3]
                                progressScore.progress = index
                                numberOfQuestion.text = "$index/10"
                            }

                            isEnabledButton(value = false)
                        }

                        index++
                        Log.i(TAG, triviaData.correctAnswer)

                    }
                }

            })

        }

    }

    private fun displayWinFragment() {
        val action = QuestionFragmentDirections.actionQuestionFragmentToResultFragment(point)
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

    private fun isEnabledButton(value: Boolean) {
        when (value) {
            false -> binding.submit.isEnabled = false
            true -> binding.submit.isEnabled = true
        }
    }

    @SuppressLint("ResourceType")
    private fun getCorrectAnswer() {
        binding.apply {
            option1.setOnClickListener {
                getAnswer(option1)
                countPoints(option1)
            }
            option2.setOnClickListener {
                getAnswer(option2)
                countPoints(option2)
            }
            option3.setOnClickListener {
                getAnswer(option3)
                countPoints(option3)
            }
            option4.setOnClickListener {
                getAnswer(option4)
                countPoints(option4)
            }
        }
    }

    private fun getAnswer(textView: TextView) {
        when (textView) {
            binding.option1 -> binding.option1.setBackgroundResource(R.drawable.selected_options_shape)
            binding.option2 -> binding.option2.setBackgroundResource(R.drawable.selected_options_shape)
            binding.option3 -> binding.option3.setBackgroundResource(R.drawable.selected_options_shape)
            binding.option4 -> binding.option4.setBackgroundResource(R.drawable.selected_options_shape)
        }
        isEnabledButton(true)
    }

    private fun countPoints(answerText: TextView) {
        if (answerText.text == correctAnswer)
            point++
        Log.i(TAG, "points: $point")

    }

    private fun getNextQuestion() {
        binding.questionBox.setOnClickListener {
            isEnabledButton(true)
        }
        binding.submit.setOnClickListener {
            makeRequestByUsingOkhttpToGetQuestion()
            getDefaultStyle()
        }
    }

    private fun getDefaultStyle() {
        binding.apply {
            option1.setBackgroundResource(R.drawable.options_shape)
            option2.setBackgroundResource(R.drawable.options_shape)
            option3.setBackgroundResource(R.drawable.options_shape)
            option4.setBackgroundResource(R.drawable.options_shape)

        }

    }

}
package com.amnah.trivia.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.amnah.trivia.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    lateinit var binding: FragmentResultBinding

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.result.text = "${args.result}/10"

        if (args.result >= 5)
            binding.apply {
                term.text = "Congratulation, you WIN \uD83C\uDF89✌"
                winPic.visibility = View.VISIBLE
            }

        else
            binding.apply {
                term.text = "Unfortunately, you Loss 😔👎"
                lossPic.visibility = View.VISIBLE
            }

//        binding.playAgain.setOnClickListener {
//            Navigation.findNavController(it).popBackStack()
//        }
    }
}
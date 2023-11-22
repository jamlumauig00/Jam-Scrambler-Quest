package jam.jam.jamscramblerquest

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jam.jam.jamscramblerquest.databinding.FragmentSecondBinding
import java.lang.Integer.min
import java.util.Locale

class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 0
    private val countdownInterval: Long = 1000 // 1 second interval

    private var wordArray: Array<String> = emptyArray()
    private var currentWordIndex: Int = 0
    private var presCounter = 0
    private var keys: Array<String> = emptyArray()
    private var textAnswer: String? = null
    private var lastClickedKey: String? = null
    private val allTextViews = mutableListOf<TextView>()
    private var wordGuessed = false
    private var done = false
    private var score: Int = 0

    private val wordCategories = mapOf(
        "Foods" to arrayOf("Banana", "Pasta", "Tomato", "Cheese", "Carrot", "Cereal", "Salad", "Donut", "Sushi", "Apple"),
        "Vehicles" to arrayOf("Car", "Bicycle", "Bus", "Scooter", "Truck", "Boat", "Train", "Plane", "Taxi", "Van"),
        "Animals" to arrayOf("Lion", "Elephant", "Giraffe", "Monkey", "Tiger", "Kangaroo", "Zebra", "Penguin", "Dog", "Cat"),
        "Sports" to arrayOf("Soccer", "Baseball", "Tennis", "Cycling", "Golf", "Swimming", "Running", "Cricket", "Hockey", "Yoga"),
        "Clothes" to arrayOf("Shirt", "Pants", "Dress", "Sweater", "Jeans", "Hat", "Jacket", "Socks", "Shoes", "Scarf"),
        "Body parts" to arrayOf("Head", "Shoulder", "Knee", "Elbow", "Wrist", "Ankle", "Finger", "Toe", "Ear", "Eye")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        startScrabble()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun startScrabble() {
        binding.apply {
            scrabble.visibility = View.VISIBLE
            scoreeee.visibility = View.GONE
        }
        val category = requireArguments().getString("Items")
        if (category in wordCategories.keys) {
            binding.textQuestion.text = "Guess the $category word!"
            wordArray = wordCategories[category] ?: emptyArray()
            wordArray.shuffle()
            keys = wordArray[currentWordIndex]
                .toCharArray()
                .map { it.toString().toUpperCase(Locale.ROOT) }
                .toTypedArray()
            keys = shuffleArray(keys)
            updateKeys()
            startTimer()
        }
    }

    private fun updateKeys() {
        keys = wordArray[currentWordIndex].toCharArray()
            .map { it.toString().toUpperCase(Locale.ROOT) }
            .toTypedArray()
        keys = shuffleArray(keys)

        val linearLayout: GridLayout = binding.keysGridLayout
        linearLayout.removeAllViews()

        val editText: EditText = binding.editText
        val minSize = min(keys.size, wordArray[currentWordIndex].length)

        for (i in 0 until minSize) {
            val marginEnd = resources.getDimensionPixelSize(R.dimen.column_margin)
            addView(linearLayout, keys[i], editText, marginEnd)
        }
    }


    private fun shuffleArray(ar: Array<String>): Array<String> {
        return ar.toList().shuffled().toTypedArray()
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun addView(viewParent: GridLayout, text: String, editText: EditText, marginEnd: Int) {

        val textView = TextView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(marginEnd)
            }
            background = resources.getDrawable(R.drawable.bgpink, null)
            setTextColor(resources.getColor(R.color.colorPurple, null))
            gravity = Gravity.CENTER
            this.text = text
            isClickable = true
            isFocusable = true
            textSize = 32f
            typeface =
                Typeface.createFromAsset(requireContext().assets, "fonts/FredokaOneRegular.ttf")
            this.setOnClickListener {
                lastClickedKey = text
                if (!isEnabled) return@setOnClickListener

                if (presCounter == 0) editText.setText("")
                editText.setText(editText.text.toString() + text)
                animate().alpha(0f).duration = 300

                presCounter++
                if (presCounter == keys.size) doValidate()

                isEnabled = false

                binding.editText.setOnClickListener {
                    undoLastClick(binding.editText, this)
                    isEnabled = true
                }
            }
        }
        viewParent.addView(textView)
    }

    private fun undoLastClick(editText: EditText, textView: TextView) {
        if (presCounter > 0) {
            presCounter--
            if (lastClickedKey != null) {
                editText.setText(editText.text?.substring(0, editText.text.length - 1))
                lastClickedKey = null

                textView.animate()?.alpha(1f)?.setDuration(300)?.setListener(null)
            }
        }
    }

    private fun doValidate() {
        presCounter = 0
        val editText: EditText = binding.editText
        val linearLayout: GridLayout = binding.keysGridLayout

        if (currentWordIndex != wordArray.size) {
            textAnswer = wordArray[currentWordIndex].toUpperCase(Locale.ROOT)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())

            val isCorrect = editText.text.toString() == textAnswer
            Log.d("editText", "editText: ${editText.text}")
            Log.d("textAnswer", "textAnswer: ${textAnswer}")

            wordGuessed = isCorrect
            Log.d("isCorrect", "isCorrect: $isCorrect")

            alertDialogBuilder.setMessage(if (isCorrect) "$textAnswer Correct! " else "Wrong! The Answer is $textAnswer")

            if (isCorrect) {
                score++
                Log.d("score", "score: $score")
            }

            currentWordIndex++ // Increment here regardless of correctness

            alertDialogBuilder.setPositiveButton("Next") { _, _ ->
                editText.setText("")
                wordGuessed = false
                if (currentWordIndex != wordArray.size) {
                    updateKeys()
                }else{
                    showScore("allDone")
                }

            }

            alertDialogBuilder.setCancelable(false)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            keys = shuffleArray(keys)
            linearLayout.removeAllViews()
            for (key in keys) {
                val marginEnd = resources.getDimensionPixelSize(R.dimen.column_margin)
                addView(linearLayout, key, editText, marginEnd)
            }

        } else {
            done = true
            Log.d("score1", "score: $score")
        }


    }

    private fun startTimer() {
        timeLeftInMillis = 50000
        val progressBar = binding.simpleProgressBar
        progressBar.max = timeLeftInMillis.toInt()
        progressBar.progress = timeLeftInMillis.toInt()

        countDownTimer = object : CountDownTimer(timeLeftInMillis, countdownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                progressBar.progress = timeLeftInMillis.toInt()
            }

            override fun onFinish() {
                onTimerFinish()
            }
        }.start()
    }

    private fun onTimerFinish() {
        if (!wordGuessed && !done) {
            showScore("timesUp")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showScore(what: String) {
        binding.scrabble.visibility = View.GONE
        binding.scoreeee.visibility = View.VISIBLE
        binding.scoretv.text = if (what == "timesUp") {
            getString(R.string.times_up, score, 10)
        } else {
            getString(R.string.quiz_completed, score, 10)
        }
        if (what == "timesUp") {
            binding.icon.setImageDrawable(resources.getDrawable(R.drawable.pusa3))
        } else {
            binding.icon.setImageDrawable(resources.getDrawable(R.drawable.pusa))
        }

        binding.submitButton.text = getString(R.string.play_again)
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}

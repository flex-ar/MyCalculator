package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mycalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.Stack

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var expression: Expression

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(this.layoutInflater)
    setContentView(binding.root)
  }

  fun addCharClick(view: View) {
    val text = binding.expressionText.text.toString()
    if (text == "0") binding.expressionText.text = ""
    binding.expressionText.append((view as Button).text)

    val result = eval(binding.expressionText.text.toString())
    if (result != null) binding.resultText.text = result
  }

  fun operatorClick(view: View) {
    val text = binding.expressionText.text
    val buttonText = (view as Button).text
    val operatorList = listOf('/', '*', '-', '+')

    if (text.last().toString() == buttonText) return
    if (operatorList.contains(text.last())) binding.expressionText.text = text.dropLast(1)

    binding.expressionText.append(buttonText)
  }

  fun backspaceClick(view: View) {
    val text = binding.expressionText.text
    if (text.length > 1) binding.expressionText.text = text.dropLast(1)
    if (text.length == 1) binding.expressionText.text = "0"
  }

  fun allClearClick(view: View) {
    binding.expressionText.text = "0"
    binding.resultText.text = ""
  }

  fun equalClick(view: View) {
    val text = binding.expressionText.text.toString()
    val especialChars = listOf('/', '*', '-', '+', '(')
    if (especialChars.contains(text.last())) return

    val result = eval(text)
    if (result == null) binding.resultText.text = "Error"
    else {
      binding.expressionText.text = result
      binding.resultText.text = ""
    }
  }

  fun dotButtonClick(view: View) {
    val text = binding.expressionText.text
    if (!text.last().isDigit()) return
    val lastNumber = text.split(Regex("[-+/*)(]+")).last()
    if (!lastNumber.contains(".")) binding.expressionText.append(".")
  }

  private fun eval(text: String): String? {
    if (!checkBrackets(text)) return null

    return try {
      expression = ExpressionBuilder(text).build()
      expression.evaluate().toString()
    } catch (error: Exception) {
      Log.e("Error in eval function", error.toString())
      null
    }
  }

  private fun checkBrackets(text: String): Boolean {
    val stack = Stack<Char>()

    for (char in text) {
      when (char) {
        '(' -> stack.push(char)
        ')' -> {
          if (stack.empty()) return false
          if (stack.pop() != '(') return false
        }
      }
    }

    return stack.empty()
  }

}

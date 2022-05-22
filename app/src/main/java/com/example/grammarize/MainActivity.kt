package com.example.grammarize

import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grammarize.R.id
import com.example.grammarize.R.layout.activity_main
import com.example.grammarize.R.string.gpt3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.URLProtocol.Companion.HTTPS
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
	private var input: EditText = findViewById(id.input)
	private var output: TextView = findViewById(id.output)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(activity_main)
		val requestViewModel = RequestViewModel()
		input.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

			override fun afterTextChanged(p0: Editable?) =
				requestViewModel.sendRequest(input.text.toString(), getString(gpt3), output)
		})
		output.setOnClickListener {
			(getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
				newPlainText(
					"Corrected Text",
					output.text
				)
			)
			makeText(this, "Correction copied to clipboard!", LENGTH_SHORT).show()
		}
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		savedInstanceState.putString("input", input.text.toString())
		savedInstanceState.putString("output", output.text.toString())
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		input.setText(outState.getString("input"))
		output.text = outState.getString("output")
	}
}

class RequestViewModel : ViewModel() {
	fun sendRequest(query: String, gpt3APIKey: String, output: TextView) {
		viewModelScope.launch(IO) {
			output.text = (HttpClient(Android) {
				defaultRequest {
					url {
						protocol = HTTPS
						host = "api.openai.com"
						path("v1/engines/text-davinci-002/completions")
					}
				}
				install(Auth) { basic { credentials { BasicAuthCredentials("", gpt3APIKey) } } }
			}.post("") {
				contentType(Json)
				setBody(
					JSONObject().put("prompt", "Correct this to standard English:\n$query")
						.put("temperature", 0).put("max_tokens", 2000).put("top_p", 1.0)
						.put("frequency_penalty", 0.0).put("presence_penalty", 0.0).toString()
				)
			}.body() as JSONObject).getJSONArray("choices").getJSONObject(0).getString("text")
		}
	}
}

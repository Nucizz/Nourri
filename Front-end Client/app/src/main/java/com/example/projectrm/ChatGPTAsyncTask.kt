import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ChatGPTAsyncTask(private val apiKey: String, private val callback: (String?) -> Unit) :
    AsyncTask<String, Void, String?>() {

    override fun doInBackground(vararg params: String?): String? {
        try {
            val prompt = params[0]
            val url = "https://api.openai.com/v1/engines/davinci-codex/completions"
            return makeHttpRequest(url, apiKey, prompt)
        } catch (e: Exception) {
            // Handle exceptions and return null or an error message
            e.printStackTrace()
            return "Error: ${e.message}"
        }
    }

    override fun onPostExecute(result: String?) {
        // Handle the result on the main thread
        // This method is executed after the background task is complete
        callback(result)
    }

    private fun makeHttpRequest(urlString: String, apiKey: String, prompt: String?): String? {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        return try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.doOutput = true

            val postDataBytes = "{\"prompt\": \"$prompt\", \"max_tokens\": 150}".toByteArray()
            connection.outputStream.use { os -> os.write(postDataBytes) }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }

            response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        finally {
            connection?.disconnect()
        }
    }

}

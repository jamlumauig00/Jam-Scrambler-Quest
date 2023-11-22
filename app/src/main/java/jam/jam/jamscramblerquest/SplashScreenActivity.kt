package jam.jam.jamscramblerquest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.WindowManager
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var webView: WebView? = null
    var ExitTime: Long = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java).apply {
                // If you need to pass data to the next activity, you can do it here
            })
            finish()
        }, 1500L)

        /*  webView = findViewById(R.id.splash)

          val webSettings = webView?.settings
          webSettings?.javaScriptEnabled = true
          webView?.addJavascriptInterface(object : Any() {
              @JavascriptInterface
              fun onDataReceived(data: String) {
                  // Handle the received data from JavaScript
                  Log.d("WebViewData", "Received data: $data")

                  try {
                      if (data == "false") {
                          // Do something for success
                          val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                          this@SplashScreenActivity.startActivity(intent)
                          this@SplashScreenActivity.finish()
                      }else{
                          runOnUiThread {
                              // UI-related operations, e.g., setVisibility
                              webView?.visibility = View.VISIBLE
                              webView?.loadUrl(data)
                          }
                      }
                  } catch (e: JSONException) {
                      Log.e("JSON Parse Error", e.message ?: "Unknown error")
                  }
              }
          }, "Android")

          webView?.webViewClient = object : WebViewClient() {
              override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                  // Do something when the page starts loading
              }

              override fun onPageFinished(view: WebView?, url: String?) {
                  // Do something when the page finishes loading
              }

              override fun onReceivedError(
                  view: WebView?,
                  request: WebResourceRequest?,
                  error: WebResourceError?
              ) {
                  // Handle errors during loading
              }
          }


          // Load the URL after setting up WebViewClient and other configurations
          webView?.loadUrl("https://jamlumauig00.github.io//Scrambler")*/
    }

    override fun onKeyDown(quizKeyCode: Int, quizEvent: KeyEvent): Boolean {
        if (quizKeyCode == KeyEvent.KEYCODE_BACK && quizEvent.action == KeyEvent.ACTION_DOWN) {
            if (webView!!.canGoBack()) {
                webView?.goBack()
            } else {
                try {
                    if (System.currentTimeMillis() - ExitTime > 2000) {
                        Toast.makeText(
                            applicationContext,
                            "Press back again to Exit",
                            Toast.LENGTH_SHORT
                        ).show()
                        ExitTime = System.currentTimeMillis()
                    } else {
                        finishAffinity()
                    }
                } catch (_: Exception) {
                }
            }
            return true
        }
        return super.onKeyDown(quizKeyCode, quizEvent)
    }

}

package metri.amit.sampleapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by amitmetri on 28,April,2021
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        * Navigate to MainActivity post 1 second delay
        * */
        Handler(Looper.getMainLooper())
                .postDelayed({ startActivity(Intent(this@SplashActivity,
                        MainActivity::class.java))
                             finish()}, 1000)
    }
}
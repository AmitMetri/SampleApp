package metri.amit.sampleapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import metri.amit.sampleapp.R

/**
 * Created by amitmetri on 28,April,2021
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        /*
        * Bind ToolBar with JetPack's navigation component
        * */
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            setSupportActionBar(toolbar)
            val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        }
    }
}
package metri.amit.sampleapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import metri.amit.sampleapp.R;

import android.os.Bundle;

/*
* MainActivity hosts JetPack's navigation component.
* This app is designed for one activity and multiple fragments.
* ItemListFragment is the default fragment to be launched in MainActivity.
* ItemListFragment lists the countries.
* ItemDetailsFragment is navigated if any country is selected from this list.
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        /*
        * Bind ToolBar with JetPack's navigation component
        * */
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            setSupportActionBar(toolbar);
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        }
    }
}
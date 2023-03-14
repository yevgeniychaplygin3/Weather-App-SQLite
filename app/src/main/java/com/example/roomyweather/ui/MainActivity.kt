package com.example.roomyweather.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomyweather.BuildConfig
import com.example.roomyweather.R
import com.example.roomyweather.data.ForecastPeriod
import com.example.roomyweather.data.ForecastRoomEntity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar


const val OPENWEATHER_APPID = BuildConfig.OPENWEATHER_API_KEY

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfig: AppBarConfiguration
    private val searchCitiesViewModel: SearchCitiesViewModel by viewModels()
    private val viewModel: FiveDayForecastViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)

        findViewById<NavigationView>(R.id.nav_view)?.setupWithNavController(navController)

        setupActionBarWithNavController(navController, appBarConfig)

        searchCitiesViewModel.searchedCities.observe(this) {
                cities ->
            val menu: Menu? = findViewById<NavigationView>(R.id.nav_view)?.menu
            val submenu = menu?.getItem(0)?.subMenu
            submenu?.clear()

            for (city in cities.sortedByDescending { it.timeStamp }) {
                submenu?.add(city.city)
            }
        }

//        viewModel.forecast.observe(this) { forecast ->
//            if (forecast != null) {
//                supportActionBar?.title = forecast.city.name
//            }
//        }
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val city = sharedPrefs.getString(getString(R.string.pref_city_key), null)
        supportActionBar?.title = city
    }

    override fun onResume() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val city = sharedPrefs.getString(getString(R.string.pref_city_key), null)
        supportActionBar?.title = city
        super.onResume()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("MainActivity", "option item : $item")
        val nav = findViewById<NavigationView>(R.id.nav_view)
        nav?.setNavigationItemSelectedListener { item ->
            Log.d("MainActivity", "item: $item")

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            drawerLayout.closeDrawer(nav)
            when (item.toString()) {
                "Settings" -> {

                    val directions = ForecastListFragmentDirections.navigateToSettings()
                    findNavController(R.id.nav_host_fragment).navigate(directions)
                    true
                }
                else -> {
                    Log.d("MainActivity", "else: $item")


                    val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
                    val units = sharedPrefs.getString(getString(R.string.pref_units_key), null)

                    viewModel.loadFiveDayForecast(item.toString(), units, OPENWEATHER_APPID)

                    val savedCity = ForecastRoomEntity(item.toString(), System.currentTimeMillis())
                    searchCitiesViewModel.addSearchedCity(savedCity)

                    val editor = sharedPrefs.edit()
                    editor.putString(getString(R.string.pref_city_key), item.toString())
                    editor.commit()
                    findNavController(R.id.nav_host_fragment).navigate(R.id.forecast_list)
                    supportActionBar?.title = item.toString()
                    true
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}
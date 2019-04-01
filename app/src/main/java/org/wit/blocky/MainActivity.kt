package org.wit.blocky

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.wit.blocky.main.MainApp


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        app = application as MainApp

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.destination_home),
            drawer_layout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, _, _ ->
            if (drawer_layout.isDrawerOpen(GravityCompat.START))
                drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        drawer_layout.closeDrawers()

        val id = menuItem.itemId
        when (id) {
//            R.id.first -> navController.navigate(R.id.firstFragment)
//            R.id.second -> navController.navigate(R.id.secondFragment)
//            R.id.third -> navController.navigate(R.id.thirdFragment)
        }
        return true

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawer_layout)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}
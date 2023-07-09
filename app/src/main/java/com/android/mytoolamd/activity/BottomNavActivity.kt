package com.android.mytoolamd.activity

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.mytoolamd.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {

    private lateinit var navhostFragmentContainer: NavHostFragment
    lateinit var navigationbottom: BottomNavigationView
    lateinit var navController: NavController
    lateinit var gradientDrawable: GradientDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        navigationbottom = findViewById(R.id.bottom_navg)

        setUpWithBottomNavigation()



        gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR, intArrayOf(
                0XFFFEDA77.toInt(),
                0XFFF58529.toInt(),
                0XFFDD2A7B.toInt(),
                0XFF8134AF.toInt(),
                0XFF515BD4.toInt()

            )
        )

        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        navigationbottom.background = gradientDrawable

    }

    private fun setUpWithBottomNavigation() {
        navhostFragmentContainer =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navhostFragmentContainer.navController
        navigationbottom = findViewById(R.id.bottom_navg)
        navigationbottom.setupWithNavController(navController)
        val BVMenu = navigationbottom.getMenu()
        BVMenu.findItem(R.id.bottomNavProfileFragment).setIcon(R.drawable.ic_user_retina)

    }

}
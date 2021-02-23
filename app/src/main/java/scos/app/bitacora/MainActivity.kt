package scos.app.bitacora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.toggleImg -> {
                    openDrawer()
                }
                R.id.fab -> {
                    makeToast("Fab Clicked", true)
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                makeToast("Item 1", true)
            }
            R.id.item2 -> {
                makeToast("Item 2", true)
            }
            R.id.item3 -> {
                makeToast("Item 3", true)
            }
        }
        return true
    }

    private fun openDrawer(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    private fun makeToast(mensaje: String, short: Boolean) {
        if (short) {
            Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, mensaje, Toast.LENGTH_LONG).show()
        }
    }

    private fun initComponents() {
        val toggleImg = findViewById<ImageView>(R.id.toggleImg)
        val sideMenu = findViewById<NavigationView>(R.id.sideMenu)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        drawer = findViewById(R.id.drawer)

        sideMenu.bringToFront() //Esta línea hace que el SideMenu sea clickeable
        toggleImg.setOnClickListener(this)
        sideMenu.setNavigationItemSelectedListener(this)
        fab.setOnClickListener(this)
    }
}
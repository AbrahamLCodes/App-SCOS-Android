package scos.app.bitacora.mainactivities

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import scos.app.bitacora.R
import java.text.SimpleDateFormat
import java.util.*

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout

    //Date and Time for SideMenu Header variables
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var sdfWatchTime: SimpleDateFormat
    private lateinit var sdfWatchDate: SimpleDateFormat
    private lateinit var hh: TextView
    private lateinit var hd: TextView
    private lateinit var sideMenu: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()

        sideMenu = findViewById(R.id.sideMenu)
        val headerView = sideMenu.getHeaderView(0)
        hd = headerView.findViewById(R.id.headerDate)
        sdfWatchTime = SimpleDateFormat("hh:mm aa")
        sdfWatchDate = SimpleDateFormat("dd/MM/yyyy")
        sideMenu.bringToFront() //Esta línea hace que el SideMenu sea clickeable
        sideMenu.setNavigationItemSelectedListener(this)

        hd.text = sdfWatchDate.format(Date())
    }

    override fun onResume() {
        super.onResume()
        val fecha = findViewById<TextView>(R.id.fecha)
        fecha.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                startActivity(Intent(this, InfoActivity::class.java))
            }
        }
        return true
    }


    private fun openDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        val bar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val btnfalla = findViewById<MaterialButton>(R.id.btnReporteFalla)
        val btnSolucion = findViewById<MaterialButton>(R.id.btnReporteSolucion)
        btnSolucion.setOnClickListener {
            startActivity(Intent(this, ReporteActivity::class.java).apply {
                putExtra("isfalla", false)
            })
        }
        btnfalla.setOnClickListener {
            startActivity(Intent(this, ReporteActivity::class.java).apply {
                putExtra("isfalla", true)
            })
        }
        drawer = findViewById(R.id.drawer)
        bar.setNavigationOnClickListener {
            openDrawer()
        }
    }
}
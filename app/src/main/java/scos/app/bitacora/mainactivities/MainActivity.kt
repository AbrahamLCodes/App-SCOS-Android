package scos.app.bitacora.mainactivities

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

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

        setupPermissions()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
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
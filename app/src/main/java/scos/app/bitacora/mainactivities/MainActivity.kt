package scos.app.bitacora.mainactivities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import scos.app.bitacora.R
import scos.app.bitacora.forms.FormSolucionActivity
import scos.app.bitacora.forms.ReporteActivity
import scos.app.bitacora.modelos.Falla
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

    companion object {
        lateinit var recycler: RecyclerView
        lateinit var fallasList: MutableList<Falla>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sideMenu = findViewById(R.id.sideMenu)
        val headerView = sideMenu.getHeaderView(0)
        hh = headerView.findViewById(R.id.headerHour)
        hd = headerView.findViewById(R.id.headerDate)
        sdfWatchTime = SimpleDateFormat("hh:mm aa")
        sdfWatchDate = SimpleDateFormat("dd/MM/yyyy")
        sideMenu.bringToFront() //Esta línea hace que el SideMenu sea clickeable
        sideMenu.setNavigationItemSelectedListener(this)

        initComponents()
    }

    override fun onResume() {
        super.onResume()
        val fecha = findViewById<TextView>(R.id.fecha)
        fecha.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    }

    override fun onStart() {
        super.onStart()
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent!!.action!!.compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    hh.text = sdfWatchTime.format(Date())
                    hd.text = sdfWatchDate.format(Date())
                }
            }

        }
        registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                makeToast("Item 1", true)
            }
            R.id.item2 -> {
                makeToast("Item 2", true)
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
        val btnSolucion = findViewById(R.id.btnReporteSolucion) as MaterialButton
        btnSolucion.setOnClickListener {
            startActivity(Intent(this, FormSolucionActivity::class.java).apply {
                putExtra("insert", true)
            })
        }
        btnfalla.setOnClickListener {
            startActivity(Intent(this, ReporteActivity::class.java).apply {
                putExtra("insert", true)
            })
            Log.d("click","hey")
        }
        drawer = findViewById(R.id.drawer)
        bar.setNavigationOnClickListener {
            openDrawer()
        }
        fallasList = ArrayList()
        //setRecyclerData()
    }
}
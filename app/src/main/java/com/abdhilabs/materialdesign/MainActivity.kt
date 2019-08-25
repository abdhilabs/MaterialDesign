package com.abdhilabs.materialdesign

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.abdhilabs.materialdesign.adapter.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_places.view.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {

    lateinit private var menu: Menu
    private var isListView: Boolean = false
    //Declare Layout Manager
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    //Declare Adapter
    lateinit private var adapterList: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set Layout Manager
        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        list.layoutManager = staggeredLayoutManager

        //Set Adapter
        adapterList = ListAdapter(this)
        list.adapter = adapterList
        adapterList.setOnItemClickListener(onItemClickListener)

        setUpActionBar()

        isListView = true
    }

    //Buat button Click
    private val onItemClickListener = object : ListAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            //1
            val transitionIntent = DetailActivity.newIntent(this@MainActivity, position)
            val placeImage = view.findViewById<ImageView>(R.id.placeImage)
            val placeNameHolder = view.findViewById<LinearLayout>(R.id.placeNameHolder)

            //2
            val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
            val statusBar = findViewById<View>(android.R.id.statusBarBackground)
            val imagePair = Pair.create(placeImage as View, "tImage")
            val holderPair = Pair.create(placeNameHolder as View, "tNameHolder")

            //3
            val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
            val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
            val toolbarPair = Pair.create(toolbar as View, "tActionBar")

            //4
            val pairs = mutableListOf(imagePair, holderPair, statusPair, toolbarPair)
            if (navigationBar != null && navPair != null) {
                pairs += navPair
            }

            //5
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, *pairs.toTypedArray())
            ActivityCompat.startActivity(this@MainActivity, transitionIntent, options.toBundle())
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.elevation = 7.0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_toggle) {
            toggle()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggle() {
        if (isListView) {
            staggeredLayoutManager.spanCount = 2
            showGridView()
        } else {
            staggeredLayoutManager.spanCount = 1
            showListView()
        }
    }

    private fun showListView() {
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_grid)
        item.title = getString(R.string.show_as_grid)
        isListView = true
    }

    private fun showGridView() {
        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_list)
        item.title = getString(R.string.show_as_list)
        isListView = false
    }
}

package com.revosleap.samplemusicplayer.ui.blueprints

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.revosleap.samplemusicplayer.R
import com.revosleap.samplemusicplayer.models.Song
import com.revosleap.samplemusicplayer.utils.RecyclerAdapter
import com.revosleap.samplemusicplayer.utils.SongProvider
import kotlinx.android.synthetic.main.activity_main.*

abstract class MainActivityBluePrint : AppCompatActivity(), ActionMode.Callback, RecyclerAdapter.OnLongClick,
        RecyclerAdapter.SongsSelected,RecyclerAdapter.SongClicked {
    private var actionMode: ActionMode? = null
    private var songAdapter: RecyclerAdapter? = null
    private var deviceMusic = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songAdapter = RecyclerAdapter()
        setViews()
    }

    override fun onSongLongClicked(position: Int) {
        if (actionMode == null) {
            actionMode = startActionMode(this)
        }
    }

    override fun onSelectSongs(selectedSongs: MutableList<Song>) {
        if (selectedSongs.isEmpty()) {
            actionMode?.finish()
            songAdapter?.removeSelection()
        } else {
            val title = "Selected Songs ${selectedSongs.size}"
            actionMode?.title = title
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.action_mode_menu, menu!!)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_delete -> {
                val songs = songAdapter?.getSelectedSongs()
                Toast.makeText(this, "Deleted ${songs?.size} Songs", Toast.LENGTH_SHORT).show()
                mode?.finish()
                return true
            }

        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
    }

    private fun setViews() {
        deviceMusic.addAll(SongProvider.getAllDeviceSongs(this))
        songAdapter?.setOnLongClick(this)
        songAdapter?.setSongsSelected(this)
        songAdapter?.setOnSongClicked(this)
        recyclerView?.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@MainActivityBluePrint)
            hasFixedSize()
        }
        songAdapter?.addSongs(deviceMusic)
    }

}
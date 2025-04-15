package hu.oxygenmusic.tv

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playerView = findViewById<PlayerView>(R.id.player_view)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        playerView.keepScreenOn = true

        val mediaItem = MediaItem.fromUri("https://oxygenmusic.hu:2443/hls/oxygenmusic_1200.m3u8")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        val closeButton = findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener {
            finish()
        }

        val btnQuality = findViewById<Button>(R.id.btn_quality)
        btnQuality.text = "SD" // Kezdeti érték
        
        btnQuality.setOnClickListener {
            val popup = android.widget.PopupMenu(this, btnQuality)
            // Apply custom style
            val popupStyle = androidx.appcompat.R.style.Theme_AppCompat
            val wrapper = android.view.ContextThemeWrapper(this, popupStyle)
            val styledPopup = android.widget.PopupMenu(wrapper, btnQuality)
            
            // Add all three quality options
            styledPopup.menu.add("SD")
            styledPopup.menu.add("HD")
            styledPopup.menu.add("UHD")
            
            styledPopup.setOnMenuItemClickListener { item ->
                val newQuality = item.title.toString()
                player.stop()
                
                val mediaItem = MediaItem.fromUri(
                    when (newQuality) {
                        "SD" -> "https://oxygenmusic.hu:2443/hls/oxygenmusic_1200.m3u8"
                        "HD" -> "https://oxygenmusic.hu:2443/hls/oxygenmusic_2200.m3u8"
                        "UHD" -> "https://oxygenmusic.hu:2443/hls/oxygenmusic_5000.m3u8"
                        else -> "https://oxygenmusic.hu:2443/hls/oxygenmusic_1200.m3u8"
                    }
                )
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true
                btnQuality.text = newQuality
                true
            }
            styledPopup.show()
        }
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}

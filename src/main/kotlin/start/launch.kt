package start

import com.nani.homeController.DownloadManager
import java.io.FileReader
import java.util.*

fun main(args: Array<String>) {
    var properties = HashMap<String, String>()
    var file = FileReader("/home/nani/file.properties")
    var lines = file.readLines();

    lines.filter { it.contains(":") }
          .forEach { properties.put(it.split(":")[0], it.split(":")[1]) }

    var downloadMgr = DownloadManager(properties)
    var playListTracks = downloadMgr.getSpotifyPlatLisTracks()

    playListTracks
            .map { downloadMgr.getYouTubeURL(it) }
            .forEach { Runtime.getRuntime().exec("/usr/bin/python /home/nani/work/test.py " + it) }

}


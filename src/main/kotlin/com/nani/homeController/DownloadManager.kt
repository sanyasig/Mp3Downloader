package com.nani.homeController

import org.apache.http.HttpResponse
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*

enum class Type {
    PUT, GET, POST, DELETE
}
class DownloadManager(var propesrties: HashMap<String, String>) {

    fun getSpotifyPlatLisTracks(): List<Track> {

        var post = HttpPost("https://accounts.spotify.com/api/token")

        var postParameters = ArrayList<BasicNameValuePair>()
        postParameters.add(BasicNameValuePair("grant_type", "refresh_token"))
        postParameters.add(BasicNameValuePair("refresh_token", propesrties.get("sportify_refresh_ token")))

        post.setEntity(UrlEncodedFormEntity(postParameters))
        post.addHeader("Authorization", "Basic "+propesrties.get("spotify_authorisation_token"))
        val httpClient = DefaultHttpClient()
        val response = httpClient.execute(post)
        val entity = response.entity
        val responseString = EntityUtils.toString(entity, "UTF-8")
        var json = JSONObject(responseString)
        var accessToken = json.get("access_token")

        val httpclient = DefaultHttpClient()
        val httpGet = HttpGet("https://api.spotify.com/v1/users/1151870304/playlists/6zNRmK33NKnIKWwsnkcqeW/tracks?limit=1&offset=0")
        httpGet.setHeader("Authorization", "Bearer " + accessToken)
        val response1 = httpclient.execute(httpGet)
        validateResponse(response1, "Retreive PlayList")
        var entity2 = response1.entity

        val playlistJson = EntityUtils.toString(entity2, "UTF-8")
        return getPlayList(playlistJson)
    }

    private fun getPlayList(playlistJson: String?) : List<Track> {
        val songJsonArray = JSONObject(playlistJson).getJSONArray("items")
        var artistNames = ""
        var name : String = ""
        var trasks = ArrayList<Track>()
        for ( i in 0..songJsonArray.length()-1) {
            var track  = songJsonArray.getJSONObject(i).getJSONObject("track")

            name = track.getString("name");
            var artists = track.getJSONArray("artists")
            artistNames = ""
            for ( j in 0..(artists.length() - 1)) {
                artistNames = artistNames + " " + artists.getJSONObject(j).getString("name")
            }
            trasks.add(Track(name, artistNames, track.getString("id")))

          }
        return trasks;
    }

    fun getYouTubeURL(track :Track) : String {
        var youtubeURL : String = "https://www.youtube.com/watch?v="
        var url = "https://www.googleapis.com/youtube/v3/search?maxResults=5&part=snippet&key="+ propesrties.get("googel_api_key")+"&"
        var searchString = URLEncoder.encode(track.name + " " +track.artist)
        url += "q=" + searchString
        val httpclient = DefaultHttpClient()
        val httpGet = HttpGet(url)
        val response = httpclient.execute(httpGet)
        validateResponse(response, "Retreive Track URL")
        var entity2 = response.entity
        var responseJson = JSONObject(EntityUtils.toString(entity2, "UTF-8"))
        var videoID = responseJson.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId")
        return youtubeURL + videoID
    }

    fun validateResponse(response : HttpResponse, stage : String) {
        if(response.statusLine.statusCode != 200){
            println("ERROR OCCURED DURING STAGE  -- " + stage)
            var entity2 = response.entity
            println(EntityUtils.toString(entity2, "UTF-8"))
            System.exit(0)
        }
    }

}

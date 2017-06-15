package com.nani.homeController

import org.apache.http.HttpRequest
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import java.util.*

class HttpRequestManager(val url: String, val headers : HashMap<String, String>, val type : Type) {

    fun testpost() {
        Runtime.getRuntime().exec("/usr/bin/python /home/nani/work/test.py https://www.youtube.com/watch?v=HusZbDM2fkk")
       var post = HttpPost("https://accounts.spotify.com/api/token")

        var postParameters = ArrayList<BasicNameValuePair>()
        postParameters.add(BasicNameValuePair("grant_type", "refresh_token"))
        postParameters.add(BasicNameValuePair("refresh_token", "AQASVh7W0iCiOgisihaviALNOWQf2_vcA9nAswEH5IlOxqciHSZQWXZ_UDKrdIefqkiRTkz3ZKzyzBgbk0WVJo1LcH3svbxlA752s1B0okevxMq0R69RL9fQdLDTVg1B9lY"))

        post.setEntity(UrlEncodedFormEntity(postParameters))
        post.addHeader("Authorization", "Basic YmE3NDEyNjg0NzE5NGE2NWFmZmNhMzk1Nzk1MzI0YTA6ZjE4ZTY5ZTNmMGU2NDYxN2E3M2QxNmM4ZjcwNDU4ZmY=")
        val httpClient =  DefaultHttpClient()

        val response = httpClient.execute(post)
        val entity = response.entity
        val responseString = EntityUtils.toString(entity, "UTF-8")
        println(responseString)
        var json = JSONObject(responseString)

        var accessToken  = json.get("access_token")


        val httpclient = DefaultHttpClient()
        val httpGet = HttpGet("https://api.spotify.com/v1/users/1151870304/playlists/6zNRmK33NKnIKWwsnkcqeW/tracks?limit=50&offset=50")
        httpGet.setHeader("Authorization" , "Bearer " + accessToken)
        val response1 = httpclient.execute(httpGet)
        println(response1.statusLine)
        var entity2 = response1.entity
        val responseString2 = EntityUtils.toString(entity2, "UTF-8")


        println(responseString2)

    }



    fun sendRequest(httpRequest : HttpRequest) {
        var request = HttpPut(url)
        when (type) {
            //Type.GET -> request = HttpGet(url)
            Type.PUT -> request = HttpPut(url)
            else -> request = HttpPut(url)
        }
        for (value in headers) {
            request.setHeader(value.key, value.value)
        }

        val httpClient =  DefaultHttpClient()
        print("semnfing request")
        val response = httpClient.execute(request)
        val entity = response.entity
        val responseString = EntityUtils.toString(entity, "UTF-8")
        println(responseString)
 
    }
}


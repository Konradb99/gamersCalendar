package com.konradb.gameCalendar.main_app.Model.API

import com.konradb.gameCalendar.main_app.Model.API.AllGames
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("games?")
    fun getAllGames(@Query("key") key: String, @Query("page") page: Int, @Query("page_size") page_size: Int): Call<AllGames>

    @GET("games?")
    fun getGamesByName(@Query("key") key: String, @Query("page") page: Int, @Query("page_size") page_size: Int, @Query("search") search: String): Call<AllGames>

    @GET("{id}?")
    fun getGameDetails(@Path("id") id: Long, @Query("key") key: String): Call<GameApi>
}
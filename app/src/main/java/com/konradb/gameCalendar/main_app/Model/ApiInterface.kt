package com.konradb.gameCalendar.main_app.Model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("games?")
    fun getAllGames(@Query("key") key: String, @Query("page") page: Int, @Query("page_size") page_size: Int): Call<AllGames>

    @GET("games?")
    fun getGamesByName(@Query("key") key: String, @Query("page") page: Int, @Query("page_size") page_size: Int, @Query("search") search: String): Call<AllGames>
}
package com.konradb.gameCalendar.main_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.konradb.gameCalendar.R
import com.konradb.gameCalendar.main_app.Model.AllGames
import com.konradb.gameCalendar.main_app.Model.ApiInterface
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game
import com.konradb.gameCalendar.main_app.Model.Models.CreateEntity
import com.konradb.gameCalendar.main_app.ViewModel.Adapters.AllGamesAdapter
import com.konradb.gameCalendar.main_app.others.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [all_games_list.newInstance] factory method to
 * create an instance of this fragment.
 */
class all_games_list : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_games_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.page_1 -> {
                    view.findNavController().navigate(R.id.action_all_games_list_to_main_list_fragment)
                    true
                }
                R.id.page_2 -> {
                    //Tu jestesmy
                    true
                }
                R.id.page_3 -> {
                    view.findNavController().navigate(R.id.action_all_games_list_to_fragment_profile_details)
                    true
                }
                else -> false
            }
        }
        //Prepare livedata
        val gamesList = MutableLiveData<List<Game>>()
        val games:ArrayList<Game> = ArrayList()
        gamesList.value = games


        //Prepare layout View
        var layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var adapter =AllGamesAdapter(gamesList, requireContext())
        gamesList.observe(viewLifecycleOwner, {adapter.notifyDataSetChanged()})

        view.findViewById<RecyclerView>(R.id.allGamesRecyclerView).let{
            it.adapter = adapter
            it.layoutManager = layoutManager
        }


        //Download all games from api
        val retrofit = Retrofit.Builder()
            .baseUrl(all_games_list.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiInterface::class.java)
        val callAllGames = service.getAllGames(Constants.APIKEY, 1, 20)

        callAllGames.enqueue(object: Callback<AllGames> {
            override fun onFailure(call: Call<AllGames>, t: Throwable) {
                println("Nie udalo sie pobrac danych")
            }
            override fun onResponse(call: Call<AllGames>, response: Response<AllGames>) {
                if(response.code() === 200) {
                    games.clear()
                    val gamesListApi = response.body()
                    var newGame = Game()
                    for(game in gamesListApi!!.result){
                        newGame = CreateEntity.CreateGame(game)
                        games.add(newGame)
                    }
                    gamesList.value = games
                    println(games.size)
                }
            }
        })

        view.findViewById<TextInputEditText>(R.id.appFindByNameText).doOnTextChanged { text, start, before, count ->
            val callByName = service.getGamesByName(Constants.APIKEY, 1, 20, view.findViewById<TextInputEditText>(R.id.appFindByNameText).text.toString())
            callByName.enqueue(object: Callback<AllGames> {
                override fun onFailure(call: Call<AllGames>, t: Throwable) {
                    println("Nie udalo sie pobrac danych")
                }
                override fun onResponse(call: Call<AllGames>, response: Response<AllGames>) {
                    if(response.code() === 200) {
                        games.clear()
                        val gamesListApi = response.body()
                        var newGame = Game()
                        for(game in gamesListApi!!.result){
                            newGame = CreateEntity.CreateGame(game)
                            games.add(newGame)
                        }
                        gamesList.value = games
                        println(games.size)
                    }
                }
            })
        }






    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment all_games_list.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            all_games_list().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        var BaseUrl = "https://api.rawg.io/api/"
    }
}
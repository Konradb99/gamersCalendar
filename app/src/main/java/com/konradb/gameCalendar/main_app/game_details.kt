package com.konradb.gameCalendar.main_app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.konradb.gameCalendar.R
import com.konradb.gameCalendar.main_app.Model.API.ApiInterface
import com.konradb.gameCalendar.main_app.Model.API.GameApi
import com.konradb.gameCalendar.main_app.Model.API.AllGames
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game
import com.konradb.gameCalendar.main_app.Model.FireBase.GameData
import com.konradb.gameCalendar.main_app.Model.Models.CreateEntity
import com.konradb.gameCalendar.main_app.ViewModel.Factory.GameViewModelFactory
import com.konradb.gameCalendar.main_app.ViewModel.GameViewModel
import com.konradb.gameCalendar.main_app.others.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [game_details.newInstance] factory method to
 * create an instance of this fragment.
 */
class game_details : Fragment() {
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
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    private lateinit var viewModel: GameViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //prepare viewmodel
        val factory = GameViewModelFactory((requireNotNull(this.activity).application))
        viewModel = ViewModelProvider(requireActivity(), factory).get(GameViewModel::class.java)

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiInterface::class.java)
        val callGameDetails = service.getGameDetails(viewModel.gameId, Constants.APIKEY)

        //Starting api download
        callGameDetails.enqueue(object: Callback<GameApi> {
            override fun onFailure(call: Call<GameApi>, t: Throwable) {
                println("Nie udalo sie pobrac danych")
            }
            override fun onResponse(call: Call<GameApi>, response: Response<GameApi>) {
                if(response.code() == 200){
                    val gameApi = response.body()
                    //Set game image
                    val executor = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())
                    var image: Bitmap? = null
                    executor.execute() {
                        val imageUrl = gameApi?.background_image
                        try {
                            val img = java.net.URL(imageUrl).openStream()
                            image = BitmapFactory.decodeStream(img)

                            handler.post() {
                                viewModel.gameImage = gameApi?.background_image
                                view.findViewById<ImageView>(R.id.gameIcon).setImageBitmap(image)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    var genresStr = ""
                    //Prepare genres string
                    for(genre in gameApi?.genres!!){
                        genresStr += "${genre.name}, "
                    }
                    genresStr = genresStr.dropLast(2)

                    //Set text attributes
                    view.findViewById<TextView>(R.id.gameName).text = gameApi?.name
                    view.findViewById<TextView>(R.id.gameReleaseDate).text = "Released ${gameApi?.released}"
                    view.findViewById<TextView>(R.id.gameRating).text = gameApi?.rating
                    view.findViewById<TextView>(R.id.gameMetacritic).text = gameApi?.metacritic.toString()
                    view.findViewById<TextView>(R.id.gameGenres).text = genresStr
                }
            }
        })

        //Set Touch listeners
        view.findViewById<Button>(R.id.gameBack).setOnClickListener(){
            view.findNavController().navigate(R.id.action_game_details_to_all_games_list)
        }

        view.findViewById<Button>(R.id.gameAddToLibraryButton).setOnClickListener(){
            //Statuses: In progress, Completed, To check
            //Default status = To check

            //Prepare game data to add to database
            viewModel.gameName = view.findViewById<TextView>(R.id.gameName).text.toString()
            viewModel.gameReleaseDate = view.findViewById<TextView>(R.id.gameReleaseDate).text.toString()
            viewModel.gameRating = view.findViewById<TextView>(R.id.gameRating).text.toString()
            viewModel.gameMetacritic = view.findViewById<TextView>(R.id.gameMetacritic).text.toString()
            viewModel.gameGenres = view.findViewById<TextView>(R.id.gameGenres).text.toString()


            //Prepare reference to database
            val database = Firebase.database("https://gamerscalendar-43e36-default-rtdb.europe-west1.firebasedatabase.app/")
            val ref = database.getReference("")
            val gameId = ref.push().key

            //Prepare object to add
            var gameToAdd = GameData(gameId,
                viewModel.gameName,
                viewModel.gameReleaseDate,
                viewModel.gameRating,
                viewModel.gameMetacritic!!.toInt(),
                viewModel.gameImage,
                viewModel.gameGenres,
                "To check"
            )
            //Add to database
            ref.child(gameId!!).setValue(gameToAdd)
        }

        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.page_1 -> {
                    view.findNavController().navigate(R.id.action_game_details_to_main_list_fragment)
                    true
                }
                R.id.page_2 -> {
                    view.findNavController().navigate(R.id.action_game_details_to_all_games_list)
                    true
                }
                R.id.page_3 -> {
                    view.findNavController().navigate(R.id.action_game_details_to_fragment_profile_details)
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment game_details.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            game_details().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        var BaseUrl = "https://api.rawg.io/api/games/"
    }
}
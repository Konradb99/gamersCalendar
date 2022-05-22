package com.konradb.gameCalendar.main_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.konradb.gameCalendar.R
import com.konradb.gameCalendar.main_app.Model.FireBase.GameData
import com.konradb.gameCalendar.main_app.ViewModel.Adapters.AllGamesAdapter
import com.konradb.gameCalendar.main_app.ViewModel.Adapters.FavouriteGamesAdapter
import com.konradb.gameCalendar.main_app.ViewModel.Factory.GameViewModelFactory
import com.konradb.gameCalendar.main_app.ViewModel.GameViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [main_list_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class main_list_fragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_main_list_fragment, container, false)
    }

    private lateinit var viewModel: GameViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //prepare viewmodel
        val factory = GameViewModelFactory((requireNotNull(this.activity).application))
        viewModel = ViewModelProvider(requireActivity(), factory).get(GameViewModel::class.java)

        //Prepare livedata
        val LibraryGames = MutableLiveData<List<GameData>>()
        val LibraryList = ArrayList<GameData>()
        LibraryGames.value = LibraryList

        //Prepare Recycler View
        var layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var adapter = FavouriteGamesAdapter(viewModel, LibraryGames, requireContext())
        LibraryGames.observe(viewLifecycleOwner, {adapter.notifyDataSetChanged()})

        view.findViewById<RecyclerView>(R.id.libraryGameList).let{
            it.adapter = adapter
            it.layoutManager = layoutManager
        }


        //Prepare database
        val database = Firebase.database("https://gamerscalendar-43e36-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("")


        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    LibraryList.clear()
                    for(game in snapshot.children) {
                        val gameItem = game.getValue(GameData::class.java)
                        println(gameItem!!.status)
                        LibraryList.add(gameItem!!)
                    }
                    LibraryGames.value = LibraryList
                }
            }
        })

        //On Click listeners
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.page_1 -> {
                    //Tu jestesmy
                    true
                }
                R.id.page_2 -> {
                    view.findNavController().navigate(R.id.action_main_list_fragment_to_all_games_list)
                    true
                }
                R.id.page_3 -> {
                    view.findNavController().navigate(R.id.action_main_list_fragment_to_fragment_profile_details)
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
         * @return A new instance of fragment main_list_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            main_list_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
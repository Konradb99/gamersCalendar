package com.konradb.gameCalendar.main_app.ViewModel.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.konradb.gameCalendar.R
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game
import com.konradb.gameCalendar.main_app.Model.FireBase.GameData
import com.konradb.gameCalendar.main_app.ViewModel.GameViewModel
import java.util.concurrent.Executors

class FavouriteGamesAdapter(private val viewModel: GameViewModel, private val games: MutableLiveData<List<GameData>>, context: Context): RecyclerView.Adapter<FavouriteGamesAdapter.MyView>() {
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var gameName = view.findViewById<TextView>(R.id.gameName)
        var gameStatus = view.findViewById<TextView>(R.id.gameStatus)
        var gameIcon = view.findViewById<ImageView>(R.id.gameIcon)
        var gameId = view.findViewById<TextView>(R.id.gameId)
        var myView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.library_item,
            parent,
            false
        )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        //set photo
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute() {
            val imageUrl = games.value?.get(position)?.background_image
            try {
                val img = java.net.URL(imageUrl).openStream()
                image = BitmapFactory.decodeStream(img)

                handler.post() {
                    holder.gameIcon.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //Status colors
        // Red - To Check
        // Yellow - In progress
        // Green - Completed
        when(games.value?.get(position)?.status){
            "To check" -> holder.myView.findViewById<ImageView>(R.id.statusIcon).setImageResource(R.drawable.to_check)
            "In progress" -> holder.myView.findViewById<ImageView>(R.id.statusIcon).setImageResource(R.drawable.in_progress)
            "Completed" -> holder.myView.findViewById<ImageView>(R.id.statusIcon).setImageResource(R.drawable.completed)
        }


        //set data
        holder.gameName.text = games.value?.get(position)?.name
        holder.gameStatus.text = games.value?.get(position)?.status
        holder.gameId.text = games.value?.get(position)?.id.toString()

        //set OnClick for item
        holder.myView.setOnClickListener(){
            //save game to ViewModel
            viewModel.gameId = holder.gameId.text.toString().toLong()
            //holder.myView.findNavController().navigate(R.id.action_all_games_list_to_game_details)
        }


    }
    override fun getItemCount()= games.value?.count()?:0
}
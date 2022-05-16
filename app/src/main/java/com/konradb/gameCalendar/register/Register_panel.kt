package com.konradb.gameCalendar.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.konradb.gameCalendar.BaseFragment
import com.konradb.gameCalendar.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register_panel.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register_panel : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val LOG_DEBUG = "LOG_DEBUG"

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
        return inflater.inflate(R.layout.fragment_register_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.appRegisterBtn).setOnClickListener(){
            val email: String = view.findViewById<TextInputEditText>(R.id.appRegisterEmailText)?.getText()?.trim().toString()
            val passwd: String = view.findViewById<TextInputEditText>(R.id.appRegisterPasswdText)?.getText()?.trim().toString()
            val repeatPasswd: String = view.findViewById<TextInputEditText>(R.id.appRegisterPasswdRepeatText)?.getText()?.trim().toString()


            if(passwd==repeatPasswd){
                fbAuth.createUserWithEmailAndPassword(email, passwd).addOnSuccessListener {authRes ->
                    if(authRes.user != null) view.findNavController().navigate(R.id.action_register_panel2_to_login_panel2)
                }.addOnFailureListener{ exc ->
                    Snackbar.make(requireView(), "Something went wrong...", Snackbar.LENGTH_SHORT).show()
                    Log.d(LOG_DEBUG, exc.message.toString())
                }
            }
        }

        view.findViewById<TextView>(R.id.loginBtnToRegister).setOnClickListener(){
            view.findNavController().navigate(R.id.action_register_panel2_to_login_panel2)
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register_panel.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register_panel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
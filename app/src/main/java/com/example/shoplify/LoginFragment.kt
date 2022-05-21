package com.example.shoplify

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.shoplify.R.id.btn_register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var fAuth: FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.log_username)
        password = view.findViewById(R.id.log_password)
        fAuth = Firebase.auth

        view.findViewById<Button>(btn_register).setOnClickListener{
            val navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegisterFragment(),false)
        }

        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            validateForm()
        }
        return view
    }
    private fun firebaseSignIn(){
        btn_login.isEnabled = false
        btn_login.alpha = 0.5f
        fAuth.signInWithEmailAndPassword(username.text.toString(),
        password.text.toString()).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                val navHome = activity as FragmentNavigation
                navHome.navigateFrag(HomeFragment(),addToStack = true)
            }else{
                btn_login.isEnabled = true
                btn_login.alpha = 1.0f
                Toast.makeText(context,task.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun validateForm(){

        val icon = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ic_warning)

        icon?.setBounds(0,0,icon.intrinsicWidth,icon.intrinsicHeight)
        when{
            TextUtils.isEmpty(username.text.toString().trim())->{
                username.setError("Please Enter Username",icon)
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                password.setError("Please Enter Password",icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty()  ->
            {
                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){

                    firebaseSignIn()

                }else{
                    username.setError("Please Enter Valid Email Id",icon)
                }
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var cnfPassword: EditText
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
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        username = view.findViewById(R.id.reg_username)
        password = view.findViewById(R.id.reg_password)
        cnfPassword = view.findViewById(R.id.reg_cnf_password)
        fAuth = Firebase.auth

        view.findViewById<Button>(R.id.btn_login_reg).setOnClickListener{
            val navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(LoginFragment(),false)
        }

        view.findViewById<Button>(R.id.btn_register_reg).setOnClickListener {
            validateEmptyForm()
        }
        return view

    }

    private fun firebaseSignUp(){
        btn_register_reg.isEnabled = false
        btn_register_reg.alpha = 0.5f
        fAuth.createUserWithEmailAndPassword(username.text.toString(),
            password.text.toString()).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                val navHome = activity as FragmentNavigation
                navHome.navigateFrag(HomeFragment(),addToStack = true)
            }else{
                btn_register_reg.isEnabled = true
                btn_register_reg.alpha = 1.0f
                Toast.makeText(context,task.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun validateEmptyForm(){
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
            TextUtils.isEmpty(cnfPassword.text.toString().trim())->{
                cnfPassword.setError("Please Enter Password Again",icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() &&
                    cnfPassword.text.toString().isNotEmpty() ->
            {
                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
                    if (password.text.toString().length>=5){
                        if (password.text.toString() == cnfPassword.text.toString()){


                           firebaseSignUp()
//                            Toast.makeText(context,"Registration Successful",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            cnfPassword.setError("Password didn't match",icon)
                        }
                    }
                    else{
                        password.setError("Please Enter at least 5 characters",icon)
                    }
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
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
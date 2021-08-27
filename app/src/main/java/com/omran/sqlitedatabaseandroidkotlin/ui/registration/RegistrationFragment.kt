package com.omran.sqlitedatabaseandroidkotlin.ui.registration

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.trimmedLength
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.omran.sqlitedatabaseandroidkotlin.App

import com.omran.sqlitedatabaseandroidkotlin.R
import com.omran.sqlitedatabaseandroidkotlin.databinding.FragmentLoginBinding
import com.omran.sqlitedatabaseandroidkotlin.databinding.FragmentRegistrationBinding
import com.omran.sqlitedatabaseandroidkotlin.helpers.InputValidation
import com.omran.sqlitedatabaseandroidkotlin.model.User
import com.omran.sqlitedatabaseandroidkotlin.sql.DataBaseHelper

class RegistrationFragment : Fragment(){

    lateinit var registrationBinding : FragmentRegistrationBinding
    private lateinit var listUsers: MutableList<User>

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DataBaseHelper
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       registrationBinding = FragmentRegistrationBinding.inflate(inflater , container, false)

        val view = registrationBinding.root

        // initializing the objects
        initObjects()
        // Inflate the layout for this fragment
        return view    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registrationBinding.include.LoginButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
        registrationBinding.include.btnlogin.setOnClickListener {
            postDataToSQLite()
            Log.d("88888888888", "onViewCreated: ${databaseHelper.getAllUser()}")
        }

    }

    /**
     * This method is to initialize objects to be used
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun initObjects() {
        inputValidation = InputValidation(App.instance())
        databaseHelper = DataBaseHelper(App.instance())

    }



    @RequiresApi(Build.VERSION_CODES.P)
    private fun postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(registrationBinding.include.userName,
                        registrationBinding.include.userNameContainer, getString(R.string.error_message_name))) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(registrationBinding.include.etemail,
                        registrationBinding.include.emailContainer, getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation.isInputEditTextEmail(registrationBinding.include.etemail,
                        registrationBinding.include.emailContainer, getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(registrationBinding.include.mypass, registrationBinding.include.passwordContainer,
                        getString(R.string.error_message_password))) {
            return
        }
        if (!inputValidation.isInputEditTextMatches(registrationBinding.include.mypass, registrationBinding.include.confirmPassword,
                        registrationBinding.include.confirmPasswordContainer, getString(R.string.error_password_match))) {
            return
        }

        if (!databaseHelper.checkUser(registrationBinding.include.etemail.text.toString())) {

            val user = User(name = registrationBinding.include.userName.text.toString(),
                    email = registrationBinding.include.etemail.text.toString(),
                    password = registrationBinding.include.mypass.text.toString())

            databaseHelper.addUser(user)

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(registrationBinding.include.mainContainer,
                    getString(R.string.success_message), Snackbar.LENGTH_LONG).show()
            emptyInputEditText()


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(registrationBinding.include.mainContainer,
                    getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show()
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private fun emptyInputEditText() {
        registrationBinding.include.userName.text = null
        registrationBinding.include.etemail.text = null
        registrationBinding.include.mypass.text = null
        registrationBinding.include.confirmPassword.text = null
    }





}
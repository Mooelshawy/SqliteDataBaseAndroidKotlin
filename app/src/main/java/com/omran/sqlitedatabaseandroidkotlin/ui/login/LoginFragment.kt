package com.omran.sqlitedatabaseandroidkotlin.ui.login

import android.database.Cursor
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.omran.sqlitedatabaseandroidkotlin.App

import com.omran.sqlitedatabaseandroidkotlin.R
import com.omran.sqlitedatabaseandroidkotlin.constaints.DefinedData
import com.omran.sqlitedatabaseandroidkotlin.databinding.FragmentLoginBinding
import com.omran.sqlitedatabaseandroidkotlin.helpers.InputValidation
import com.omran.sqlitedatabaseandroidkotlin.model.User
import com.omran.sqlitedatabaseandroidkotlin.sql.DataBaseHelper

class LoginFragment : Fragment() {

    lateinit var MainBinding : FragmentLoginBinding

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DataBaseHelper
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MainBinding = FragmentLoginBinding.inflate(inflater , container, false)

        val view = MainBinding.root
        initObjects()
        // Inflate the layout for this fragment
        return view
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainBinding.login.setOnClickListener {
            verifyFromSQLite()

        }
        MainBinding.registrationButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    /**
     * This method is to initialize objects to be used
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun initObjects() {

        databaseHelper = DataBaseHelper(App.instance())
        inputValidation = InputValidation(App.instance())

    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun verifyFromSQLite() {

        if (!inputValidation.isInputEditTextFilled(MainBinding.username, MainBinding.userNameContainer,
                        getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation.isInputEditTextEmail(MainBinding.username, MainBinding.userNameContainer,
                        getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(MainBinding.password, MainBinding.passwordContainer,
                        getString(R.string.error_message_email))) {
            return
        }


        Log.d("emial", "verifyFromSQLite:  ${MainBinding.username.text.toString()}")
        Log.d("password", "verifyFromSQLite:  ${MainBinding.password.text.toString()}")

        if (databaseHelper.checkUser(MainBinding.username.text.toString() ,MainBinding.password.text.toString())) {


          // login to main activity
            // Snack Bar to show error message that record already exists
            Snackbar.make(MainBinding.container,
                    "Done this is A user ", Snackbar.LENGTH_LONG).show()

            val user = databaseHelper.getSingleUser(MainBinding.username.text.toString() ,MainBinding.password.text.toString())
            view?.findNavController()?.navigate(LoginFragmentDirections.actionLoginFragmentToMainScreen3(user))
            emptyInputEditText()

        } else {

            // Snack Bar to show success message that record is wrong
            Snackbar.make(MainBinding.container, getString(R.string.error_valid_email_password)+
                    "${databaseHelper.checkUser(MainBinding.username.text.toString() 
                    , MainBinding.password.text.toString())}", Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private fun emptyInputEditText() {
        MainBinding.username.text = null
        MainBinding.password.text = null
    }








}

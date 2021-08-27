package com.omran.sqlitedatabaseandroidkotlin.sql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.text.trimmedLength
import com.omran.sqlitedatabaseandroidkotlin.constaints.DefinedData
import com.omran.sqlitedatabaseandroidkotlin.model.User
import java.util.*

//this class with SQLiteOpenHelper to manage database creation and version management.
@RequiresApi(Build.VERSION_CODES.P)
/*
* To use SQLiteOpenHelper, create a subclass that overrides the onCreate() and
* onUpgrade() callback methods. You may also want to implement the onDowngrade()
* or onOpen() methods, but they are not required.
* */
class DataBaseHelper(context: Context)
    : SQLiteOpenHelper(context, DefinedData.DATABASE_NAME, null , DefinedData.DATABASE_VERSION) {

    // create table sql query
    private val CREATE_USER_TABLE = ("CREATE TABLE " + DefinedData.TABLE_USER + "("
            + DefinedData.COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  DefinedData.COLUMN_USER_NAME + " TEXT,"
            +  DefinedData.COLUMN_USER_EMAIL + " TEXT," +  DefinedData.COLUMN_USER_PASSWORD + " TEXT" + ")")

    // drop table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS ${DefinedData.TABLE_USER}"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USER_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //Drop User Table if exist
        db?.execSQL(DROP_USER_TABLE)
        // Create tables again
        onCreate(db)
    }
    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */

    @SuppressLint("Recycle")
    fun getAllUser() : List<User> {
        // array of columns to fetch
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val columns = arrayOf(DefinedData.COLUMN_USER_ID, DefinedData.COLUMN_USER_EMAIL,
                DefinedData.COLUMN_USER_NAME, DefinedData.COLUMN_USER_PASSWORD)

        // sorting orders
        // How you want the results sorted in the resulting Cursor
        //DESC :decrement
        //ASC : increment
        val sortOrder = "${DefinedData.COLUMN_USER_NAME} ASC"
        //array list to store list of user class
        val userList = ArrayList<User>()

        //variable to open readable object for database
        val db = this.readableDatabase


        // Filter results WHERE "title" = 'My Title'
        //val selection = "${COLUMN_NAME_TITLE} = ?"
        //val selectionArgs = arrayOf("My Title")

        // query the user table
        val cursor = db.query(DefinedData.TABLE_USER, //Table to query
                columns,            //columns to return
                null,     //columns for the WHERE clause
                // (selection and selectionArgs) are combined to create a WHERE clause condition on database
                null,  //The values for the WHERE clause
                null,      //group the rows
                null,       //filter by row groups
                sortOrder)         //The sort order


        //loop to move on table record sequentionly
        /*
        * To look at a row in the cursor, use one of the Cursor move methods,
        * which you must always call before you begin reading values. Since the
        *  cursor starts at position -1, calling moveToNext() places the "read position" on
        *  the first entry in the results and returns whether or not the cursor is already past
        *  the last entry in the result set. For each row, you can read a column's
        *  value by calling one of the Cursor get methods, such as getString() or getLong().
        *  For each of the get methods, you must pass the index position of the column you desire,
        *  which you can get by calling getColumnIndex() or getColumnIndexOrThrow().
        *  When finished iterating through results, call close() on the cursor to release its
        *  resources. For example, the following shows how to get all the item IDs stored in a
        *  cursor and add them to a list:
        * */
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                        id = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_ID)).trimmedLength(),
                        name = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_NAME)),
                        email = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_EMAIL)),
                        password = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_PASSWORD)))

                userList.add(user)
            } while (cursor.moveToNext())
        }

        //should close cursor and db object when finished operation
        cursor.close()
        db.close()

        return userList

    }


    fun getSingleUser(email:String,password:String) : User{
        // array of columns to fetch
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val columns = arrayOf(DefinedData.COLUMN_USER_ID, DefinedData.COLUMN_USER_EMAIL,
            DefinedData.COLUMN_USER_NAME, DefinedData.COLUMN_USER_PASSWORD)

        // sorting orders
        // How you want the results sorted in the resulting Cursor
        //DESC :decrement
        //ASC : increment
        val sortOrder = "${DefinedData.COLUMN_USER_NAME} ASC"


        // selection criteria
        val selection = "${DefinedData.COLUMN_USER_EMAIL} = ? AND  ${DefinedData.COLUMN_USER_PASSWORD} = ?"

        // selection arguments
        val selectionArgs = arrayOf(email, password)
        //variable to open readable object for database
        val db = this.readableDatabase

        // Filter results WHERE "title" = 'My Title'
        //val selection = "${COLUMN_NAME_TITLE} = ?"
        //val selectionArgs = arrayOf("My Title")

        // query the user table
        val cursor = db.query(DefinedData.TABLE_USER, //Table to query
            columns,            //columns to return
            selection,     //columns for the WHERE clause
            // (selection and selectionArgs) are combined to create a WHERE clause condition on database
            selectionArgs,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order



        //return number of email found with this name
        val cursorCount = cursor.count
        Log.d("++++++++++++++", "checkUser:${cursorCount} ")

        if (cursorCount > 0) {

            if (cursor.moveToFirst()) {
                do {
                    return User(
                        id = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_ID))
                            .trimmedLength(),
                        name = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_NAME)),
                        email = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_EMAIL)),
                        password = cursor.getString(cursor.getColumnIndex(DefinedData.COLUMN_USER_PASSWORD))
                    )
                } while (cursor.moveToNext())
            }
            }

        cursor.close()
        db.close()
        return User(id = 0 , name = " " ,email =" " , password= " ")

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    fun addUser(user: User) {
        //object to can write to db
        val db = this.writableDatabase

        //object to collect data about user in row
        //Insert data into the database by passing a ContentValues object to the insert() method:
        val values = ContentValues()
        values.put(DefinedData.COLUMN_USER_NAME, user.name)
        values.put(DefinedData.COLUMN_USER_EMAIL, user.email)
        values.put(DefinedData.COLUMN_USER_PASSWORD, user.password)

        // Inserting Row
        /*
        * The second argument tells the framework what to do in the event that the ContentValues is empty
        * (i.e., you did not put any values). If you specify the name of a column, the framework inserts a
        * row and sets the value of that column to null. If you specify null, like in this code sample,
        * the framework does not insert a row when there are no values.
        * */
        db.insert(DefinedData.TABLE_USER, null, values)
        db.close()
    }

    /**
     * This method to update user record
     *
     * @param user
     */

    fun updateUsr(user:User){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(DefinedData.COLUMN_USER_NAME , user.name)
        values.put(DefinedData.COLUMN_USER_EMAIL , user.email)
        values.put(DefinedData.COLUMN_USER_PASSWORD , user.password)

        // updating row
        db.update(DefinedData.TABLE_USER, values, "${DefinedData.COLUMN_USER_ID } = ? ",
                arrayOf(user.id.toString()))
        db.close()

    }

    /**
     * This method is to delete user record
     *
     * @param user
     */

    fun deleteUser(user: User) {

        val db = this.writableDatabase
        // delete user record by id
        db.delete(DefinedData.TABLE_USER, "${DefinedData.COLUMN_USER_ID} = ?",
                arrayOf(user.id.toString()))
        db.close()


    }


    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */

    fun checkUser(email: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(DefinedData.COLUMN_USER_ID)
        val db = this.readableDatabase

        // selection criteria
        val selection = "${DefinedData.COLUMN_USER_EMAIL} = ?"

        // selection argument
        val selectionArgs = arrayOf(email)

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(DefinedData.TABLE_USER, //Table to query
                columns,        //columns to return
                selection,      //columns for the WHERE clause
                selectionArgs,  //The values for the WHERE clause
                null,  //group the rows
                null,   //filter by row groups
                null)  //The sort order


        //return number of email found with this name
        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0) {
            return true
        }

        return false
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */

    fun checkUser(email:String,password:String):Boolean {

        // array of columns to fetch
        val columns = arrayOf(DefinedData.COLUMN_USER_ID)

        val db = this.readableDatabase


        // selection criteria
        val selection = "${DefinedData.COLUMN_USER_EMAIL} = ? AND ${DefinedData.COLUMN_USER_PASSWORD} = ?"

        // selection arguments
        val selectionArgs = arrayOf(email, password)



        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(DefinedData.TABLE_USER, //Table to query
                columns, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, //The values for the WHERE clause
                null,  //group the rows
                null, //filter by row groups
                null) //The sort order

        val cursorCount = cursor.count
        Log.d("************", "checkUser:${cursorCount} ")


        if (cursorCount > 0)
            return true
        cursor.close()
        db.close()
        return false


    }


    }

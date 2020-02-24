package com.example.sqlitedatabase

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var addbtn:Button
    lateinit var viewbtn:Button
    lateinit var deletebtn:Button
    lateinit var viewallbtn:Button
    lateinit var updatebtn:Button

    lateinit var myId:EditText
    lateinit var myName:EditText
    lateinit var myProfession:EditText
    lateinit var mySalary:EditText

    lateinit var databaseHelper:DatabaseHelper

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this)

        addbtn = findViewById(R.id.addbtn)
        viewallbtn = findViewById(R.id.viewallbtn)
        viewbtn = findViewById(R.id.viewbtn)
        deletebtn = findViewById(R.id.deletebtn)
        updatebtn = findViewById(R.id.updatebtn)

        myId = findViewById(R.id.myId)
        myName = findViewById(R.id.myName)
        myProfession = findViewById(R.id.myProfession)
        mySalary = findViewById(R.id.mySalary)

        addData()
        getData()
        updateData()
        deleteData()
        viewAllData()

    }
    
    private fun addData() {
        addbtn.setOnClickListener(View.OnClickListener {
            val name = myName.text.toString().trim()
            val profession = myProfession.text.toString().trim()
            val salary = mySalary.text.toString().trim()

            if(TextUtils.isEmpty(name)){
                myName.error = "Name required!"
                return@OnClickListener
            }
            if(TextUtils.isEmpty(profession)){
                myProfession.error = "Profession required!"
                return@OnClickListener
            }
            if(TextUtils.isEmpty(salary)){
                mySalary.error = "Salary required!"
                return@OnClickListener
            }
            val isInserted = databaseHelper.insertData(name, profession,salary)
            if (isInserted == true) {
                Toast.makeText(applicationContext, "Data Inserted.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Data Could Not Be Inserted.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    
    private fun viewAllData() {
        viewallbtn.setOnClickListener(View.OnClickListener {
            val res = databaseHelper.getAllData()

            if (res.getCount()==0){
                showMessage("Error ", "Nothing found")
                return@OnClickListener
            } else {
                val buffer = StringBuffer()
                while (res.moveToNext()) {
                    buffer.append("Id:" + res.getString(0) + "\n")
                    buffer.append("Name: " + res.getString(1) + "\n\n")
                    buffer.append("Profession: " + res.getString(2) + "\n\n")
                    buffer.append("Salary: " + res.getString(3) + "\n\n")
                }

                showMessage("Data", buffer.toString())
            }
        })
    }
    
    
    fun deleteData() {

        deletebtn.setOnClickListener(View.OnClickListener {

            val id = myId.getText().toString().trim()
            if (TextUtils.isEmpty(id)) {
                myName.error = "Enter id"
            }

            val deleterows = databaseHelper.deleteData(id)
            if (deleterows!! > 0 ) {
                
                Toast.makeText(applicationContext, "Data deleted ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Data could not deleted ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    
     fun updateData() {

        updatebtn.setOnClickListener(View.OnClickListener {

            val id = myId.getText().toString().trim()
            val name = myName.text.toString().trim()
            val profession = myProfession.text.toString().trim()
            val salary = mySalary.text.toString().trim()

            if (TextUtils.isEmpty(id)) {
                myName.error = "Enter id"
                return@OnClickListener
            }

            if (TextUtils.isEmpty(name)) {

                myName.error = "Enter name"

                return@OnClickListener

            }

            if (TextUtils.isEmpty(profession)) {

                myProfession.error = "Enter profession"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(salary)) {
                mySalary.error = "Enter salary"
                return@OnClickListener
            }
            
            val isUpdated = databaseHelper.updateData(id, name, profession, salary)

            if (isUpdated == true) {
                Toast.makeText(applicationContext, "Data updated ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Data could not be updated ", Toast.LENGTH_SHORT).show()
            }
        })

    }

//// ---- GET DATA

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun getData() {

        viewbtn.setOnClickListener(View.OnClickListener {

            val id = myId.getText().toString().trim()

            if (TextUtils.isEmpty(id)) {
                myId.setError("Enter ID")
                return@OnClickListener
            }
            val res = databaseHelper.getData(id)
            var data: String? = null

            if (res.moveToFirst()) {
                data = "Id:" + res.getString(0) + "\n" +
                        "Name:" + res.getString(1) + "\n" +
                        "Profession:" + res.getString(2) + "\n" +
                        "Salary:" + res.getString(3) + "\n"
            }

            showMessage("Data", data)
        })
    }

    private fun showMessage(title: String, message: String?) {

        val builder = AlertDialog.Builder(this)

        builder.create()
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }

}
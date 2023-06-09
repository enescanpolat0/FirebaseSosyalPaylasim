package com.enescanpolat.firebasesosyalpaylasim.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.enescanpolat.firebasesosyalpaylasim.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if(currentUser!=null){

            val intent = Intent(this, feedActivitiy::class.java)
            startActivity(intent)
            finish()

        }


    }

    fun signinClicked (view:View){
        val email=binding.emailText.text.toString()
        val password=binding.passwordText.text.toString()

        if (email.equals("")||password.equals("")){
            Toast.makeText(this,"Hatali giris tekrar deneyiniz",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                val intent = Intent(this@MainActivity, feedActivitiy::class.java)
                finish()
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    fun signupClicked (view:View){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.equals("")||password.equals("")){

            Toast.makeText(this,"Lutfen email ve sifrenizi giriniz",Toast.LENGTH_LONG).show()

        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                val intent = Intent(this@MainActivity, feedActivitiy::class.java)
                startActivity(intent)
                finish()


            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }




        }



    }
}
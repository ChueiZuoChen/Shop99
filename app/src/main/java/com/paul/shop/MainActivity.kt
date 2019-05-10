package com.paul.shop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private val RC_SIGN_IN: Int = 100
    private val TAG = MainActivity::class.java.simpleName
    var categories = mutableListOf<Category>()
    lateinit var adapter:ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(it, "Verify email sent.", Snackbar.LENGTH_LONG).show()
                    }
                }
        }

        //spinner
        FirebaseFirestore.getInstance().collection("categories")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        categories.add(Category("", "All"))
                        for (doc in it) {
                            categories.add(Category(doc.id, doc.data.get("name").toString()))
                        }
                        spinner.adapter = ArrayAdapter<Category>(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            categories
                        ).apply {
                                setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                            }
                        spinner.setSelection(0, false)
                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            }

                        }
                    }
                }
            }


        //recyclerview
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(mutableListOf<Item>())
        recycler.adapter = adapter


    }

    inner class ItemAdapter(var items:List<Item>) : RecyclerView.Adapter<ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bindTo(items.get(position))
            holder.itemView.setOnClickListener{
                itemClicked(items.get(position),position)
            }
        }

    }



    private fun itemClicked(item: Item, position: Int) {
        Log.d(TAG, "itemClicked: ${item.title} / ${item.price} / $position")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ITEM", item)
        startActivity(intent)
    }


    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser
        Log.d(TAG, "onActivityResult: ${user?.uid}")
        if (user != null) {
            user_info.setText("Email:${user.email} / ${user.isEmailVerified}")
            verify_email.visibility = if (user.isEmailVerified) View.GONE else View.VISIBLE
        } else {
            user_info.setText("Not Login.")
            verify_email.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)

    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signIn -> {
                val whiteList = listOf<String>("tw", "au", "hk", "cn")
                val myLoginLayout = AuthMethodPickerLayout.Builder(R.layout.signup)
                    .setEmailButtonId(R.id.email_auth)
                    .setGoogleButtonId(R.id.google_auth)
                    .setPhoneButtonId(R.id.phone_auth)
                    .build()

                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                        Arrays.asList(
                            AuthUI.IdpConfig.EmailBuilder().build(),
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        )
                    )
                    .setIsSmartLockEnabled(false)
                    .build()
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            Arrays.asList(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.PhoneBuilder()
                                    .setWhitelistedCountries(whiteList)
                                    .setDefaultCountryIso("tw")
                                    .build()
                            )
                        )
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.android1)
                        .setTheme(R.style.SignUp)
                        .setAuthMethodPickerLayout(myLoginLayout)
                        .build(), RC_SIGN_IN
                )

                /*startActivityForResult(Intent(this, SignInActivity::class.java), RC_SIGN_IN)*/
                true
            }
            R.id.action_signout -> {
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

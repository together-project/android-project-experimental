package com.together.button

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        supportActionBar!!.hide()
    }

    companion object {
        const val REQUEST_CALL: Int = 101
        const val REQUEST_CONTACT: Int = 201
        const val REQUEST_NUMBER: Int = 301
    }

    private lateinit var phoneNo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val togetherModal: LinearLayout = findViewById(R.id.togethermodal)
        val dismissView: ImageView = findViewById(R.id.dismissView)
        dismissView.setOnClickListener {
            togetherModal.visibility = View.INVISIBLE
        }

        val sloganDown: TextView = findViewById(R.id.slogan_down)
        val text: String = getString(R.string.aux1)+"\n"+getString(R.string.aux2)+"\n"+
                getString(R.string.aux3)
        sloganDown.text = text

        val call180: Button = findViewById(R.id.call180)
        call180.setOnClickListener {
            requestPermission(true)
        }

        val callSecurity: Button = findViewById(R.id.callSecurity)
        callSecurity.setOnClickListener{
            requestPermission(false)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun requestPermission(call: Boolean) {
        if (call) {
            if (ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
            } else {
                phoneNo = "180"
                calling()
            }
        } else {
            if (ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACT)
            } else {
                fetchPhoneNo()
            }
        }
    }

    private fun calling() {
        val dial = "tel:$phoneNo"
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
    }

    private fun fetchPhoneNo() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_NUMBER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CALL) {
            phoneNo = "180"
            calling()
        } else if (requestCode == REQUEST_CONTACT) {
            fetchPhoneNo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_NUMBER && data?.data != null) {
            val contactUri = data.data;
            val crContacts = contentResolver.query(contactUri!!, null, null,
                    null, null);

            crContacts!!.moveToFirst()
            val id = crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts._ID));

            if (Integer.parseInt(crContacts.getString(crContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                val crPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = ?", arrayOf(id), null)

                crPhones!!.moveToFirst()
                phoneNo = crPhones.getString(crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                calling()
                crPhones.close()
            }
            crContacts.close()
        }
    }
}
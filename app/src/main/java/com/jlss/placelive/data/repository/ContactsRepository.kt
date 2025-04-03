package com.jlss.placelive.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract

class ContactsRepository(applicationContext: Context) {

    fun getContacts(context: Context): List<String> {
        val contactList = mutableListOf<String>()
        val contentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val phoneNumber = it.getString(numberIndex).replace("\\s".toRegex(), "") // Remove spaces
                contactList.add(phoneNumber)
            }
        }
        cursor?.close()
        return contactList
    }
}

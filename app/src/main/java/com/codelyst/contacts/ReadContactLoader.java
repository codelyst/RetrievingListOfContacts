/**
 * Copyright [2015] [Codelyst (Prashant goyal)]
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.se.
 */
package com.codelyst.contacts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.codelyst.contacts.data.Contact;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ReadContactLoader extends AsyncTaskLoader<ArrayList<Contact>> {

    private static final String ACTION_READ_ALL = "com.codelyst.contacts.action.read_all";
    private HashMap<String, Boolean> readPermission;


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    private ReadContactLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Contact> loadInBackground() {

        return handleActionReadAll();
    }


    public void setReadPermission(String readPermission) {
        if (this.readPermission == null) {
            this.readPermission = new HashMap<String, Boolean>();
        }
        this.readPermission.put(readPermission, true);
    }

    private ArrayList<Contact> handleActionReadAll() {
        try {

            final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            Cursor dataCursor = getContext().getContentResolver().query(CONTENT_URI,
                    CONTACT_PROJECTION, SELECTION_ALL,
                    null, ContactsContract.Contacts.SORT_KEY_PRIMARY);

            ArrayList<Contact> contactArrayList = new ArrayList<Contact>();
            while (dataCursor.moveToNext()) {

                Contact contact = new Contact();
                contact.putLong(Contact._ID,
                        dataCursor.getLong(dataCursor.getColumnIndex(ContactsContract.Contacts._ID)));
                contact.putString(Contact.LOOKUP_KEY,
                        dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)));
                contact.putString(Contact.DISPLAY_NAME_PRIMARY,
                        dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                contact.putString(Contact.PHOTO_THUMBNAIL_URI,
                        dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)));
                contact.putString(Contact.SORT_KEY_PRIMARY,
                        dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.SORT_KEY_PRIMARY)));


                if (readPermission != null && readPermission.containsKey(Builder.READ_PHONE_NUMBER)) {
                    findPhoneNumbers(contact);
                }


                if (readPermission != null && readPermission.containsKey(Builder.READ_EMAIL_ADDRESS)) {
                    findEmailAddress(contact);
                }

                contactArrayList.add(contact);


            }


            if(dataCursor != null && !dataCursor.isClosed()){
                dataCursor.close();
            }

            return contactArrayList;
        } catch (Exception exp) {

            Log.e(getClass().getName(), "Exception enable to read contacts.", exp);
        }

        return null;

    }

    private void findEmailAddress(Contact contact) {
        try {

            Cursor emailDataCursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    EMAIL_PROJECTION, SELECTION_EMAIL,
                    new String[]{contact.getString(Contact.LOOKUP_KEY)},
                    null);


            JSONArray email_address = new JSONArray();
            while (emailDataCursor.moveToNext()) {
               // Log.d(getClass().getName(), "Find Email Address" + emailDataCursor.getString(0) + "-- " + emailDataCursor.getInt(1));
                JSONObject phone = new JSONObject();
                phone.put(Contact.Email.ADDRESS, emailDataCursor.getString(0));
                phone.put(Contact.Email.DISPLAY_NAME_PRIMARY, emailDataCursor.getInt(1));
                phone.put(Contact.Email.TYPE, emailDataCursor.getInt(2));
email_address.put(phone);

            }

            contact.putEmailAddresses(email_address);


            if(emailDataCursor != null && !emailDataCursor.isClosed()){
                emailDataCursor.close();
            }

        } catch (Exception exp) {
            Log.e(getClass().getName(), "Handle exp", exp);
        }
    }

    private void findPhoneNumbers(Contact contact) {
        try {

            Cursor phoneDataCursor = getContext().getContentResolver().
                    query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONE_NUMBER_PROJECTION,
                            SELECTION_PHONE, new String[]{contact.getString(Contact.LOOKUP_KEY)}, null);


            JSONArray phone_array = new JSONArray();
            while (phoneDataCursor.moveToNext()) {
                //Log.d(getClass().getName(),""+contact.getString(Contact.DISPLAY_NAME_PRIMARY)+"-- "+phoneDataCursor.getString(0));
                JSONObject phone = new JSONObject();
                phone.put(Contact.Phone.NUMBER, phoneDataCursor.getString(0));
                phone.put(Contact.Phone.TYPE, phoneDataCursor.getInt(1));
                phone_array.put(phone);

            }

            contact.putPhoneNumbers(phone_array);

            if(phoneDataCursor != null && !phoneDataCursor.isClosed()){
                phoneDataCursor.close();
            }

        } catch (Exception exp) {
            Log.e(getClass().getName(), "Exception When Search Phone Numbers", exp);
        }
    }


    // The selection clause for the CursorLoader query. The search criteria defined here
    // restrict results to contacts that have a display name and are linked to visible groups.
    // Notice that the search on the string provided by the user is implemented by appending
    // the search string to CONTENT_FILTER_URI.
    final static String SELECTION_ALL =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY +
                    "<>''" + " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1";
    final static String[] CONTACT_PROJECTION = {

            // The contact's row id
            ContactsContract.Contacts._ID,

            // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
            // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
            // a "permanent" contact URI.
            ContactsContract.Contacts.LOOKUP_KEY,

            // In platform version 3.0 and later, the Contacts table contains
            // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
            // some other useful identifier such as an email address. This column isn't
            // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
            // instead.
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            // In Android 3.0 and later, the thumbnail image is pointed to by
            // PHOTO_THUMBNAIL_URI.
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,

            // The sort order column for the returned Cursor, used by the AlphabetIndexer
            ContactsContract.Contacts.SORT_KEY_PRIMARY,

    };


    final static String SELECTION_PHONE =
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + "=?";

    final static String[] PHONE_NUMBER_PROJECTION = {

            ContactsContract.CommonDataKinds.Phone.NUMBER,

            ContactsContract.CommonDataKinds.Phone.TYPE

    };


    final static String SELECTION_EMAIL =
            ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + "=?";

    final static String[] EMAIL_PROJECTION = {

            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Email.TYPE
    };


    public static class Builder {

        public static String READ_BASIC_INFO = "read_basic_info";
        public static String READ_PHONE_NUMBER = "read_phone_number";
        public static String READ_EMAIL_ADDRESS = "read_email_address";


        private  ReadContactLoader contactLoader;
        private Context context;

        public Builder(Context context) {
            create(context);
        }

        public Builder create(Context context) {
            this.context = context;
            contactLoader = new ReadContactLoader(context);
            contactLoader.setReadPermission(READ_BASIC_INFO);
            return this;
        }

        public Builder addReadPermission(String readPermission) {
            contactLoader.setReadPermission(readPermission);
            return this;
        }

        public ReadContactLoader build() {
            return contactLoader;
        }
    }
}

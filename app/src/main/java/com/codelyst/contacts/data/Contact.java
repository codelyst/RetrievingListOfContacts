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
package com.codelyst.contacts.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prashant on 21-07-2015.
 */
public class Contact {


    public static final String _ID = "_id";
    public static final String LOOKUP_KEY = "lookup";
    public static final String DISPLAY_NAME_PRIMARY = "display_name";
    public static final String PHOTO_THUMBNAIL_URI = "photo_thumb_uri";
    public static final String SORT_KEY_PRIMARY = "sort_key";
    public static final String PHONE_ARRAY = "phone_array";
    public static final String EMAIL_ARRAY = "email_array";


    private final JSONObject data;

    public Contact() {
        data = new JSONObject();
    }

    public String getString(String key) {
        return data.optString(key, "");
    }

    public Contact putString(String key, String value) {

        try {
            data.put(key, value);
        } catch (JSONException e) {
            Log.e(getClass().getName(), "handle", e);
        }

        return this;
    }

    public long getLong(String key) {


        return data.optLong(key, 0);

    }

    public Contact putLong(String key, long value) {

        try {
            data.put(key, value);
        } catch (JSONException e) {
            Log.e(getClass().getName(), "handle", e);
        }

        return this;
    }

    public Contact putPhoneNumbers(JSONArray phone_array) {
        try {

            data.put(PHONE_ARRAY, phone_array);
        } catch (Exception exp) {
            Log.e(getClass().getName(), "Phone Numbers", exp);
        }
        return this;
    }

    public boolean hasPhoneNumbers() {
        try {

            if (data.has(PHONE_ARRAY) && data.optJSONArray(PHONE_ARRAY) != null && data.optJSONArray(PHONE_ARRAY).length() > 0) {

                return true;
            } else {
                return false;
            }
        } catch (Exception exp) {
            Log.e(getClass().getName(), "hasPhoneNumbers exp", exp);

        }
        return false;
    }


    public ArrayList<Phone> getPhoneNumbers() {

        ArrayList<Phone> phones = null;
        try {


            if (hasPhoneNumbers()) {

                phones = new ArrayList<Phone>();

                JSONArray phoneJSONArray = data.optJSONArray(PHONE_ARRAY);
                for (int i = 0; i < phoneJSONArray.length(); i++) {
                    Phone phone = new Phone();
                    phone.setData(phoneJSONArray.getJSONObject(i));
                    phones.add(phone);
                }


            }

        } catch (Exception exp) {
            Log.e(getClass().getName(), "getPhoneNumbers exp", exp);

        }

        return phones;
    }

    public Contact putEmailAddresses(JSONArray email_address) {
        try {

            data.put(EMAIL_ARRAY, email_address);
        } catch (Exception exp) {
            Log.e(getClass().getName(), "Email Addres", exp);
        }
        return this;
    }


    public boolean hasEmailAddresses() {
        try {

            if (data.has(EMAIL_ARRAY) && data.optJSONArray(EMAIL_ARRAY) != null &&
                    data.optJSONArray(EMAIL_ARRAY).length() > 0) {

                return true;
            } else {
                return false;
            }
        } catch (Exception exp) {
            Log.e(getClass().getName(), "hasEmailAddresses exp", exp);

        }
        return false;
    }


    public ArrayList<Email> getEmailAddresses() {

        ArrayList<Email> emails = null;
        try {


            if (hasEmailAddresses()) {
                emails= new ArrayList<Email>();

                JSONArray emailJSONArray = data.optJSONArray(EMAIL_ARRAY);
                for (int i = 0; i < emailJSONArray.length(); i++) {
                    Email email = new Email();
                    email.setData(emailJSONArray.getJSONObject(i));
                    emails.add(email);
                }


            }

        } catch (Exception exp) {
            Log.e(getClass().getName(), "getEmailAddresses exp", exp);

        }

        return emails;
    }



    //Class Phone

    public static class Phone {

        public static String NUMBER = "number";
        public static String TYPE = "type";

        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }

        private JSONObject data;

        public Phone() {
            data = new JSONObject();
        }


        public String getString(String key) {
            return data.optString(key, "");
        }

        public Phone putString(String key, String value) {

            try {
                data.put(key, value);
            } catch (JSONException e) {
                Log.e(getClass().getName(), "handle", e);
            }

            return this;
        }

        public Phone putLong(String key, long value) {

            try {
                data.put(key, value);
            } catch (JSONException e) {
                Log.e(getClass().getName(), "handle", e);
            }

            return this;
        }

        public long getLong(String key) {


            return data.optLong(key, 0);

        }


    }


    //Email Class

    public static class Email {

        public static final String ADDRESS = "address";
        public static final String TYPE = "type";
        public static final String DISPLAY_NAME_PRIMARY = "display_name";



        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }

        private JSONObject data;

        public Email() {
            data = new JSONObject();
        }


        public String getString(String key) {
            return data.optString(key, "");
        }

        public Email putString(String key, String value) {

            try {
                data.put(key, value);
            } catch (JSONException e) {
                Log.e(getClass().getName(), "handle", e);
            }

            return this;
        }

        public Email putLong(String key, long value) {

            try {
                data.put(key, value);
            } catch (JSONException e) {
                Log.e(getClass().getName(), "handle", e);
            }

            return this;
        }

        public long getLong(String key) {


            return data.optLong(key, 0);

        }


    }
}

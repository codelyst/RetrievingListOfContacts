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
package com.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codelyst.contacts.R;
import com.codelyst.contacts.data.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 22-07-2015.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> objects) {
        super(context, R.layout.adapter_contact, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_contact, null);

            holder = new Holder();
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text2 = (TextView) convertView.findViewById(R.id.text2);
            holder.text3 = (TextView) convertView.findViewById(R.id.text3);

            convertView.setTag(holder);

        }else {
            holder = (Holder) convertView.getTag();
        }


        holder.text1.setText(getItem(position).getString(Contact.DISPLAY_NAME_PRIMARY));

        if(getItem(position).hasPhoneNumbers()){

            ArrayList<Contact.Phone> numberList = getItem(position).getPhoneNumbers();

            StringBuffer phoneListBuffer = new StringBuffer();
            for(int i = 0; i < numberList.size(); i++){

                phoneListBuffer.append(numberList.get(i).getString(Contact.Phone.NUMBER)+"\n");
            }

            holder.text2.setText(phoneListBuffer.toString());
        }else{
            holder.text2.setText("");

        }


        if(getItem(position).hasEmailAddresses()){

            ArrayList<Contact.Email> emailAddresses = getItem(position).getEmailAddresses();

            StringBuffer emailsListBuffer = new StringBuffer();
            for(int i = 0; i < emailAddresses.size(); i++){

                emailsListBuffer.append(emailAddresses.get(i).getString(Contact.Email.ADDRESS) + "\n");
            }

            holder.text3.setText(emailsListBuffer.toString());
        }else{
            holder.text3.setText("");

        }

        return convertView;
    }


    public static class Holder{
        public TextView text1;// Name of the Person
        public TextView text2; // List of Phone Numbers
        public TextView text3; // List of Email Address.
    }
}

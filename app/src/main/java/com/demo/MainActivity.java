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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codelyst.contacts.R;
import com.codelyst.contacts.ReadContactLoader;
import com.codelyst.contacts.data.Contact;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Contact> data;
    private ListView listView;
    private View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        loading = findViewById(R.id.loading);

    }




    @Override
    protected void onStart() {
        super.onStart();
       getSupportLoaderManager().initLoader(1, null, new android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Contact>>() {
           @Override
           public android.support.v4.content.Loader<ArrayList<Contact>> onCreateLoader(int id, Bundle args) {

               loading.setVisibility(View.VISIBLE);
               return new ReadContactLoader.Builder(getApplicationContext())
                       .addReadPermission(ReadContactLoader.Builder.READ_BASIC_INFO)
                       .addReadPermission(ReadContactLoader.Builder.READ_PHONE_NUMBER)
                       .addReadPermission(ReadContactLoader.Builder.READ_EMAIL_ADDRESS)
                       .build();
           }

           @Override
           public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Contact>> loader, ArrayList<Contact> data) {
               loading.setVisibility(View.GONE);
               setListAdapter(data);

           }

           @Override
           public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Contact>> loader) {

           }
       });
    }

    private void setListAdapter(ArrayList<Contact> data) {

        getSupportLoaderManager().destroyLoader(1);

        this.data = data;

        ContactAdapter contactAdapter = new ContactAdapter(getApplicationContext(),data);
        listView.setAdapter(contactAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.lovec.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lovec.mobilesafe.engine.ContactEngine;
import com.lovec.mobilesafe.utils.MyAsycnTaks;

import java.util.HashMap;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private ListView lv_contact_contacts;
    private List<HashMap<String, String>> list;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);
        loading = (ProgressBar) findViewById(R.id.loading);

        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("num", list.get(position).get("phone"));
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        new MyAsycnTaks() {
            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinBack() {
                list = ContactEngine.getAllContactInfo(ContactActivity.this);
            }

            @Override
            public void postTask() {
                lv_contact_contacts.setAdapter(new Myadapter());
                loading.setVisibility(View.INVISIBLE);
            }
        }.excute();
    }

    private class Myadapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getApplicationContext(), R.layout.item_contact, null);
            TextView tv_itemcontact_name = (TextView) view.findViewById(R.id.tv_itemcontact_name);
            TextView tv_itemcontact_phone = (TextView) view.findViewById(R.id.tv_itemcontact_phone);
            tv_itemcontact_name.setText(list.get(position).get("name"));
            tv_itemcontact_phone.setText(list.get(position).get("phone"));
            return view;

        }
    }
}

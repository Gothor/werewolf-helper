package com.gothor.werefolfhelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

public class ContactListActivity extends AppCompatActivity {

    ContactAdapter adapter;

    RecyclerView recyclerView;
    Button importButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);

        ArrayList<String> contacts = getContacts();

        recyclerView = findViewById(R.id.list_contact);
        importButton = findViewById(R.id.bt_import);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(adapter);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImportButtonClick();
            }
        });
    }

    ArrayList<String> getContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        ArrayList<String> contacts = new ArrayList<>();

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                contacts.add(name);
            }
        }
        if (cur != null) {
            cur.close();
        }

        Collections.sort(contacts, new IgnoreCaseComparator());

        return contacts;
    }

    private void onImportButtonClick() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("contacts", adapter.getSelectedContacts());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

package com.gothor.werefolfhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.gothor.werefolfhelper.Game.Role;

import java.util.Arrays;
import java.util.List;

public class RoleActivity extends ListActivity {

    public static String RESULT_ROLE_ID = "roleid";
    private List<Role> roleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roleList = Arrays.asList(Game.getGame(this).getAvailableRoles());
        ArrayAdapter<Role> adapter = new RoleListArrayAdapter(this, roleList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Role r = roleList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_ROLE_ID, r.getId());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
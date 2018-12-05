package com.gothor.werefolfhelper;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class PlayersActivity extends PlayerAdapter.Listener {

    private Game game;

    ArrayList<Player> players = new ArrayList<>();

    PlayerAdapter adapter;

    private RecyclerView recyclerView;
    private Button createButton;
    private Button importButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_players);

        game = Game.getGame(this);

        for (Player p : game.players) {
            players.add(new Player(p));
        }

        recyclerView = (RecyclerView) findViewById(R.id.list_players);
        createButton = (Button) findViewById(R.id.bt_create);
        importButton = (Button) findViewById(R.id.bt_import);
        submitButton = (Button) findViewById(R.id.bt_start);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerAdapter(this, players, PlayerAdapter.NAME_VIEW);
        recyclerView.setAdapter(adapter);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateButtonClick();
            }
        });
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImportButtonClick();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitButtonClick();
            }
        });
    }

    private void onCreateButtonClick() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_get_name, null);

        final EditText nameEditText = layout.findViewById(R.id.nameEditText);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(getResources().getInteger(R.integer.name_max_length));
        nameEditText.setFilters(FilterArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout)
                .setTitle(R.string.new_player_title)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameEditText.getText().toString();
                        players.add(new Player(name));
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onImportButtonClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, ContactListActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        }
    }

    protected void onSubmitButtonClick() {
        game.players = players;
        game.save();
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    int originalSize = players.size();
                    ArrayList<String> contactNames = data.getStringArrayListExtra("contacts");

                    for (String name: contactNames) {
                        int limit = getResources().getInteger(R.integer.name_max_length);
                        name = name.trim();
                        if (name.length() > limit) {
                            name = name.substring(0, limit);
                            name = name.trim();
                        }
                        players.add(new Player(name));
                    }

                    adapter.notifyItemRangeInserted(originalSize, players.size() - originalSize);
                }
            }
        }
    }

    @Override
    public void onClickRemove(PlayerAdapter.PlayerViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        Player player = players.get(position);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Retirer " + player.name + " de la partie ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        players.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClickEdit(PlayerAdapter.PlayerViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        final Player player = players.get(position);

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_get_name, null);

        final EditText nameEditText = layout.findViewById(R.id.nameEditText);
        nameEditText.setText(player.name);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(this.getResources().getInteger(R.integer.name_max_length));
        nameEditText.setFilters(FilterArray);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Renommer " + player.name)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameEditText.getText().toString();
                        player.name = name;
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClickRole(PlayerAdapter.PlayerViewHolder viewHolder) { }

    @Override
    public void onClickStatus(PlayerAdapter.PlayerViewHolder viewHolder) { }
}

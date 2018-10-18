package com.gothor.werefolfhelper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayersActivity extends AppCompatActivity {

    private Game game;

    private RecyclerView recyclerView;
    private Button createButton;
    private Button importButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_players);

        game = Game.getGame(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        createButton = (Button) findViewById(R.id.bt_create);
        importButton = (Button) findViewById(R.id.bt_import);
        submitButton = (Button) findViewById(R.id.bt_start);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new PlayerAdapter(game.players, PlayerAdapter.NAME_VIEW);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout)
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameEditText.getText().toString();
                        game.addPlayer(new Player(name));
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
    }

    protected void onSubmitButtonClick() {
        onBackPressed();
        game.save();
    }
}

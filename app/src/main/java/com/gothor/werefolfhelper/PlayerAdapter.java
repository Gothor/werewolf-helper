package com.gothor.werefolfhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    public static final int NAME_VIEW = 0;
    public static final int IN_GAME_VIEW = 1;

    private Context context;
    private ArrayList<Player> players;
    private int viewType;

    public PlayerAdapter(Context context, ArrayList<Player> players, int viewType) {
        this.context = context;
        this.players = players;
        this.viewType = viewType;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlayerViewHolder viewHolder;
        View layout;
        switch (viewType) {
            case IN_GAME_VIEW:
                layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_player_in_game, parent, false);
                viewHolder = new PlayerViewHolderInGame(layout);
                break;
            case NAME_VIEW:
            default:
                layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_player_name, parent, false);
                viewHolder = new PlayerViewHolderName(layout);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setPlayer(player);
    }

    @Override
    public int getItemViewType(int position) {
        return this.viewType;
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /* Abstract ViewHolder */
    public abstract class PlayerViewHolder extends RecyclerView.ViewHolder {

        protected Player player;

        public PlayerViewHolder(View itemView) {
            super(itemView);
        }

        public void setPlayer(Player player) {
            this.player = player;
            onSetPlayer();
        }

        public abstract void onSetPlayer();
    }

    /* View Holder showing only the name */
    public class PlayerViewHolderName extends PlayerViewHolder {

        private TextView tv_name;
        private Button bt_edit;
        private Button bt_remove;

        public PlayerViewHolderName(final View layout) {
            super(layout);

            bt_edit = layout.findViewById(R.id.bt_edit);
            bt_remove = layout.findViewById(R.id.bt_remove);
            tv_name = layout.findViewById(R.id.playerNameTextView);

            bt_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    Player player = players.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Retirer " + player.name + " de la partie ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    players.remove(position);
                                    notifyItemRemoved(position);
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
            });

            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    final Player player = players.get(position);

                    LayoutInflater inflater = LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.dialog_get_name, null);

                    final EditText nameEditText = layout.findViewById(R.id.nameEditText);
                    nameEditText.setText(player.name);

                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(context.getResources().getInteger(R.integer.name_max_length));
                    nameEditText.setFilters(FilterArray);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Renommer " + player.name)
                            .setView(layout)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String name = nameEditText.getText().toString();
                                    player.name = name;
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
            });
        }

        @Override
        public void onSetPlayer() {
            tv_name.setText(player.name);
        }
    }

    /* ViewHolder used in game */
    public class PlayerViewHolderInGame extends PlayerViewHolder {

        private TextView tv_name;
        private ImageView iv_role;
        private ImageView iv_status;

        public PlayerViewHolderInGame(final View layout) {
            super(layout);

            tv_name = layout.findViewById(R.id.playerNameTextView);
            iv_role = layout.findViewById(R.id.playerRoleTextView);
            iv_status = layout.findViewById(R.id.playerStatusTextView);

            iv_role.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Nouveau rôle ?")
                            .setItems(Game.getGame(context).getAvailableRoleNames(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    player.role = Game.getGame(context).getAvailableRoles()[i];
                                    notifyDataSetChanged();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.alive = !player.alive;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onSetPlayer() {
            String role = player.role.toString().toLowerCase();
            int imgId = context.getResources().getIdentifier("role_" + role, "drawable", context.getPackageName());

            tv_name.setText(player.name);
            iv_role.setImageResource(imgId);
            iv_status.setImageResource(player.alive ? R.drawable.alive : R.drawable.dead);
        }
    }

}

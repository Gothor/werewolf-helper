package com.gothor.werefolfhelper;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    public static final int NAME_VIEW = 0;
    public static final int IN_GAME_VIEW = 1;

    private Listener context;
    private ArrayList<Player> players;
    private int viewType;

    public PlayerAdapter(Listener context, ArrayList<Player> players, int viewType) {
        this.context = context;
        this.players = players;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
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
        private ImageButton bt_edit;
        private ImageButton bt_remove;

        public PlayerViewHolderName(final View layout) {
            super(layout);

            bt_edit = layout.findViewById(R.id.bt_edit);
            bt_remove = layout.findViewById(R.id.bt_remove);
            tv_name = layout.findViewById(R.id.playerNameTextView);

            bt_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onClickRemove(PlayerViewHolderName.this);
                }
            });

            bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onClickEdit(PlayerViewHolderName.this);
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
                    context.onClickRole(PlayerViewHolderInGame.this);
                }
            });

            iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onClickStatus(PlayerViewHolderInGame.this);
                }
            });
        }

        @Override
        public void onSetPlayer() {
            tv_name.setText(player.name);
            iv_role.setImageResource(player.role.getPictureId(context));
            iv_status.setImageResource(player.alive ? R.drawable.alive : R.drawable.dead);
        }
    }

    abstract static class Listener extends AppCompatActivity {
        abstract public void onClickRemove(PlayerViewHolder viewHolder);
        abstract public void onClickEdit(PlayerViewHolder viewHolder);
        abstract public void onClickRole(PlayerViewHolder viewHolder);
        abstract public void onClickStatus(PlayerViewHolder viewHolder);
    }

}

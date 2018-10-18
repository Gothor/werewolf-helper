package com.gothor.werefolfhelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    public static final int NAME_VIEW = 0;
    public static final int IN_GAME_VIEW = 1;

    private int viewType;

    private ArrayList<Player> players;

    public PlayerAdapter(ArrayList<Player> players, int viewType) {
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

        public PlayerViewHolderName(final View layout) {
            super(layout);
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    players.remove(position);
                    notifyItemRemoved(position);

                    return false;
                }
            });
            tv_name = layout.findViewById(R.id.playerNameTextView);
        }

        @Override
        public void onSetPlayer() {
            tv_name.setText(player.name);
        }
    }

    /* ViewHolder used in game */
    public class PlayerViewHolderInGame extends PlayerViewHolder {

        private TextView tv_name;

        public PlayerViewHolderInGame(final View layout) {
            super(layout);

            tv_name = layout.findViewById(R.id.playerNameTextView);
        }

        @Override
        public void onSetPlayer() {
            tv_name.setText(player.name);
        }
    }

}

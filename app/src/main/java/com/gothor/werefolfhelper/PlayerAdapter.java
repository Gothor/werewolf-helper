package com.gothor.werefolfhelper;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private RecyclerView rv;
    private ArrayList<Player> players;

    public class ViewHolder extends RecyclerView.ViewHolder {

        int position;
        TextView textView;

        public ViewHolder(final ConstraintLayout layout) {
            super(layout);
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    players.remove(position);
                    notifyItemRemoved(position);
                    // notifyItemRangeChanged(position, players.size());
                    return false;
                }
            });
            textView = layout.findViewById(R.id.playerNameTextView);
        }
    }

    public PlayerAdapter(ArrayList<Player> players, RecyclerView rv) {
        this.players = players;
        this.rv = rv;
    }

    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(players.get(position).name);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }
}

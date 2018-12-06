package com.gothor.werefolfhelper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.gothor.werefolfhelper.Game.Role;

import java.util.List;

public class RoleListArrayAdapter extends ArrayAdapter<Role> {

    private final List<Role> list;
    private final Activity context;

    static class ViewHolder {
        protected ImageView role;
    }

    public RoleListArrayAdapter(Activity context, List<Game.Role> list) {
        super(context, R.layout.role_row, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.role_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.role = view.findViewById(R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        String role = list.get(position).toString().toLowerCase();
        int imgId = context.getResources().getIdentifier("role_" + role, "drawable", context.getPackageName());
        holder.role.setImageResource(imgId);

        return view;
    }
}
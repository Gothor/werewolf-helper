package com.gothor.werefolfhelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<String> contacts;
    private ArrayList<Boolean> selected = new ArrayList<>();

    public ContactAdapter(ArrayList<String> contacts) {
        this.contacts = contacts;
        for (String contact : contacts) {
            selected.add(false);
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_contact, parent, false);
        return new ContactViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setName(this.contacts.get(position));
        holder.setSelected(this.selected.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public ArrayList<String> getSelectedContacts() {
        ArrayList<String> selectedContacts = new ArrayList<>();

        for (int i = 0; i < contacts.size(); i++) {
            if (selected.get(i)) {
                selectedContacts.add(contacts.get(i));
            }
        }

        return selectedContacts;
    }

    /* View Holder showing only the name */
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private CheckBox contactSelected;
        private TextView contactName;

        public ContactViewHolder(final View layout) {
            super(layout);

            contactSelected = layout.findViewById(R.id.contact_selected);
            contactName = layout.findViewById(R.id.contact_name);

            contactSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = getAdapterPosition();
                    selected.set(position, b);
                }
            });
        }

        public void setName(String s) {
            contactName.setText(s);
        }

        public void setSelected(boolean selected) {
            contactSelected.setChecked(selected);
        }
    }

}

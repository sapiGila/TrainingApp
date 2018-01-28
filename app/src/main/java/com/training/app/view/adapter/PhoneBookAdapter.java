package com.training.app.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.training.app.R;
import com.training.app.object.PhoneBook;
import com.training.app.view.activity.PhoneBookActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 7/8/2017.
 */

public class PhoneBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private PhoneBookActivity activity;
    private List<PhoneBook> phoneBooks = new ArrayList<>();
    private List<PhoneBook> phoneBooksFiltered = new ArrayList<>();
    private List<PhoneBook> phoneBookList = new ArrayList<>();

    public PhoneBookAdapter(PhoneBookActivity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.adapter_phone_book, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (phoneBooks.size() > 0) {
            ((ViewHolder) holder).loadBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return phoneBooks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.person_name)
        AppCompatTextView personName;
        @BindView(R.id.person_phone_number)
        AppCompatTextView personPhoneNumber;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadBind(int position) {
            final PhoneBook phoneBook = phoneBooks.get(position);
            personName.setText(phoneBook.getName());
            personPhoneNumber.setText(phoneBook.getPhone());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startPersonActivity(phoneBook);
                }
            });
        }

    }

    @Override
    public Filter getFilter() {
        return getParkingLocationFiltered();
    }

    @NonNull
    private Filter getParkingLocationFiltered() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    phoneBooksFiltered = phoneBookList;
                } else {
                    ArrayList<PhoneBook> filteredList = new ArrayList<>();
                    for (PhoneBook phoneBook : phoneBookList) {
                        if (phoneBook.getName().toLowerCase().contains(charString)
                                || phoneBook.getPhone().toLowerCase().contains(charString)
                                || phoneBook.getAddress().toLowerCase().contains(charString)
                                || phoneBook.getEmail().toLowerCase().contains(charString)) {
                            filteredList.add(phoneBook);
                        }
                    }
                    phoneBooksFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = phoneBooksFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                phoneBooksFiltered = (ArrayList<PhoneBook>) filterResults.values;
                refreshPhoneBook(phoneBooksFiltered);
            }
        };
    }

    public void updatePhoneBooks(List<PhoneBook> phoneBooks) {
        this.phoneBookList = new ArrayList<>();
        this.phoneBookList.addAll(phoneBooks);
        refreshPhoneBook(this.phoneBookList);
    }

    public void refreshPhoneBook(List<PhoneBook> phoneBooks) {
        this.phoneBooks = new ArrayList<>();
        this.phoneBooks.addAll(phoneBooks);
        notifyDataSetChanged();
    }

    public void clearPhoneBooks() {
        this.phoneBookList = new ArrayList<>();
        this.phoneBooks = new ArrayList<>();
        notifyDataSetChanged();
    }
}

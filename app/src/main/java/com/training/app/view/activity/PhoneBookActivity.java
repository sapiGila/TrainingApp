package com.training.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.training.app.R;
import com.training.app.dummy.DummyPhoneBook;
import com.training.app.object.PhoneBook;
import com.training.app.view.adapter.PhoneBookAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/8/2017.
 */

public class PhoneBookActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_phone_book)
    RecyclerView recyclerViewPhoneBook;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<PhoneBook> phoneBooks = new ArrayList<>();
    private PhoneBookAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        phoneBooks = DummyPhoneBook.getPhoneBooks();
        adapter = new PhoneBookAdapter(PhoneBookActivity.this);
        recyclerViewPhoneBook.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhoneBook.setAdapter(adapter);
        adapter.updatePhoneBooks(phoneBooks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phone_book, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @OnClick(R.id.add_person)
    public void onViewClicked() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }

    public void startPersonActivity(PhoneBook phoneBook) {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("person", Parcels.wrap(phoneBook));
        startActivity(intent);
    }
}

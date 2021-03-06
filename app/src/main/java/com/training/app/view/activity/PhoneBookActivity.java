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
import android.view.View;
import android.widget.ProgressBar;

import com.training.app.R;
import com.training.app.contract.PhoneBookContract;
import com.training.app.datasource.PhoneBookRestApiAdapter;
import com.training.app.model.PhoneBookRealmRepository;
import com.training.app.model.PhoneBookRepository;
import com.training.app.object.PhoneBook;
import com.training.app.presenter.PhoneBookPresenter;
import com.training.app.util.RealmDB;
import com.training.app.util.Toaster;
import com.training.app.view.adapter.PhoneBookAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dell on 7/8/2017.
 */

public class PhoneBookActivity extends AppCompatActivity implements PhoneBookContract.PhoneBookView {

    @BindView(R.id.recycler_view_phone_book)
    RecyclerView recyclerViewPhoneBook;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<PhoneBook> phoneBooks = new ArrayList<>();
    private PhoneBookAdapter adapter;
    private PhoneBookContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        adapter = new PhoneBookAdapter(PhoneBookActivity.this);
        recyclerViewPhoneBook.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhoneBook.setAdapter(adapter);
//        presenter = new PhoneBookPresenter(this, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBeforeProcessing();
        presenter = new PhoneBookPresenter(this, Schedulers.io(), AndroidSchedulers.mainThread(),
                new RealmDB(), new PhoneBookRepository(PhoneBookRestApiAdapter.getPhoneBookRestApi()),
                new PhoneBookRealmRepository());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPersons();
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
        intent.putExtra("person", Parcels.wrap(PhoneBook.class, phoneBook));
        startActivity(intent);
    }

    @Override
    public void doBeforeProcessing() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doShowPerson(List<PhoneBook> phoneBooks) {
        progressBar.setVisibility(View.GONE);
        if (phoneBooks.size() > 0) {
            adapter.updatePhoneBooks(phoneBooks);
        } else {
            adapter.clearPhoneBooks();
            Toaster.show(this, "Kosong");
        }
    }

    @Override
    public void doOnError(String message) {
        progressBar.setVisibility(View.GONE);
        Toaster.show(this, message);
    }

    @Override
    public void doAfterProcessing() {
        progressBar.setVisibility(View.GONE);
    }

    private void initPersons() {
        presenter.getPersons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

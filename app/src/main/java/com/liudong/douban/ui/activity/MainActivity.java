package com.liudong.douban.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.liudong.douban.R;
import com.liudong.douban.di.components.ActivityComponent;
import com.liudong.douban.ui.fragment.book.BookFragment;
import com.liudong.douban.ui.fragment.movie.MovieFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Inject
    MainDisplay mainDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(R.string.movie_tit);
            navigationView.setCheckedItem(R.id.nav_movie);
            mainDisplay.loadRootFragment(MovieFragment.newInstance(), "MovieFragment");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawer.setFitsSystemWindows(true);
            drawer.setClipToPadding(false);
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_movie:
                MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentByTag("MovieFragment");
                if (movieFragment == null) {
                    movieFragment = MovieFragment.newInstance();
                    mainDisplay.addFragmentToActivity(movieFragment, "MovieFragment");
                }
                mainDisplay.showFragment(movieFragment);
                getSupportActionBar().setTitle(R.string.movie_tit);
                break;
            case R.id.nav_book:
                BookFragment bookFragment = (BookFragment) getSupportFragmentManager().findFragmentByTag("BookFragment");
                if (bookFragment == null) {
                    bookFragment = BookFragment.newInstance();
                    mainDisplay.addFragmentToActivity(bookFragment, "BookFragment");
                }
                mainDisplay.showFragment(bookFragment);
                getSupportActionBar().setTitle(R.string.book_tit);
                break;
            case R.id.me:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.about:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}

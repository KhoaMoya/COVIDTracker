package com.khoa.covidtracker.main.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khoa.covidtracker.R;
import com.khoa.covidtracker.databinding.ActivityMainBinding;
import com.khoa.covidtracker.home.view.HomeFragment;
import com.khoa.covidtracker.main.viewmodel.MainViewModel;
import com.khoa.covidtracker.map.view.MapFragment;
import com.khoa.covidtracker.model.TrackerWrapper;
import com.khoa.covidtracker.news.view.NewsFragment;
import com.khoa.covidtracker.repository.network.ApiServiceFactory;
import com.khoa.covidtracker.world.view.WorldFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    public MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }

        setupBinding();
        showFragment();
        loadDataFromBoYTe();
    }

    private void showFragment() {
        if (mViewModel.currentFragmentTag.isEmpty()) {
            // show home fragment
            mBinding.btmNav.setSelectedItemId(R.id.menu_home);
        } else {
            switch (mViewModel.currentFragmentTag) {
                case MapFragment.TAG:
                    mBinding.btmNav.setSelectedItemId(R.id.menu_map);
                    break;
                case NewsFragment.TAG:
                    mBinding.btmNav.setSelectedItemId(R.id.menu_news);
                    break;
                case WorldFragment.TAG:
                    mBinding.btmNav.setSelectedItemId(R.id.menu_world);
                    break;
                default:
                    mBinding.btmNav.setSelectedItemId(R.id.menu_home);
            }
        }
    }

    private void setupBinding() {
        mBinding.btmNav.setOnNavigationItemSelectedListener(this);


    }

    private void loadFragment(String tag, MyFragment fragment, boolean add, boolean hide) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (add && !hide) {
            fragmentTransaction.add(R.id.container, fragment, tag);
        } else if (!add && hide) {
            fragmentTransaction.hide(mViewModel.getCurrentFragment());
            fragmentTransaction.show(fragment);
        } else if (add && hide) {
            fragmentTransaction.hide(mViewModel.getCurrentFragment());
            fragmentTransaction.add(R.id.container, fragment, tag);
        }
        fragmentTransaction.commit();

        mViewModel.currentFragmentTag = tag;
        if (add) {
            mViewModel.fragmentMap.put(tag, fragment);
        }
    }

    private void switchFrament(String tag, MyFragment fragment) {
        if (tag.equals(mViewModel.currentFragmentTag)) return;
        // chưa có fragment được hiển thị
        if (mViewModel.currentFragmentTag.isEmpty()) {
            loadFragment(tag, fragment, true, false);
        }
        // đã có fragment
        else {
            Fragment frm = mViewModel.fragmentMap.get(tag);
            if (frm != null) {
                // ẩn fragment cũ và hiển thị fragment mới
                loadFragment(tag, fragment, false, true);
            } else {
                // ẩn fragment cũ và thêm fragment mới
                loadFragment(tag, fragment, true, true);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MyFragment newFragment = null;
        String newTag = "";
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home:
                newTag = HomeFragment.TAG;
                newFragment = mViewModel.fragmentMap.get(newTag);
                if (newFragment == null)
                    newFragment = HomeFragment.getInstance();
                break;
            case R.id.menu_map:
                newTag = MapFragment.TAG;
                newFragment = mViewModel.fragmentMap.get(newTag);
                if (newFragment == null)
                    newFragment = MapFragment.getInstance();
                break;
            case R.id.menu_news:
                newTag = NewsFragment.TAG;
                newFragment = mViewModel.fragmentMap.get(newTag);
                if (newFragment == null)
                    newFragment = NewsFragment.getInstance();
                break;
            case R.id.menu_world:
                newTag = WorldFragment.TAG;
                newFragment = mViewModel.fragmentMap.get(newTag);
                if (newFragment == null)
                    newFragment = WorldFragment.getInstance();
                break;
            default:
                break;
        }
        if (newFragment != null) switchFrament(newTag, newFragment);
        return true;
    }

    public void loadDataFromBoYTe() {
        mViewModel.disposable.add(ApiServiceFactory
                .getBoYTeApiService()
                .getData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TrackerWrapper>() {
                    @Override
                    public void onSuccess(TrackerWrapper tw) {
                        mViewModel.trackerWrapper.postValue(tw);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}

package com.khoa.covidtracker.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.khoa.covidtracker.databinding.FragmentHomeBinding;
import com.khoa.covidtracker.home.viewmodel.HomeViewModel;
import com.khoa.covidtracker.main.view.MyFragment;
import com.khoa.covidtracker.model.Tracker;
import com.khoa.covidtracker.model.TrackerWrapper;
import com.khoa.covidtracker.utils.TimeUtils;

import java.util.List;

public class HomeFragment extends MyFragment {

    public final static String TAG = "HomeFragment";
    private static HomeFragment instance;

    private HomeViewModel mViewModel;
    private FragmentHomeBinding mBinding;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupBinding();
    }

    @Override
    public void setupBinding() {
        mBinding.rcvProvince.setAdapter(mViewModel.mProvinceAdapter);
        mBinding.actionBar.imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
            }
        });

        getTrackerWrapper().observe(getViewLifecycleOwner(), new Observer<TrackerWrapper>() {
            @Override
            public void onChanged(TrackerWrapper trackerWrapper) {
                onFinishLoading();
                showCountryStatistic(trackerWrapper);
            }
        });
    }

    private void showProvinceList(TrackerWrapper trackerWrapper) {
        mBinding.txtListProvince.setText("Danh sách " + (trackerWrapper.data.VN.size()-2) + " tỉnh thành bị ảnh hưởng");
        mViewModel.mProvinceAdapter.setTrackerList(trackerWrapper.data.VN.subList(0, trackerWrapper.data.VN.size() - 2));
    }

    private void showCountryStatistic(TrackerWrapper trackerWrapper) {
        if(!trackerWrapper.success) return;
        List<Tracker> vnTrackerList = trackerWrapper.data.VN;
        if (vnTrackerList.isEmpty()) return;

        showCountryInfo(vnTrackerList.get(vnTrackerList.size() - 1));
        showProvinceList(trackerWrapper);
    }

    private void showCountryInfo(Tracker tracker) {
        mBinding.txtAmountConfirmed.setText(tracker.confirmed + "");
        mBinding.txtAmountRecovered.setText(tracker.recovered + "");
        mBinding.txtAmountDeath.setText(tracker.deaths + "");
        mBinding.txtNewConfirmed.setText("+" + tracker.increaseConfirmed);
        mBinding.txtNewDeath.setText("+" + tracker.increaseRecovered);
        mBinding.txtLastupdate.setText("Cập nhật lúc: " + TimeUtils.convertTimestamp(tracker.lastUpdate));
        mBinding.txtSource.setText("Nguồn: Bộ Y tế");
    }

    @Override
    public void refreshData() {
        onStartLoading();
        getMainActivity().loadDataFromBoYTe();
    }

    @Override
    public void onStartLoading() {
        mBinding.actionBar.progressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishLoading() {
        mBinding.actionBar.progressLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}

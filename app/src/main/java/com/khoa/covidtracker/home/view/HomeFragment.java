package com.khoa.covidtracker.home.view;

import android.os.Bundle;
import android.text.Html;
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
import com.khoa.covidtracker.model.Country;
import com.khoa.covidtracker.model.Province;
import com.khoa.covidtracker.repository.network.ApiServiceFactory;
import com.khoa.covidtracker.utils.TimeUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

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
        loadData();
    }

    private void setupBinding() {
        mBinding.rcvProvince.setAdapter(mViewModel.provinceAdapter);
        mBinding.actionBar.imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        mViewModel.provinceList.observe(getViewLifecycleOwner(), new Observer<List<Province>>() {
            @Override
            public void onChanged(List<Province> provinces) {
                mViewModel.provinceAdapter.setProvinceList(provinces);
                mBinding.txtListProvince.setText(Html.fromHtml("Danh sách  <font color=\"yellow\">"
                        + provinces.size() + "</font> tỉnh thành bị ảnh hưởng", Html.FROM_HTML_MODE_LEGACY));
            }
        });
    }

    private void loadData() {
        onStartLoading();
        // tải thống kê tổng quan
        addDisposble(createStatisticDisposable());

        // tải thống kê theo tỉnh
        addDisposble(createProvinceDisposable());
    }

    private void showCountryInfo(Country country) {
        mBinding.txtAmountConfirmed.setText(country.totalConfirmed + "");
        mBinding.txtAmountRecovered.setText(country.totalRecovered + "");
        mBinding.txtAmountDeath.setText(country.totalDeaths + "");
        mBinding.txtNewConfirmed.setText("+" + country.dailyConfirmed);
        mBinding.txtNewDeath.setText("+" + country.dailyDeaths + "");
        mBinding.txtLastupdate.setText(TimeUtils.convertDate(country.lastUpdated));
        mBinding.txtSource.setText("Nguồn: coronatracker.com");
    }

    private Disposable createStatisticDisposable() {
        return ApiServiceFactory.getTrackerApiService()
                .getCountryInfo("VN")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<List<Country>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        onFinishLoading();
                    }

                    @Override
                    public void onSuccess(List<Country> countries) {
                        if (countries.size() > 0) showCountryInfo(countries.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(e.getMessage());
                    }
                });
    }

    private Disposable createProvinceDisposable() {
        return ApiServiceFactory.getProvincesApiService()
                .getProvinces()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        onFinishLoading();
                    }

                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        mViewModel.handleResponBodyProvince(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(e.getMessage());
                    }
                });
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

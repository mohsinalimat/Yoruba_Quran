package com.alium.yoruba_quran.ui.chapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alium.quran_app_domain.entities.ChapterEntity;
import com.alium.yoruba_quran.R;
import com.alium.yoruba_quran.injection.data.RoomModule;
import com.alium.yoruba_quran.injection.people.PeopleModule;
import com.alium.yoruba_quran.ui.base.view.BaseFragment;
import com.alium.yoruba_quran.ui.main.MainActivity;
import com.alium.yoruba_quran.ui.widget.CustomAppBar;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 04/01/2017.
 */

public class ChaptersFragment extends BaseFragment implements ChapterContract.View {

    @BindView(R.id.fragment_people__swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.fragment_people__recycler_view)
    RecyclerView recyclerView;

    @Inject
    ChapterContract.Presenter presenter;

    private ChapterAdapter adapter;

    public static ChaptersFragment newInstance() {
        ChaptersFragment fragment = new ChaptersFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();

        presenter.onCreate();

    }


    @Override
    public void attachToPresenter() {
        super.attachToPresenter();
        presenter.attachView(this);
    }

    @Override
    public void detachFromPresenter() {
        super.detachFromPresenter();
        presenter.detachView();
    }


    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChapterAdapter();
        adapter.setOnPersonClickListener(new ChapterView.OnPersonClickListener() {
            @Override
            public void onPersonClick(ChapterEntity chapter) {
                presenter.clickPerson(chapter);
            }

            @Override
            public void onPersonActionClick(ChapterEntity chapter) {
                presenter.clickPersonAction(chapter);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        CustomAppBar appBar = ((MainActivity) getActivity()).getCustomAppBar();
        appBar.setTitle(getString(R.string.fragment_people__title));
        appBar.setMenuRes(R.menu.people_general, R.menu.people_specific, R.menu.people_merged);

        final SearchView searchView = appBar.findViewById(R.id.menu_people__search);
        searchView.setOnQueryTextListener(new OnSearchQueryTextListener());

    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getPeople();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        //swipeRefreshLayout bug
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
            swipeRefresh.destroyDrawingCache();
            swipeRefresh.clearAnimation();
        }
    }

    @Override
    public void injectDependencies() {
        ((MainActivity) getActivity())
                .getMainComponent()
                .plus(new PeopleModule(), new RoomModule(getActivity()))
                .inject(this);
    }

    class OnSearchQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            final List<ChapterEntity> filteredModelList = adapter.filter(newText);
            adapter.setPeopleList(filteredModelList);
            return true;
        }
    }



    @Override
    public void onLandscape() {

    }

    @Override
    public void onPortrait() {

    }

    @Override
    public void getArgs(Bundle _bundle) {

    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public View getRootView() {
        return recyclerView;
    }

    @Override
    public void showNoNetwork() {

    }

    @Override
    public void showPeopleList(List<ChapterEntity> peopleList) {
        adapter.setPeopleList(peopleList);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

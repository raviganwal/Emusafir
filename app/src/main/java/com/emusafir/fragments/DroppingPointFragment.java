package com.emusafir.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emusafir.R;
import com.emusafir.adapter.DroppingPointAdapter;
import com.emusafir.interfaces.NotifyPointLayout;
import com.emusafir.interfaces.OnPointSelected;
import com.emusafir.model.buslayoutmodel.PointModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class DroppingPointFragment extends Fragment implements OnPointSelected {

    private final String TAG = BoardingPointFragment.class.getSimpleName();
    private ViewPager mViewPager;
    private DroppingPointAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private NotifyPointLayout mNotifyPointLayout;

    public static DroppingPointFragment newInstance(List<PointModel> mBusLayoutModel) {
        DroppingPointFragment fragment = new DroppingPointFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mBusLayoutModel", (Serializable) mBusLayoutModel);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dropping_point, container, false);
        ArrayList<PointModel> mList = (ArrayList<PointModel>) getArguments().getSerializable("mBusLayoutModel");

        mRecyclerView = view.findViewById(R.id.mRecyclerView);

        mAdapter = new DroppingPointAdapter(mList, getActivity(), this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
//        mRecyclerView.addItemDecoration(itemDecor);

//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(final View view, final int position) {
//                    }
//                })
//        );
        return view;
    }

    @Override
    public void onPointSelected(PointModel point, final boolean isBoarding) {
        mNotifyPointLayout.notifyPointLayout(point, isBoarding);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mNotifyPointLayout = (NotifyPointLayout) parent;
        } else {
            mNotifyPointLayout = (NotifyPointLayout) context;
        }
    }

    @Override
    public void onDetach() {
        mNotifyPointLayout = null;
        super.onDetach();
    }
}




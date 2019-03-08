package com.emusafir.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emusafir.R;
import com.emusafir.adapter.BoardingPointAdapter;
import com.emusafir.interfaces.NotifyPointLayout;
import com.emusafir.interfaces.OnPointSelected;
import com.emusafir.model.buslayoutmodel.PointModel;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class BoardingPointFragment extends Fragment implements OnPointSelected {

    private final String TAG = BoardingPointFragment.class.getSimpleName();
    private ViewPager mViewPager;
    private BoardingPointAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private NotifyPointLayout mNotifyPointLayout;

    public static BoardingPointFragment newInstance(List<PointModel> mBusLayoutModel) {
        BoardingPointFragment fragment = new BoardingPointFragment();
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
        List<PointModel> mList = (List<PointModel>) getArguments().getSerializable("mBusLayoutModel");

        View view = inflater.inflate(R.layout.fragment_boarding_point, container, false);

        mRecyclerView = view.findViewById(R.id.mRecyclerView);


        for (int i = 0; i < mList.size(); i++) {
            Log.e(TAG, "BOARD "+mList.get(i).getId() + " " + mList.get(i).getName());
        }
        mAdapter = new BoardingPointAdapter(mList, getActivity(), this);


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
        Log.e(TAG,point.getId()+" "+point.getName()+" "+isBoarding);
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


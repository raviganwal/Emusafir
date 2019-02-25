package com.emusafir.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emusafir.R;
import com.emusafir.adapter.SeatLayoutAdapter;
import com.emusafir.interfaces.NotifyBusLayout;
import com.emusafir.interfaces.OnSeatSelected;
import com.emusafir.model.buslayoutmodel.BusLayoutModel;
import com.emusafir.model.buslayoutmodel.LowerDack;
import com.emusafir.model.buslayoutmodel.SeatModel;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


public class LowerDeckFragment extends Fragment implements OnSeatSelected {

    private final String TAG = LowerDeckFragment.class.getSimpleName();
    private ViewPager mViewPager;
    //    private ArrayList<LowerDack> mRighrLowerList;


//    private LinearLayout linearlayout;

    private ArrayList<String> deckList;

    private SeatLayoutAdapter mLeftSeatLayoutAdapter;
    private SeatLayoutAdapter mRightSeatLayoutAdapter;
    private SeatLayoutAdapter mEndRowAdapter;

    private RecyclerView mRecyclerViewLeft1, mRecyclerViewLeft2;
    private RecyclerView mRecyclerViewRight1, mRecyclerViewRight2, mRecyclerViewRight3;
    private RecyclerView mEndRowRecyclerView;
    private int selectedPos = 0;

    private CardView mCardViewBookNow;

    private NotifyBusLayout mNotifyBusLayout;

    public static LowerDeckFragment newInstance(BusLayoutModel mBusLayoutModel) {
        LowerDeckFragment fragment = new LowerDeckFragment();
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

        View view = inflater.inflate(R.layout.fragment_deck, container, false);

//        ArrayList<LowerDack> mLowerList = (ArrayList<LowerDack>) getArguments().getSerializable("mLowerList");
        BusLayoutModel mBusLayoutModel = (BusLayoutModel) getArguments().getSerializable("mBusLayoutModel");
//        linearlayout = view.findViewById(R.id.linearlayout);
        mRecyclerViewLeft1 = view.findViewById(R.id.mRecyclerViewLeft1);
        mRecyclerViewLeft2 = view.findViewById(R.id.mRecyclerViewLeft2);
        mRecyclerViewRight1 = view.findViewById(R.id.mRecyclerViewRight1);
        mRecyclerViewRight2 = view.findViewById(R.id.mRecyclerViewRight2);
        mRecyclerViewRight3 = view.findViewById(R.id.mRecyclerViewRight3);
        mEndRowRecyclerView = view.findViewById(R.id.mEndRowRecyclerView);
        mCardViewBookNow = getActivity().findViewById(R.id.mCardViewBookNow);

//        mLowerList = new ArrayList<>();
//        mRighrLowerList = new ArrayList<>();
//        mEndRowList = new ArrayList<>();

        deckList = new ArrayList<>();


        LowerDack mLowerDeck = mBusLayoutModel.getLowerDack();
        for (int i = 0; i < mLowerDeck.getLeft().size(); i++) {
            if (mLowerDeck.getLeft().size() > 0) {
                mLeftSeatLayoutAdapter = new SeatLayoutAdapter(mLowerDeck.getLeft().get(0), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewLeft1.setLayoutManager(mLayoutManager);
                mRecyclerViewLeft1.setAdapter(mLeftSeatLayoutAdapter);
                mRecyclerViewLeft1.setNestedScrollingEnabled(false);
            }
            if (mLowerDeck.getLeft().size() > 1) {
                mLeftSeatLayoutAdapter = new SeatLayoutAdapter(mLowerDeck.getLeft().get(1), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewLeft2.setLayoutManager(mLayoutManager);
                mRecyclerViewLeft2.setAdapter(mLeftSeatLayoutAdapter);
                mRecyclerViewLeft2.setNestedScrollingEnabled(false);
            }
        }
        for (int i = 0; i < mLowerDeck.getRight().size(); i++) {
            if (mLowerDeck.getRight().size() > 0) {
                mRightSeatLayoutAdapter = new SeatLayoutAdapter(mLowerDeck.getRight().get(0), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewRight1.setLayoutManager(mLayoutManager);
                mRecyclerViewRight1.setAdapter(mRightSeatLayoutAdapter);
                mRecyclerViewRight1.setNestedScrollingEnabled(false);
            }
            if (mLowerDeck.getRight().size() > 1) {
                mRightSeatLayoutAdapter = new SeatLayoutAdapter(mLowerDeck.getRight().get(1), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewRight2.setLayoutManager(mLayoutManager);
                mRecyclerViewRight2.setAdapter(mRightSeatLayoutAdapter);
                mRecyclerViewRight2.setNestedScrollingEnabled(false);
            }
            if (mLowerDeck.getRight().size() > 2) {
                mRightSeatLayoutAdapter = new SeatLayoutAdapter(mLowerDeck.getRight().get(2), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewRight3.setLayoutManager(mLayoutManager);
                mRecyclerViewRight3.setAdapter(mRightSeatLayoutAdapter);
                mRecyclerViewRight3.setNestedScrollingEnabled(false);
            }
        }


        if (mBusLayoutModel.getEndRowDack() != null)
            mEndRowAdapter = new SeatLayoutAdapter(mBusLayoutModel.getEndRowDack(), getActivity(), this);


        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mEndRowRecyclerView.setLayoutManager(mLayoutManager1);
        mEndRowRecyclerView.setAdapter(mEndRowAdapter);
        mEndRowRecyclerView.setNestedScrollingEnabled(false);


        return view;
    }


    @Override
    public void onSeatSelected(SeatModel mList, int position) {
        mNotifyBusLayout.notifyBusLayout(mList, position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mNotifyBusLayout = (NotifyBusLayout) parent;
        } else {
            mNotifyBusLayout = (NotifyBusLayout) context;
        }
    }

    @Override
    public void onDetach() {
        mNotifyBusLayout = null;
        super.onDetach();
    }
}
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
import com.emusafir.model.buslayoutmodel.SeatModel;
import com.emusafir.model.buslayoutmodel.UpperDack;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


public class UpperDeckFragment extends Fragment implements  OnSeatSelected {

    private final String TAG = UpperDeckFragment.class.getSimpleName();
    private ViewPager mViewPager;
    //    private ArrayList<LowerDack> mRighrLowerList;


//    private LinearLayout linearlayout;
    private BusLayoutModel mBusLayoutModel;
    private ArrayList<String> deckList;

    private SeatLayoutAdapter mSeatLayoutAdapter;
    //    private RightSeatLayoutAdapter mRightSeatLayoutAdapter;
    private SeatLayoutAdapter mRightSeatLayoutAdapter;
    //    private SeatLayoutAdapter mRightAdapter;
    private SeatLayoutAdapter mEndRowAdapter;

    private RecyclerView mRecyclerViewLeft1, mRecyclerViewLeft2;
    private RecyclerView mRecyclerViewRight1, mRecyclerViewRight2, mRecyclerViewRight3;
    private RecyclerView mEndRowRecyclerView;
    private int selectedPos = 0;

    private CardView mCardViewBookNow;

    private NotifyBusLayout mNotifyBusLayout;

    public static UpperDeckFragment newInstance(BusLayoutModel mBusLayoutModel) {
        UpperDeckFragment fragment = new UpperDeckFragment();
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


        UpperDack mUpperDeck = mBusLayoutModel.getUpperDack();
        for (int i = 0; i < mUpperDeck.getLeft().size(); i++) {
            if (mUpperDeck.getLeft().size() > 0) {
                mSeatLayoutAdapter = new SeatLayoutAdapter(mUpperDeck.getLeft().get(0), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewLeft1.setLayoutManager(mLayoutManager);
                mRecyclerViewLeft1.setAdapter(mSeatLayoutAdapter);
                mRecyclerViewLeft1.setNestedScrollingEnabled(false);
            }
            if (mUpperDeck.getLeft().size() > 1) {
                mSeatLayoutAdapter = new SeatLayoutAdapter(mUpperDeck.getLeft().get(1), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewLeft2.setLayoutManager(mLayoutManager);
                mRecyclerViewLeft2.setAdapter(mSeatLayoutAdapter);
                mRecyclerViewLeft2.setNestedScrollingEnabled(false);
            }
        }
        for (int i = 0; i < mUpperDeck.getRight().size(); i++) {
            if (mUpperDeck.getRight().size() > 0) {
                mRightSeatLayoutAdapter = new SeatLayoutAdapter(mUpperDeck.getRight().get(0), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewRight1.setLayoutManager(mLayoutManager);
                mRecyclerViewRight1.setAdapter(mRightSeatLayoutAdapter);
                mRecyclerViewRight1.setNestedScrollingEnabled(false);
            }
            if (mUpperDeck.getRight().size() > 1) {
                mRightSeatLayoutAdapter = new SeatLayoutAdapter(mUpperDeck.getRight().get(1), getActivity(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                mRecyclerViewRight2.setLayoutManager(mLayoutManager);
                mRecyclerViewRight2.setAdapter(mRightSeatLayoutAdapter);
                mRecyclerViewRight2.setNestedScrollingEnabled(false);
            }
            if (mUpperDeck.getRight().size() > 2) {
                mRightSeatLayoutAdapter = new SeatLayoutAdapter(mUpperDeck.getRight().get(2), getActivity(), this);
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

//        mRecyclerViewLeft1.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(final View view, final int position) {
//                        selectedPos = position;
//                        linearlayout.setVisibility(linearlayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//                    }
//                })
//        );


//        mRightRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(final View view, final int position) {
//                        selectedPos = position;
//                        linearlayout.setVisibility(linearlayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//                    }
//                })
//        );
//
//
//        mEndRowRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(final View view, final int position) {
//                        selectedPos = position;
//                        linearlayout.setVisibility(linearlayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//                    }
//                })
//        );
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
package com.emusafir.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.emusafir.CityActivity;
import com.emusafir.R;
import com.emusafir.adapter.AmenityAdapter;
import com.emusafir.model.BusAmenity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AmenityDialogFragment extends BottomSheetDialogFragment {
    private static final String TAG = CityActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<BusAmenity> mList;
    private AmenityAdapter mAdapter;

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";

    // TODO: Customize parameters
    public static AmenityDialogFragment newInstance(List<BusAmenity> itemCount) {
        final AmenityDialogFragment fragment = new AmenityDialogFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_COUNT, (Serializable) itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return inflater.inflate(R.layout.fragment_amenities_dialog, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mList = new ArrayList<>();
        mList = (List<BusAmenity>) getArguments().getSerializable(ARG_ITEM_COUNT);
        mAdapter = new AmenityAdapter(getActivity(), mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), R.drawable.divider));
        mRecyclerView.setAdapter(mAdapter);


    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        final Fragment parent = getParentFragment();
//        if (parent != null) {
//            mLogoutListener = (LogoutListener) parent;
//        } else {
//            mLogoutListener = (LogoutListener) context;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        mLogoutListener = null;
//        super.onDetach();
//    }
//
//    public interface LogoutListener {
//        void isLogoutClicked(boolean bool);
//    }


}

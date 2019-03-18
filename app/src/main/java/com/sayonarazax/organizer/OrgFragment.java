package com.sayonarazax.organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class OrgFragment extends Fragment {
    private static final String ARG_ORG_ID = "org_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME= "DialogTime" ;

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Org mOrg;
    private EditText mTitleField;
    private EditText mDetailsField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    public static OrgFragment newInstance(UUID orgId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORG_ID, orgId);
        OrgFragment fragment = new OrgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID orgId = (UUID) getArguments().getSerializable(ARG_ORG_ID);
        mOrg = OrgLab.get(getActivity()).getOrg(orgId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_org, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_org_action:
                UUID crimeId = mOrg.getId();
                OrgLab.get(getActivity()).deleteOrg(crimeId);

                Toast.makeText(getActivity(), R.string.toast_delete_org, Toast.LENGTH_SHORT).show();
                getActivity().finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        OrgLab.get(getActivity())
                .updateOrg(mOrg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_org, container, false);

        mTitleField = (EditText) v.findViewById(R.id.org_title);
        mTitleField.setText(mOrg.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
// Здесь намеренно оставлено пустое место
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mOrg.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
// И здесь тоже
            }
        });

        mDetailsField = (EditText) v.findViewById(R.id.org_details);
        mDetailsField.setText(mOrg.getDetails());
        mDetailsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
// Здесь намеренно оставлено пустое место
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mOrg.setDetails(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
// И здесь тоже
            }
        });

        mDateButton = (Button) v.findViewById(R.id.org_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mOrg.getDate());
                dialog.setTargetFragment(OrgFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button) v.findViewById(R.id.org_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mOrg.getDate());
                dialog.setTargetFragment(OrgFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.org_solved);
        mSolvedCheckBox.setChecked(mOrg.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mOrg.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        mOrg.setDate(date);

        switch (requestCode) {
            case REQUEST_DATE:
                updateDate();
                break;
            case REQUEST_TIME:
                updateTime();
                break;
        }
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, yyyy-MM-dd", mOrg.getDate()));
    }
    private void updateTime() {
        mTimeButton.setText(DateFormat.format("kk:mm", mOrg.getDate()));
    }
}

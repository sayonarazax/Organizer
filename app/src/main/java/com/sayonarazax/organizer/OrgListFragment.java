package com.sayonarazax.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrgListFragment extends Fragment {
    private RecyclerView mOrgRecyclerView;
    private OrgAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_org, container, false);
        mOrgRecyclerView = view.findViewById(R.id.org_recycler_view);
        mOrgRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView fab = (ImageView) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Org org = new Org();
                OrgLab.get(getActivity()).addOrg(org);
                Intent intent = OrgPagerActivity
                        .newIntent(getActivity(), org.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_org:
                Org org = new Org();
                OrgLab.get(getActivity()).addOrg(org);
                Intent intent = OrgPagerActivity
                        .newIntent(getActivity(), org.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        OrgLab orgLab = OrgLab.get(getActivity());
        List<Org> orgs = orgLab.getOrgs();
        Collections.sort(orgs, new Comparator<Org>() {
            @Override
            public int compare(Org o1, Org o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        if (mAdapter == null) {
            mAdapter = new OrgAdapter(orgs);
            mOrgRecyclerView.setAdapter(mAdapter);
            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(mAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mOrgRecyclerView);
        } else {
            mAdapter.setOrgs(orgs);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class OrgHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Org mOrg;
        private ImageView mSolvedImageView;

        public OrgHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_org, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.org_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.org_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.org_solved);
        }

        public void bind(Org org) {
            mOrg = org;
            mTitleTextView.setText(mOrg.getTitle());
            mDateTextView.setText(DateFormat.format("EEEE, yyyy-MM-dd kk:mm", mOrg.getDate()));
            mSolvedImageView.setVisibility(org.isSolved() ? View.VISIBLE :
                    View.GONE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = OrgPagerActivity.newIntent(getActivity(), mOrg.
                    getId());
            startActivity(intent);
        }
    }

    private class OrgAdapter extends RecyclerView.Adapter<OrgHolder> implements ItemTouchHelperAdapter {
        private List<Org> mOrgs;

        public OrgAdapter(List<Org> orgs) {
            mOrgs = orgs;
        }

        @NonNull
        @Override
        public OrgHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new OrgHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull OrgHolder holder, int position) {
            Org org = mOrgs.get(position);
            holder.bind(org);
        }

        @Override
        public int getItemCount() {
            return mOrgs.size();
        }

        public void setOrgs(List<Org> orgs) {
            mOrgs = orgs;
        }

        @Override
        public void onItemDismiss(int position) {
            UUID crimeId = mOrgs.get(position).getId();
            mOrgs.remove(position);
            notifyItemRemoved(position);

            OrgLab.get(getActivity()).deleteOrg(crimeId);

            Toast.makeText(getActivity(), R.string.toast_delete_org, Toast.LENGTH_SHORT).show();
        }
    }

    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

            private final ItemTouchHelperAdapter mAdapter;

            public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
                mAdapter = adapter;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }

    }
}

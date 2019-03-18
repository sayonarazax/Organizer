package com.sayonarazax.organizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class OrgPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ORG_ID =
            "com.saynarazax.android.organizer.org_id";

    private ViewPager mViewPager;
    private List<Org> mOrgs;

    public static Intent newIntent(Context packageContext, UUID orgId) {
        Intent intent = new Intent(packageContext, OrgPagerActivity.class);
        intent.putExtra(EXTRA_ORG_ID, orgId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ORG_ID);

        mViewPager = (ViewPager) findViewById(R.id.org_view_pager);
        mOrgs = OrgLab.get(this).getOrgs();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Org org = mOrgs.get(position);
                return OrgFragment.newInstance(org.getId());
            }
            @Override
            public int getCount() {
                return mOrgs.size();
            }
        });

        for (int i = 0; i < mOrgs.size(); i++) {
            if (mOrgs.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

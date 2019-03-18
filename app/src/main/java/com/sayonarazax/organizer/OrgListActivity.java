package com.sayonarazax.organizer;

import android.support.v4.app.Fragment;

public class OrgListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new OrgListFragment();
    }
}

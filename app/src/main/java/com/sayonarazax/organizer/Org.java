package com.sayonarazax.organizer;

import java.util.Date;
import java.util.UUID;

public class Org {
    private UUID mId;
    private String mTitle;
    private String mDetails;
    private Date mDate;
    private boolean mSolved;

    public Org() {
        this(UUID.randomUUID());
    }

    public Org(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}

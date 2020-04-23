package com.example.tasktimer;

import java.io.Serializable;

class Task implements Serializable {
    public static final long serialVersionUID = 20161120L;

    private long m_Id;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    public Task(long m_id, String name, String description, int msortOrder) {
        this.m_Id = m_id;
        mName = name;
        mDescription = description;
        this.mSortOrder = msortOrder;
    }

    public long getId() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

    public void setId(long id) {
        this.m_Id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "m_Id=" + m_Id +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder +
                '}';
    }
}

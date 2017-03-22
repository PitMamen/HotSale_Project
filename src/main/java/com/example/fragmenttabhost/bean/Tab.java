package com.example.fragmenttabhost.bean;

/**
 * Created by Richie on 2017/3/14.
 */

public class Tab {
    private  int title;
    private  Class fragment;
    private  int icon;

    public Tab(int title, Class fragment, int icon) {
        this.title = title;
        this.fragment = fragment;
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

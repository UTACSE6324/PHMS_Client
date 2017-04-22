package com.cse6324.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cse6324.fragment.ArticleFragment;
import com.cse6324.fragment.NoteFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2017/4/20.
 */

public class ContentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Article","Note"};
    private Context context;
    private List<Fragment> list;

    public ContentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        this.list = new ArrayList<>();
        list.add(new ArticleFragment());
        list.add(new NoteFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

}

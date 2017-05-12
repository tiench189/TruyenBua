package com.td.truyenbuavd.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.singleton.MySingleton;

public class StoryFragmentAdapter extends FragmentPagerAdapter {

	// public int size = 1;

	public StoryFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		Fragment myFragment = new StoryFragment();
		Bundle mBundle = new Bundle();
		mBundle.putInt("index", pos);
		myFragment.setArguments(mBundle);
		return myFragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MySingleton.getInstance().listStory.size();

	}

}

package com.td.truyenbuavd.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.singleton.MySingleton;
import com.td.truyenbuavd.MainActivity;
import com.td.truyenbuavd.R;
import com.td.truyenbuavd.helper.DatabaseHelper;
import com.td.truyenbuavd.helper.TDHelper;
import com.td.truyenbuavd.model.TDStory;

public class StoryFragment extends Fragment implements TDHelper {

	private TDStory story;

	private int index = 0;

	private ImageView iv_story;

	private int width;

	private boolean isLoad = true;

	private ProgressBar pb;

	private Bitmap img_bm = null;

	private WebView wv;

	private RelativeLayout rlLeft, rlRight;

	private DatabaseHelper db;

	private SharedPreferences _preferences;

	private int currentCateID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHelper(getActivity());
		_preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
		currentCateID = _preferences.getInt(CURRENT_CATEGORY, 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_story, null);
		story = new TDStory();
		index = getArguments().getInt("index");

		story = MySingleton.getInstance().listStory.get(index);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		width = displaymetrics.widthPixels - 10;
		iv_story = (ImageView) view.findViewById(R.id.iv_story);
		pb = (ProgressBar) view.findViewById(R.id.pb);
		wv = (WebView) view.findViewById(R.id.wv);
		Log.i("IMG URL", story.imgUrl);
		String html = "<html><body><img src=\"" + TDHelper.HEADER_URL
				+ story.imgUrl + "\" width=\"100%\"\"/></body></html>";
		Log.i("IMG HTML", html);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pb.setVisibility(View.GONE);
				// Intent i = new Intent();
				// i.setAction(TDHelper.ACTION_FILTER);
				// i.putExtra("action", TDHelper.ACTION_LOAD_MORE);
				// getActivity().sendBroadcast(i);
			}
		});
		wv.loadData(html, "text/html", null);
		// TDCategory category =
		// MySingleton.getInstance().listCategory.get(MySingleton.getInstance().currentCate);
		// category.currentId = story.id;
		// db.updateCategory(category);
		rlLeft = (RelativeLayout) view.findViewById(R.id.rlLeft);
		rlRight = (RelativeLayout) view.findViewById(R.id.rlRight);
		rlLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (index > 0) {
					((MainActivity) getActivity()).mPager.setCurrentItem(
							index - 1, true);
				} else {
					Toast.makeText(getActivity(), "First Page",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		rlRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (index < MySingleton.getInstance().listStory.size() - 1) {
					((MainActivity) getActivity()).mPager.setCurrentItem(
							index + 1, true);
				} else {
					Toast.makeText(getActivity(), "Last Page",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}

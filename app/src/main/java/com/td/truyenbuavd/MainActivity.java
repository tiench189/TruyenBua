package com.td.truyenbuavd;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.singleton.MyApplication;
import com.singleton.MySingleton;
import com.startapp.android.publish.StartAppAd;
import com.td.truyenbuavd.adapter.ListStoryAdapter;
import com.td.truyenbuavd.fragment.CategoryFragment;
import com.td.truyenbuavd.fragment.StoryFragmentAdapter;
import com.td.truyenbuavd.helper.ConnectionDetector;
import com.td.truyenbuavd.helper.Hashids;
import com.td.truyenbuavd.helper.JSONParser;
import com.td.truyenbuavd.helper.TDHelper;
import com.td.truyenbuavd.model.TDStory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ly.count.android.sdk.Countly;

public class MainActivity extends FragmentActivity implements TDHelper {

	public SlidingMenu menu;

	private ImageView iv_menu;

	public ViewPager mPager;

	private StoryFragmentAdapter mAdapter;

	private ImageView ivShare;

	private MyApplication myApp;

	private MainReceiver receiver;

	private Spinner sp_story;

	private List<TDStory> listStory;

	private boolean isPageScroll = false;

	private ProgressDialog pDialog;

	// private DatabaseHelper db;
	private ConnectionDetector mConnect;
	public SharedPreferences _preferences;

	private Context mContext;

	// Start App
	private StartAppAd startAppAd = new StartAppAd(this);

	private final String DEV_ID = "107726156";

	private final String APP_ID = "207161117";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		mContext = this;
		// set up ads
		StartAppAd.init(this, DEV_ID, APP_ID);

		// /** Create Splash Ad **/
		// StartAppAd.showSplash(this, savedInstanceState, new SplashConfig()
		// .setTheme(Theme.OCEAN).setAppName(getString(R.string.app_name))
		// .setOrientation(Orientation.PORTRAIT));

		mConnect = new ConnectionDetector(this);
		// Countly
		Countly.sharedInstance().init(this, "http://thongke.cuatao.net",
				"d5f60b6588d13387848e13f70baa97fedcddb5d5");

		// set application
		myApp = (MyApplication) getApplication();
		myApp.customAppMethod();
		MySingleton.getInstance().customSingletonMethod();

		_preferences = getPreferences(MODE_PRIVATE);

		initMenu();

		initView();

		receiver = new MainReceiver();
		registerReceiver(receiver, new IntentFilter(TDHelper.ACTION_FILTER));
	}
	
	private void initView() {
		// Admob

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		mPager = (ViewPager) findViewById(R.id.pager);
		iv_menu = (ImageView) findViewById(R.id.iv_left_menu);
		iv_menu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
			}
		});

		sp_story = (Spinner) findViewById(R.id.sp_list_story);

		ivShare = (ImageView) findViewById(R.id.ivShare);

		ivShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Countly.sharedInstance().recordEvent("click_share_sum", 1, 0.99);
				HashMap<String, String> segmentation = new HashMap<String, String>();
				segmentation.put("product_code", "02GKTOAN2");

				Countly.sharedInstance().recordEvent("readbook", segmentation, 1, 10, 50);
				try {
					Hashids hashids = new Hashids("tdapp2014!@#$%^&*(");
					String id = hashids.encrypt(listStory.get(mPager
							.getCurrentItem()).id);
					shareTextUrl("http://truyenbua.app2014.net/?id=" + id);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		Log.d("CONNECTION", "" + mConnect.isConnectingToInternet());
		if (mConnect.isConnectingToInternet()) {
			new AsyntaskParser().execute();
		} else {
			showErrorConnect();
		}

	}

	/**
	 * set left menu
	 */
	private void initMenu() {

		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new CategoryFragment()).commit();
	}

	public class MainReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getExtras().containsKey("action")) {
				if (intent.getExtras().getString("action")
						.equals(TDHelper.ACTION_LOAD_MORE)) {
					// mAdapter.size++;
					// mAdapter.notifyDataSetChanged();
				} else if (intent.getExtras().getString("action")
						.equals(TDHelper.ACTION_CHANGE_CATEGORY)) {
					startAppAd.showAd();
					startAppAd.loadAd();
					listStory = new ArrayList<TDStory>();
					new AsyntaskParser().execute();
				} else if (intent.getExtras().getString("action")
						.equals(TDHelper.ACTION_CHANGE_STORY)) {

				}
			}
		}

	}

	public class AsyntaskParser extends AsyncTask<String, String, Boolean> {

		private int cateID = _preferences.getInt(CURRENT_CATEGORY, 1);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean sc = false;
			JSONParser jParser = new JSONParser();
			try {
				String strJson = jParser.getJSONFromUrl(
						"http://vl.cuatao.net/api/get/" + cateID + "/skip/" + 0
								+ "/limit/1000/from/0", "post", null);
				Log.v("JSON CATEGORY", strJson);
				listStory = new ArrayList<TDStory>();
				JSONObject jsonObject = new JSONObject(strJson);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					TDStory story = new TDStory();
					story.id = jsonArray.getJSONObject(i).getInt("id");
					story.name = jsonArray.getJSONObject(i).getString("name");
					story.categoryID = jsonArray.getJSONObject(i).getInt(
							"category_id");
					story.imgUrl = jsonArray.getJSONObject(i)
							.getString("image");
					listStory.add(story);
				}

				sc = true;
			} catch (JSONException e) {
				e.printStackTrace();
				sc = false;
			}
			return sc;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result) {
				MySingleton.getInstance().listStory = listStory;
				MySingleton.getInstance().firstID = _preferences.getInt(
						CURRENT_CHAP + cateID, 0);
				Log.d("CACHE CHAP",
						_preferences.getInt(CURRENT_CHAP + cateID, 0) + "");
				sp_story.setAdapter(new ListStoryAdapter(MainActivity.this,
						MySingleton.getInstance().listStory));

				mAdapter = new StoryFragmentAdapter(getSupportFragmentManager());
				mPager.setAdapter(mAdapter);
				// mPager.setCurrentItem(MySingleton.getInstance().firstID);
				isPageScroll = false;
				sp_story.setSelection(_preferences.getInt(
						CURRENT_CHAP + cateID, 0));
				mPager.setOnPageChangeListener(new OnPageChangeListener() {

					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						isPageScroll = true;
						sp_story.setSelection(arg0);
						_preferences.edit().putInt(CURRENT_CHAP + cateID, arg0)
								.commit();
						Log.v("CHANGE CHAP",
								_preferences.getInt(CURRENT_CHAP + cateID, 0)
										+ "");
					}

					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

				sp_story.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						Log.d("Spinner change item", "" + isPageScroll);
						if (!isPageScroll) {
							Log.d("Spiner change page",
									"aaaaaaaaaaaaaaaaaaaaaa");
							// sp_story.setSelection(pos);
							mPager.setCurrentItem(pos, false);
						}
						isPageScroll = false;
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
			} else {
				showErrorConnect();
			}

		}
	}

	private void shareTextUrl(String url) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		share.putExtra(Intent.EXTRA_SUBJECT, "Truyen Bua");
		share.putExtra(Intent.EXTRA_TEXT, "\n" + url);

		startActivity(Intent.createChooser(share, "Share link!"));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {

		}
		super.onDestroy();
	}

	private void showErrorConnect() {
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("Lỗi");
		alert.setMessage("Không thể kết nối");
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alert.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		Countly.sharedInstance().onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		Countly.sharedInstance().onStop();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		startAppAd.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startAppAd.onResume();
	}

	@Override
	public void onBackPressed() {
		startAppAd.onBackPressed();
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			finish();
		}
	}

}

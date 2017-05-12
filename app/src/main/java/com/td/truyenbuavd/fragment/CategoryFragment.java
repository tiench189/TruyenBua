package com.td.truyenbuavd.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.singleton.MySingleton;
import com.td.truyenbuavd.MainActivity;
import com.td.truyenbuavd.R;
import com.td.truyenbuavd.adapter.ListCategoryAdapter;
import com.td.truyenbuavd.helper.ConnectionDetector;
import com.td.truyenbuavd.helper.JSONParser;
import com.td.truyenbuavd.helper.TDHelper;
import com.td.truyenbuavd.model.TDCategory;

public class CategoryFragment extends Fragment implements TDHelper {
	private ConnectionDetector mConnect;

	private ListView lv_category;

	private List<TDCategory> listCategory;

	private ListCategoryAdapter listCategoryAdapter;

	public SharedPreferences _preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mConnect = new ConnectionDetector(getActivity());
		listCategory = new ArrayList<TDCategory>();
		_preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_category, null);
		lv_category = (ListView) view.findViewById(R.id.lv_category);
		if (mConnect.isConnectingToInternet()) {
			if (listCategory.size() == 0) {
				if (mConnect.isConnectingToInternet()) {
					new AsyntaskParser().execute();
				}
			}
		}
		lv_category.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				((MainActivity) getActivity()).menu.showContent();
				if (_preferences.getInt(CURRENT_CATEGORY, 1) != listCategory
						.get(pos).id) {
					_preferences.edit()
							.putInt(CURRENT_CATEGORY, listCategory.get(pos).id)
							.commit();
					Intent i = new Intent();
					i.setAction(TDHelper.ACTION_FILTER);
					i.putExtra("action", TDHelper.ACTION_CHANGE_CATEGORY);
					getActivity().sendBroadcast(i);
				}

			}
		});
		return view;
	}

	public class AsyntaskParser extends AsyncTask<String, String, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			boolean sc = false;
			JSONParser jParser = new JSONParser();
			try {
				String strJson = jParser.getJSONFromUrl(
						"http://vl.cuatao.net/api/list/skip/0/limit/20",
						"post", null);
				Log.v("JSON CATEGORY", strJson);
				JSONObject jsonObject = new JSONObject(strJson);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					TDCategory category = new TDCategory();
					category.id = jsonArray.getJSONObject(i).getInt("id");
					category.name = jsonArray.getJSONObject(i)
							.getString("name");
					listCategory.add(category);
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
			super.onPostExecute(result);
			if (result) {
				MySingleton.getInstance().listCategory = listCategory;
				listCategoryAdapter = new ListCategoryAdapter(getActivity(),
						listCategory);
				lv_category.setAdapter(listCategoryAdapter);
			} else {

			}
		}
	}

}

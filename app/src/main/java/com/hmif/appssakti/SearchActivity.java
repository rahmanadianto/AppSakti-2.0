package com.hmif.appssakti;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hmif.custom.IconTextAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private JSONArray jsonArray;
	private ListView resultList;
	private List<String> titles;
	private List<JSONObject> datas;
	private List<Integer> icons;
	private List<Integer> dataType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		resultList = (ListView) findViewById(R.id.search_result);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setBackgroundColor(getResources().getColor(R.color.hmif_color));
		setSupportActionBar(toolbar);
		setTitle(null);

		try {
			jsonArray = loadJSON(R.raw.oskm).getJSONArray("OSKM");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			try {
				titles = new ArrayList<>();
				datas = new ArrayList<>();
				icons = new ArrayList<>();
				dataType = new ArrayList<>();
				search(2, jsonArray, query);
				IconTextAdapter adapter = new IconTextAdapter(this, R.layout.text_icon_item2,
						titles, null, icons);
				resultList.setAdapter(adapter);
				resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MainActivity.searchObj = datas.get(position);
						MainActivity.searchType = dataType.get(position);
						SearchActivity.this.finish();
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			try {
				titles = new ArrayList<>();
				datas = new ArrayList<>();
				icons = new ArrayList<>();
				dataType = new ArrayList<>();
				search(2, jsonArray, query);
				IconTextAdapter adapter = new IconTextAdapter(this, R.layout.text_icon_item2,
						titles, null, icons);
				resultList.setAdapter(adapter);
				resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MainActivity.searchObj = datas.get(position);
						MainActivity.searchType = dataType.get(position);
						SearchActivity.this.finish();
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void search(int startIndex, JSONArray array, String query) throws JSONException {
		//mulai dari 2 karena 0 dan 1 hard code
		for(int i=startIndex; i<array.length(); i++){
			JSONObject obj = array.getJSONObject(i);
			if(hasSomeValue(obj, query.toLowerCase())){
//				System.out.println(obj.toString());
				datas.add(obj);
				if(obj.has("judul")){
					dataType.add(MainActivity.PLAIN_INFORMATION);
					titles.add(obj.getString("judul"));
				}else if(obj.has("nama lembaga")){
					dataType.add(MainActivity.INFORMATION);
					titles.add(obj.getString("nama lembaga"));
				}else if(obj.has("nama fakultas")){
					dataType.add(MainActivity.MENU_LIST);
					titles.add(obj.getString("nama fakultas"));
				}else if(obj.has("nama himpunan")){
					dataType.add(MainActivity.INFORMATION);
					titles.add(obj.getString("nama himpunan"));
				}else if(obj.has("kategori unit")){
					dataType.add(MainActivity.MENU_LIST);
					titles.add(obj.getString("nama unit"));
				}else if(obj.has("nama unit")){
					dataType.add(MainActivity.INFORMATION);
					titles.add(obj.getString("nama unit"));
				}
				if(obj.has("logo") && !obj.getString("logo").equals("-")){
					icons.add(getResources().getIdentifier(obj.getString("logo"),
							"drawable", getPackageName()));
				}else if(obj.has("header foto") && !obj.getString("header foto")
						.equals("-")){
					icons.add(getResources().getIdentifier(obj.getString("header " +
							"foto"), "drawable", getPackageName()));
				}else{
					icons.add(null);
				}
			}

		}
	}

	private boolean hasSomeValue(JSONObject obj, String str) throws JSONException {
		Iterator<String> keys = obj.keys();
		while(keys.hasNext()){
			String key = keys.next();
			if(obj.get(key) instanceof String){
				String test = (String)obj.get(key);
				if(test.toLowerCase().contains(str)){
//					System.out.println(test);
					return true;
				}
			}else if(obj.get(key) instanceof JSONArray){
				search(0, (JSONArray)obj.get(key), str);
			}else if(obj.get(key) instanceof JSONObject){
				if(hasSomeValue((JSONObject)obj.get(key), str)){ return true; }
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_search, menu);
		MenuItem menuItem = menu.findItem(R.id.action_search);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		restoreActionBar();
		return true;
	}

	public void restoreActionBar() {
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setTitle(mTitle);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		System.out.println(id);
		if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private JSONObject loadJSON(int resId) throws IOException, JSONException {
		String line;
		StringBuilder result = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources()
				.openRawResource(resId)));
		while((line = reader.readLine()) != null){
			result.append(line);
		}
		return new JSONObject(result.toString());
	}
}

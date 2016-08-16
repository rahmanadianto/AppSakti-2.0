package com.hmif.appssakti;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
	private TextView noResult;
	private List<String> titles;
	private List<JSONObject> data;
	private List<String> icons;
	private List<Integer> dataType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		resultList = (ListView) findViewById(R.id.search_result);
		noResult = (TextView) findViewById(R.id.no_result);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setBackgroundColor(getResources().getColor(R.color.hmif_color));
		setSupportActionBar(toolbar);
		setTitle(null);

		noResult.setVisibility(View.GONE);
		resultList.setVisibility(View.GONE);

		titles = new ArrayList<>();
		data = new ArrayList<>();
		icons = new ArrayList<>();
		dataType = new ArrayList<>();

		Intent intent = getIntent();
		new JSONTask().execute(intent);
	}

	private void search(int startIndex, JSONArray array, String query) throws JSONException {

		for(int i=startIndex; i<array.length(); i++){
			JSONObject obj = array.getJSONObject(i);
			boolean shouldAddData = false;
			if(hasSomeValue(obj, query.toLowerCase())){

				if(obj.has("pages")) {
					dataType.add(MainActivity.PAGES);
					titles.add(obj.getString("judul"));
					shouldAddData = true;
				}
				else if(obj.has("gedung")) {
					dataType.add(MainActivity.MENU_LIST);
					titles.add(obj.getString("gedung"));
					shouldAddData = true;
				}
				else if(obj.has("daftar_makanan")) {
					dataType.add(MainActivity.MENU_LIST);
					titles.add(obj.getString("nama"));
					shouldAddData = true;
				}
				else if(obj.has("nama lembaga")){
					dataType.add(MainActivity.INFORMATION);
					titles.add(obj.getString("nama lembaga"));
					shouldAddData = true;
				}
				else if(obj.has("nama fakultas")){
					dataType.add(MainActivity.MENU_LIST);
					titles.add(obj.getString("nama fakultas"));
					shouldAddData = true;
				}
				else if(obj.has("nama himpunan")){
					dataType.add(MainActivity.INFORMATION);
					titles.add(obj.getString("nama himpunan"));
					shouldAddData = true;
				}
				else if(obj.has("kategori unit")){
					dataType.add(MainActivity.MENU_LIST);
					titles.add(obj.getString("kategori unit"));
					shouldAddData = true;
				}
				else if(obj.has("nama unit")){
					dataType.add(MainActivity.INFORMATION);
					titles.add(obj.getString("nama unit"));
					shouldAddData = true;
				}

				if (shouldAddData) {
					data.add(obj);

					if(obj.has("logo") && !obj.getString("logo").equals("-")){
						icons.add(obj.getString("logo"));
					}
					else if(obj.has("header foto") && !obj.getString("header foto").equals("-")){
						icons.add(obj.getString("header foto"));
					}
					else if(obj.has("foto") && !obj.getString("foto").equals("-")){
						icons.add(obj.getString("foto"));
					}
					else {
						icons.add("apps_header_logo");
					}
				}

			}

		}
	}

	private boolean hasSomeValue(JSONObject obj, String str) throws JSONException {
		Iterator<String> keys = obj.keys();

		while(keys.hasNext()){

			String key = keys.next();

			if (key.equals("pages")) {
				JSONArray array = (JSONArray) obj.get(key);
				for (int i = 0; i < array.length(); i++) {
					if (hasSomeValue(array.getJSONObject(i), str)) {
						return true;
					}
				}
			}
			else if (key.equals("ruangan")) {
				JSONArray array = (JSONArray) obj.get(key);
				for (int i = 0; i < array.length(); i++) {
					if (array.getJSONObject(i).getString("nama").equals(str)) {
						return true;
					}
				}
			}
			else if (key.equals("daftar_makanan")) {
				JSONArray array = (JSONArray) obj.get(key);
				for (int i = 0; i < array.length(); i++) {
					String test = array.getJSONObject(i).getString("nama");
					if (test.toLowerCase().equals(str)) {
						return true;
					}
				}
			}
			else if (obj.get(key) instanceof String){
				String test = (String) obj.get(key);
				if(test.toLowerCase().contains(str)){
					return true;
				}
			}
			else if(obj.get(key) instanceof JSONArray){
				JSONArray array = (JSONArray) obj.get(key);

				if (array.get(0) instanceof JSONObject) {
					search(0, array, str);
				}
			}
			else if(obj.get(key) instanceof JSONObject){
				if(hasSomeValue((JSONObject)obj.get(key), str)){
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_search, menu);
		//MenuItem menuItem = menu.findItem(R.id.action_search);
		//SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

		// Assumes current activity is the searchable activity
		//searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		//searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		restoreActionBar();
		return true;
	}

	public void restoreActionBar() {
		//ActionBar actionBar = getSupportActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//actionBar.setDisplayShowTitleEnabled(true);
		//actionBar.setTitle(mTitle);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

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

	private class JSONTask extends AsyncTask<Intent, Void, Void> {

		@Override
		protected Void doInBackground(Intent... params) {

			Intent intent = params[0];

			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

				try {
					jsonArray = loadJSON(R.raw.data).getJSONArray("DATA");

					if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
						String query = intent.getStringExtra(SearchManager.QUERY);

						search(2, jsonArray, query);
					}

				} catch (JSONException | IOException e) {
					//e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if (data.size() > 0) {
				noResult.setVisibility(View.GONE);
				resultList.setVisibility(View.VISIBLE);

				IconTextAdapter adapter = new IconTextAdapter(getApplicationContext(), R.layout.text_icon_item_search,
						titles, null, icons, true);

				resultList.setAdapter(adapter);
				resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MainActivity.searchObj = data.get(position);
						MainActivity.searchType = dataType.get(position);
						SearchActivity.this.finish();
					}
				});
			}
			else {
				noResult.setVisibility(View.VISIBLE);
			}

		}
	}
}

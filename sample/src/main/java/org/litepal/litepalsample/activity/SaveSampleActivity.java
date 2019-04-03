/*
 * Copyright (C)  Tony Green, Litepal Framework Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.litepal.litepalsample.activity;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.litepalsample.R;
import org.litepal.litepalsample.adapter.DataArrayAdapter;
import org.litepal.litepalsample.model.Singer;
import org.litepal.litepalsample.model.User;
import org.litepal.tablemanager.Connector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SaveSampleActivity extends Activity implements OnClickListener {

	private EditText mSingerNameEdit;

	private EditText mSingerAgeEdit;

	private EditText mSingerGenderEdit;

	private ProgressBar mProgressBar;

	private Button mSaveBtn;

	private ListView mDataListView;

	private DataArrayAdapter mAdapter;

	private List<List<String>> mList = new ArrayList<List<String>>();

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, SaveSampleActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_sample_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mSingerNameEdit = (EditText) findViewById(R.id.singer_name_edit);
		mSingerAgeEdit = (EditText) findViewById(R.id.singer_age_edit);
		mSingerGenderEdit = (EditText) findViewById(R.id.singer_gender_edit);
		mSaveBtn = (Button) findViewById(R.id.save_btn);
		mDataListView = (ListView) findViewById(R.id.data_list_view);
		mSaveBtn.setOnClickListener(this);
		mAdapter = new DataArrayAdapter(this, 0, mList);
		mDataListView.setAdapter(mAdapter);
		populateDataFromDB();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_btn:
			try {
				User user = new User();
				user.setAddress("setAddress");
				user.setIconUrl("setIconUrl");
				user.setGradeid(11111);
				user.setName("name");
				user.setPhone("135");
				user.setSex("nv");
				user.setToken("token");
				user.save();
				refreshListView(user.getId(), user.getName(), user.getPhone(),
						user.getGradeid(),user.getSex(),user.getAddress(),user.getIconUrl(),user.getToken());


			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, getString(R.string.error_param_is_not_valid),
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	private void populateDataFromDB() {
		mProgressBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				mList.clear();
				List<String> columnList = new ArrayList<String>();
				columnList.add("id");
				columnList.add("name");
				columnList.add("phone");
				columnList.add("gradeid");
				columnList.add("sex");
				columnList.add("address");
				columnList.add("iconUrl");
				columnList.add("token");
				mList.add(columnList);

				List<User>  users = DataSupport.findAll(User.class);

				Log.i("save","users="+users);
				for (User user:users){
					List<String> stringList = new ArrayList<String>();
					stringList.add(String.valueOf(user.getId()));
					stringList.add(user.getName());
					stringList.add(user.getPhone());
					stringList.add(String.valueOf(user.getGradeid()));
							stringList.add(user.getSex());
							stringList.add(user.getAddress());
							stringList.add(user.getIconUrl());
							stringList.add(user.getToken());
					mList.add(stringList);
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mProgressBar.setVisibility(View.GONE);
						mAdapter.notifyDataSetChanged();
					}
				});
//				Cursor cursor = null;
//				try {
//
//					cursor = Connector.getDatabase().rawQuery("select * from user order by id",
//							null);
//					if (cursor.moveToFirst()) {
//						do {
//							int id = cursor.getInt(cursor.getColumnIndex("id"));
//							String name = cursor.getString(cursor.getColumnIndex("name"));
//							String phone = cursor.getString(cursor.getColumnIndex("phone"));
//							int gradeid = cursor.getInt(cursor.getColumnIndex("gradeid"));
//							String sex = cursor.getString(cursor.getColumnIndex("sex"));
//							String address = cursor.getString(cursor.getColumnIndex("address"));
//							String iconUrl = cursor.getString(cursor.getColumnIndex("iconUrl"));
//							String token = cursor.getString(cursor.getColumnIndex("token"));
//							List<String> stringList = new ArrayList<String>();
//							stringList.add(String.valueOf(id));
//							stringList.add(name);
//							stringList.add(phone);
//							stringList.add(String.valueOf(gradeid));
//							stringList.add(sex);
//							stringList.add(address);
//							stringList.add(iconUrl);
//							stringList.add(token);
//							mList.add(stringList);
//						} while (cursor.moveToNext());
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (cursor != null) {
//						cursor.close();
//					}
//	runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					mProgressBar.setVisibility(View.GONE);
//					mAdapter.notifyDataSetChanged();
//				}
//			});
//				}
			}
		}).start();
	}

	private void refreshListView(int id, String name, String phone, int gradeid
			, String sex, String address, String iconUrl, String token) {
		List<String> stringList = new ArrayList<String>();
		stringList.add(String.valueOf(id));
		stringList.add(name);
		stringList.add(phone);
		stringList.add(String.valueOf(gradeid));
		stringList.add(sex);
		stringList.add(address);
		stringList.add(iconUrl);
		stringList.add(token);
		mList.add(stringList);
		mAdapter.notifyDataSetChanged();
		mDataListView.setSelection(mList.size());
	}

}
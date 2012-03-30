/**
 * 
 */
package com.junzhilu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.task.DiscussTask;

/**
 * @author eureka
 * 
 */
public class DiscussActivity extends ListActivity implements ICallBack {
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	private ListView listView;
	private String id_blog;
	private SimpleAdapter adapter;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.junzhilu.interfaces.ICallBack#doCallBack(java.util.Map)
	 */
	private View list_footer;
	private ProgressDialog progressDialog;
	private ImageView discuss_back;
	private ProgressBar list_footer_progressbar;

	public void doCallBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		if (map != null) {
			String name = (String) map.get("name");
			if (name.equalsIgnoreCase("DiscussTask")) {
				if (progressDialog != null) {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
				list_footer_progressbar.setVisibility(View.INVISIBLE);
				String sina_data = (String) map.get("sina_data");
				if (sina_data != null) {
					try {
						JSONArray sia_json = new JSONArray(sina_data);
						int size = sia_json.length();
						if (size == 0) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"没有评论", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							finish();
						}
						if (size < 20) {
							listView.removeFooterView(list_footer);
						}
						for (int i = 0; i < size; i++) {
							HashMap<String, Object> tempData = new HashMap<String, Object>();
							JSONObject jsonObject = (JSONObject) sia_json
									.opt(i);
							String created_at = jsonObject
									.getString("created_at");
							// idΪ΢��id jsonObject.getString("id");
							String id = jsonObject.getString("id");
							String text = jsonObject.getString("text");
							JSONObject user_json = jsonObject
									.getJSONObject("user");
							String screen_name = user_json
									.getString("screen_name");
							if (created_at != null) {
								try {
									Date date = new Date(created_at);
									DateFormat format = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss");
									created_at = format.format(date);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							created_at = "<font color='#4999bd'>" + created_at
									+ "</font>";
							screen_name = "<font color='#4999bd'>"
									+ screen_name + "</font>";

							tempData.put("created_at", created_at);
							tempData.put("text", text);
							tempData.put("screen_name", screen_name);
							data.add(tempData);
						}
						adapter.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.discuss);
		listView = (ListView) findViewById(android.R.id.list);
		discuss_back = (ImageView) findViewById(R.id.discuss_back);
		discuss_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiscussActivity.this.finish();
			}
		});

		LayoutInflater inflater = LayoutInflater.from(this);
		// View list_header = inflater.inflate(R.layout.list_header, null);
		// listView.addHeaderView(list_header);
		// list_header.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // data.clear();
		//
		// }
		// });
		list_footer = inflater.inflate(R.layout.list_footer, null);
		list_footer_progressbar = (ProgressBar) list_footer
				.findViewById(R.id.list_footer_progressbar);
		list_footer.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				list_footer_progressbar.setVisibility(View.VISIBLE);
				if (id_blog != null) {
					new DiscussTask(DiscussActivity.this).execute(id_blog);
				}
			}
		});

		// for (int i = 0; i < 10; i++) {
		// HashMap<String, Object> temp = new HashMap<String, Object>();
		// temp.put("text", "item"+i);
		// data.add(temp);
		// }

		adapter = new mySimpleAdapter(this, data, R.layout.discuss_item,
				new String[] { "screen_name", "created_at", "text" },
				new int[] { R.id.discuss_item_user, R.id.discuss_item_time,
						R.id.discuss_item_conment });
		Drawable divider_horizontal_timeline = getResources().getDrawable(
				R.drawable.divider_horizontal_timeline);
		listView.setDivider(divider_horizontal_timeline);
		listView.addFooterView(list_footer);
		listView.setAdapter(adapter);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		id_blog = bundle.getString("id_blog");
		if (id_blog != null) {
			progressDialog = new ProgressDialog(this);
			ShowProgressDialog();
			new DiscussTask(this).execute(id_blog);
		}
	}

	public void ShowProgressDialog() {
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// תȦ���
		// ��������ΪProgressDialog.STYLE_HORIZONTAL,ʹ��setMax,setProgress,incrementProgressBy�������ý��
		progressDialog.setMessage("	正在加载......");
		// ����ʹ��setButton2,setButton3����Ӹ�ఴť
		// progressDialog.setCancelable(false);//���ɱ����ؼ�ȡ��Ի���
		progressDialog.show();
	}
}

class mySimpleAdapter extends SimpleAdapter {

	public mySimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setViewText(TextView v, String text) {
		// TODO Auto-generated method stub
		v.setText(Html.fromHtml(text));
	}
}

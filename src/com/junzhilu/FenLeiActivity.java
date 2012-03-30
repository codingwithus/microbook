/**
 * 
 */
package com.junzhilu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junzhilu.adapter.GridAdapter;
import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.task.FriendIdsTask;
import com.junzhilu.task.InfoByIdTask;

/**
 * @author eureka
 * 
 */
public class FenLeiActivity extends Activity implements ICallBack {
	private String user_id;
	private GridView gridview;
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	private GridAdapter gridAdapter;
	private ProgressDialog progressDialog;
	private String title_txt;
	private Button fenlei_lookall;
	ArrayList<String> ids = new ArrayList<String>();
	// private ProgressBar fenlei_progressbar;
	private static final int requestCode_FlipBlogActivity = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fenlei);
		fenlei_lookall = (Button) findViewById(R.id.fenlei_lookall);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		user_id = bundle.getString("user_id");
		title_txt = bundle.getString("title_txt");
		if (title_txt != null) {
			TextView fenlei_txt = (TextView) findViewById(R.id.fenlei_txt);
			fenlei_txt.setText(title_txt);
		}
		// user_id = "2002023045";
		progressDialog = new ProgressDialog(this);
		ShowProgressDialog();
		// fenlei_progressbar = (ProgressBar)
		// findViewById(R.id.fenlei_progressbar);
		// fenlei_progressbar.setVisibility(View.VISIBLE);
		if (user_id != null) {
			new FriendIdsTask(this).execute(user_id);
		} else {
			finish();
		}
		// HashMap<String, Object> hh = new HashMap<String, Object>();
		// hh.put("nick_name", "XXXX");
		// hh.put("profile_image_url",
		// "http://tp4.sinaimg.cn/2080919643/50/5597549236/1");
		// data.add(hh);

		gridview = (GridView) findViewById(R.id.fenlei_grid);
		gridAdapter = new GridAdapter(this, gridview, data,
				R.layout.comicgriditem, new String[] { "nick_name",
						"profile_image_url" }, new int[] { R.id.ItemText,
						R.id.ItemImage });
		gridview.setAdapter(gridAdapter);
		RelativeLayout fenlei_title = (RelativeLayout) findViewById(R.id.fenlei_title);
		fenlei_title.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				gridview.setSelection(0);
			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, Object> itemData = data.get(position);
				String ItemId = (String) itemData.get("id");
				String ItemText = (String) itemData.get("nick_name");
				if (ItemId != null) {
					Intent intent = new Intent(FenLeiActivity.this,
							FlipBlogActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", ItemId);
					bundle.putString("screen_name", ItemText);
					bundle.putInt("position", position);
					intent.putExtras(bundle);
					startActivityForResult(intent, requestCode_FlipBlogActivity);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// switch (requestCode) {
		// case requestCode_FlipBlogActivity:
		// if ((this.data.size() -1) == resultCode) {
		// Toast toast = Toast.makeText(getApplicationContext(),
		// "�����Ѿ������һ���ˣ�", Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
		// }else {
		// HashMap<String, Object> itemData = this.data.get(resultCode+1);
		// String ItemId = (String) itemData.get("id");
		// String ItemText = (String) itemData.get("nick_name");
		// if (ItemId != null) {
		// Intent intent = new Intent(FenLeiActivity.this,
		// FlipBlogActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putString("id", ItemId);
		// bundle.putString("screen_name", ItemText);
		// bundle.putInt("position", resultCode+1);
		// intent.putExtras(bundle);
		// startActivityForResult(intent, requestCode_FlipBlogActivity);
		// }
		// }
		//
		// break;
		//
		// default:
		// break;
		// }
	}

	public void ShowProgressDialog() {
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// תȦ���
		// ��������ΪProgressDialog.STYLE_HORIZONTAL,ʹ��setMax,setProgress,incrementProgressBy�������ý��
		progressDialog.setMessage("	正在加载......");
		// ����ʹ��setButton2,setButton3����Ӹ�ఴť
		// progressDialog.setCancelable(false);//���ɱ����ؼ�ȡ��Ի���
		progressDialog.show();
	}

	public void doCallBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		if (map != null) {
			String name = (String) map.get("name");
			if (name.equalsIgnoreCase("FriendIdsTask")) {
				String err = (String) map.get("err");
				if (err.equalsIgnoreCase("0")) {
					String sina_data = (String) map.get("sina_data");
					if (sina_data != null) {
						try {
							JSONObject sina_json = new JSONObject(sina_data);
							JSONArray sina_arr = sina_json.getJSONArray("ids");
							for (int i = 0; i < sina_arr.length(); i++) {
								String id = sina_arr.get(i).toString();
								if (id != null) {
									new InfoByIdTask(this).execute(id);
									ids.add(id);
								}
							}
							fenlei_lookall
									.setOnClickListener(new OnClickListener() {

										public void onClick(View v) {
											// TODO Auto-generated method stub
											Intent intent = new Intent(
													FenLeiActivity.this,
													FlipBlog2Activity.class);
											intent.putExtra("ids", ids);
											startActivity(intent);
										}
									});
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			} else if (name.equalsIgnoreCase("InfoByIdTask")) {

				String err = (String) map.get("err");
				if (err.equalsIgnoreCase("0")) {
					String sina_data = (String) map.get("sina_data");
					if (sina_data != null) {
						try {
							JSONObject json_data = new JSONObject(sina_data);
							String nick_name = json_data.getString("name");
							String id = json_data.getString("id");
							String profile_image_url = json_data
									.getString("profile_image_url");
							HashMap<String, Object> tempData = new HashMap<String, Object>();
							tempData.put("nick_name", nick_name);
							tempData.put("id", id);
							tempData.put("profile_image_url", profile_image_url);
							data.add(tempData);
							gridAdapter.notifyDataSetChanged();
							if (progressDialog != null) {
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
							}
							// fenlei_progressbar.setVisibility(View.INVISIBLE);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		}
	}

}

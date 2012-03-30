package com.junzhilu;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.LinearLayout;

import com.junzhilu.adapter.ImageAdapter;
import com.junzhilu.util.DataCenter;

public class DMk extends ActivityGroup {
	private Gallery main_gallery;
	private LinearLayout pageContainer;
	private Intent[] intents;
	private Window[] subPageView;
	private ArrayList<String> typeId;
	private ArrayList<String> typeName;
	private SharedPreferences settings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dmk);
		settings = getSharedPreferences("microbook", 0);

		// ��ݿ��ʼ��
		DataCenter.GetInstance().SetContext(getApplicationContext());
		DataCenter.GetInstance().OpenGlobalDatabase();

		typeId = new ArrayList<String>();
		typeName = new ArrayList<String>();

		typeId.add("2257737451");
		typeName.add("腐世界");

		typeId.add("2258390507");
		typeName.add("治愈堂");

		typeId.add("2260958503");
		typeName.add("新番速递");

		typeId.add("2260783995");
		typeName.add("爆笑联盟");

		typeId.add("2259453461");
		typeName.add("阳光宅男");

		typeId.add("2262556711");
		typeName.add("萌图馆");

		typeId.add("2262202543");
		typeName.add("周边沙龙");

		typeId.add("2261218235");
		typeName.add("COS基地");

		pageContainer = (LinearLayout) findViewById(R.id.main_layout);

		main_gallery = (Gallery) findViewById(R.id.main_gallery);
		main_gallery.setCallbackDuringFling(false);
		main_gallery.setSpacing(15);
		main_gallery.setUnselectedAlpha(0.5f);
		main_gallery.setAdapter(new ImageAdapter(this, typeName));
		main_gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SwitchPage(position);
				Log.i("main_gallery", "onItemSelected");
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				Log.i("main_gallery", "onNothingSelected");
			}
		});
		main_gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SwitchPage(position);
			}
		});

		intents = new Intent[typeId.size()];
		subPageView = new Window[typeId.size()];
		main_gallery.setSelection(Integer.MAX_VALUE / 2);
	}

	private void SwitchPage(int id) {
		// TODO Auto-generated method stub
		id = id % typeId.size();
		pageContainer.removeAllViews();// ������������������е�View
		Window pageView = null;
		pageView = getPageView(id);
		if (pageView != null) {
			// װ����ҳ��View��LinearLayout��������
			pageContainer.addView(pageView.getDecorView(),
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}

	}

	private Window getPageView(int pageID) {
		// TODO Auto-generated method stub
		if (intents[pageID] == null || subPageView[pageID] == null) {
			intents[pageID] = new Intent(DMk.this, FenLeiActivity.class);
			intents[pageID].putExtra("user_id", typeId.get(pageID));
			intents[pageID].putExtra("title_txt", typeName.get(pageID));
			subPageView[pageID] = getLocalActivityManager().startActivity(
					"FenLeiActivity" + pageID, intents[pageID]);
		}

		return subPageView[pageID];
	}

	//
	public static final int MENU_ABOUT = Menu.FIRST;
	public static final int MENU_CHANGEACCOUNT = Menu.FIRST + 2;
	public static final int MENU_EXIT = Menu.FIRST + 3;
	public static final int MENU_SETTING = Menu.FIRST + 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ABOUT, 0, "关于");
		menu.add(0, MENU_CHANGEACCOUNT, 0, "切换账号");
		menu.add(0, MENU_EXIT, 0, "退出");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ABOUT:
			new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon)
					.setTitle("关于").setMessage(R.string.aboutus).create()
					.show();
			break;
		case MENU_CHANGEACCOUNT:
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle("切换账号")
					.setMessage("确定要删除当前账号、重新登录？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked OK so do some stuff */
									SharedPreferences.Editor editor = settings
											.edit();
									editor.clear();
									editor.commit();
									DataCenter.GetInstance().SetBExit(true);
									Intent intent11 = new Intent(DMk.this,
											CoverActivity.class);
									startActivity(intent11);
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
									dialog.dismiss();
								}
							}).create().show();
			break;
		case MENU_EXIT:
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle("退出")
					.setMessage("确定退出？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked OK so do some stuff */
									DMk.this.finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
									dialog.dismiss();
								}
							}).create().show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
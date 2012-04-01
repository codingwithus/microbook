/**
 * 
 */
package com.junzhilu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * @author chenwei10
 *
 */
public class PageListActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.page_list);
		LinearLayout page_1 = (LinearLayout)findViewById(R.id.page_1);
		page_1.setOnClickListener(this);
		LinearLayout page_2 = (LinearLayout)findViewById(R.id.page_2);
		page_2.setOnClickListener(this);
		LinearLayout page_3 = (LinearLayout)findViewById(R.id.page_3);
		page_3.setOnClickListener(this);
		LinearLayout page_4 = (LinearLayout)findViewById(R.id.page_4);
		page_4.setOnClickListener(this);
		LinearLayout page_5 = (LinearLayout)findViewById(R.id.page_5);
		page_5.setOnClickListener(this);
		LinearLayout page_6 = (LinearLayout)findViewById(R.id.page_6);
		page_6.setOnClickListener(this);
		LinearLayout page_7 = (LinearLayout)findViewById(R.id.page_7);
		page_7.setOnClickListener(this);
		LinearLayout page_8 = (LinearLayout)findViewById(R.id.page_8);
		page_8.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, FenLeiActivity.class);
		switch (v.getId()) {
		case R.id.page_1:
			intent.putExtra("user_id", "2257737451");
			intent.putExtra("title_txt", "腐世界");
			break;
		case R.id.page_2:
			intent.putExtra("user_id", "2258390507");
			intent.putExtra("title_txt", "治愈堂");
			break;
		case R.id.page_3:
			intent.putExtra("user_id", "2260958503");
			intent.putExtra("title_txt", "新番速递");
			break;
		case R.id.page_4:
			intent.putExtra("user_id", "2260783995");
			intent.putExtra("title_txt", "爆笑联盟");
			break;
		case R.id.page_5:
			intent.putExtra("user_id", "2259453461");
			intent.putExtra("title_txt", "阳光宅男");
			break;
		case R.id.page_6:
			intent.putExtra("user_id", "2262556711");
			intent.putExtra("title_txt", "萌图馆");
			break;
		case R.id.page_7:
			intent.putExtra("user_id", "2262202543");
			intent.putExtra("title_txt", "周边沙龙");
			break;
		case R.id.page_8:
			intent.putExtra("user_id", "2261218235");
			intent.putExtra("title_txt", "COS基地");
			break;

		default:
			break;
		}
		startActivity(intent);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.alert_dialog_icon)
			.setTitle("退出")
			.setMessage("确定退出？")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							/* User clicked OK so do some stuff */
							PageListActivity.this.finish();
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
		}
		return true;
	}
}

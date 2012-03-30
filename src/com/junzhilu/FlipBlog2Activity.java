/**
 * 
 */
package com.junzhilu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.layout.FlipLayout;
import com.junzhilu.task.AttentionTask;
import com.junzhilu.task.BlogListTask;
import com.junzhilu.task.ImageDownloadTask;
import com.junzhilu.util.Util;
import com.junzhilu.util.zoomimage.ImageZoomActivity;

/**
 * @author eureka
 * 
 */
public class FlipBlog2Activity extends Activity implements ICallBack,
		OnGestureListener {

	private FlipLayout viewFlip;
	private Button flip_addbtn;
	private ImageView flip_back;
	private TextView flip_title_txt;
	private GestureDetector detector;
	private ProgressDialog progressDialog;
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	private long since_id = 0;
	private int page = 1;
	private Boolean bLooding = false;
	private int position = 0;
	private ArrayList<String> ids;
	private SharedPreferences settings;
	private boolean bShowTips = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.junzhilu.interfaces.ICallBack#doCallBack(java.util.Map)
	 */
	@Override
	public void doCallBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		if (map != null) {
			String name = (String) map.get("name");
			if (name.equalsIgnoreCase("FriendshipTask")) {
				Boolean bfollowing = (Boolean) map.get("bfollowing");
				if (bfollowing) {
					flip_addbtn.setVisibility(View.INVISIBLE);
				} else {
					flip_addbtn.setVisibility(View.VISIBLE);
				}
			} else if (name.equalsIgnoreCase("BlogListTask")) {
				ArrayList<HashMap<String, Object>> dataTemp = (ArrayList<HashMap<String, Object>>) map
						.get("data");
				int size = dataTemp.size();
				if (progressDialog != null) {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
				if (bShowTips) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"���һ������Կ�Ŷ��˦��Ҳ�����л���һ���Σ�",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					bShowTips = false;
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("bShowTips", bShowTips);
					editor.commit();
				}
				if (map.get("data") != null) {
					// ˢ�º͸��
					if (((String) map.get("type")).equalsIgnoreCase("refresh")) {

					} else if (((String) map.get("type"))
							.equalsIgnoreCase("more")) {
						page++;
						if (dataTemp.isEmpty()) {
							Toast toast = Toast.makeText(
									getApplicationContext(), "�Ѿ������һ���ˣ�",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							bLooding = true;
						} else {
							// if ((viewFlip.getChildCount() - 1) !=
							// viewFlip.getDisplayedChild()) {
							// viewFlip.setDisplayedChild(viewFlip.getDisplayedChild());
							// }
							for (int i = 1; i < size; i++) {
								data.add(dataTemp.get(i));
								viewFlip.addView(addFlipView(dataTemp.get(i)));
							}
							bLooding = false;

						}
					} else if (((String) map.get("type"))
							.equalsIgnoreCase("first")) {
						page++;
						if (dataTemp.isEmpty()) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"����˺Ż�û��΢��O(��_��)O~��",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							finish();
						} else {
							for (int i = 1; i < size; i++) {
								data.add(dataTemp.get(i));
								viewFlip.addView(addFlipView(dataTemp.get(i)));
							}
							// for (int i = 1; i < data.size(); i++) {
							// HashMap<String, Object> item = data.get(i);
							// viewFlip.addView(addFlipView(item));
							// }
						}
					}
				} else {

				}

			} else if (name.equalsIgnoreCase("AttentionTask")) {
				String err = (String) map.get("err");
				if (err.equalsIgnoreCase("0")) {
					String sina_data = (String) map.get("sina_data");
					if (sina_data != null) {
						Util.ShowTips(getApplicationContext(), "关注成功");
						flip_addbtn.setVisibility(View.INVISIBLE);
					}
				} else {
					Util.ShowTips(getApplicationContext(), err);
					flip_addbtn.setVisibility(View.VISIBLE);
				}
			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.flip);
		settings = getSharedPreferences("microbook", 0);
		bShowTips = settings.getBoolean("bShowTips", true);
		detector = new GestureDetector(this);
		viewFlip = (FlipLayout) findViewById(R.id.flip_view);
		viewFlip.SetGestureDetector(detector);

		flip_addbtn = (Button) findViewById(R.id.flip_addbtn);
		flip_back = (ImageView) findViewById(R.id.flip_back);
		flip_title_txt = (TextView) findViewById(R.id.flip_title_txt);
		flip_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FlipBlog2Activity.this.finish();
			}
		});
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		ids = (ArrayList<String>) bundle.get("ids");
		new BlogListTask(this).execute("first", ids.get(position), page);
		flip_addbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AttentionTask(FlipBlog2Activity.this).execute(ids
						.get(position));
				v.setVisibility(View.INVISIBLE);
			}
		});
		progressDialog = Util.ShowProgressDialog(this, null);
		progressDialog.show();

	}

	private View addFlipView(HashMap<String, Object> item) {

		LayoutInflater inflater = LayoutInflater.from(this);
		View flipview = inflater.inflate(R.layout.flipview, null);

		ImageView flip_user_icon = (ImageView) flipview
				.findViewById(R.id.flip_user_icon);
		String profile_image_url = (String) item.get("profile_image_url");
		if (profile_image_url != null) {
			flip_user_icon.setTag(profile_image_url);
			new ImageDownloadTask().execute(profile_image_url, flip_user_icon);
		}
		ImageView flip_pic = (ImageView) flipview.findViewById(R.id.flip_pic);
		String thumbnail_pic = (String) item.get("thumbnail_pic");
		final String bmiddle_pic = (String) item.get("bmiddle_pic");
		if (thumbnail_pic != null) {
			flip_pic.setTag(thumbnail_pic);
			new ImageDownloadTask().execute(thumbnail_pic, flip_pic);
			if (bmiddle_pic != null) {
				flip_pic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (bmiddle_pic != null) {
							Intent intent = new Intent(FlipBlog2Activity.this,
									ImageZoomActivity.class);
							intent.putExtra("url", bmiddle_pic);
							startActivity(intent);
						}
					}
				});
			}
		}
		TextView flip_user_name = (TextView) flipview
				.findViewById(R.id.flip_user_name);
		String screen_name = (String) item.get("screen_name");
		if (screen_name != null) {
			flip_user_name.setText(screen_name);
		}
		TextView flip_txt = (TextView) flipview.findViewById(R.id.flip_txt);
		String text = (String) item.get("text");
		if (text != null) {
			flip_txt.setText(Html.fromHtml(text));
		}

		Button flip_btn_pl = (Button) flipview.findViewById(R.id.flip_btn_pl);
		final String id = (String) item.get("id");
		if (id != null) {
			flip_btn_pl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(FlipBlog2Activity.this,
							DiscussActivity.class);
					intent.putExtra("id_blog", id);
					startActivity(intent);
				}
			});
		}
		Button flip_btn_nextgroup = (Button) flipview
				.findViewById(R.id.flip_btn_nextgroup);
		flip_btn_nextgroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewFlip.setDisplayedChild((viewFlip.getChildCount() - 1));
			}
		});

		return flipview;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
		// if(viewFlip.isInTouchMode()){
		// return detector.onTouchEvent(event);
		// } else{
		// return super.onTouchEvent(event);
		// }
		// return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 120) {
			viewFlip.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			viewFlip.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			// ���������
			if ((viewFlip.getChildCount() - 1) == viewFlip.getDisplayedChild()) {
				if (bLooding) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"���ڼ���...", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					if (page == 2) {
						new BlogListTask(this).execute("more",
								ids.get(position), page);
						page = 1;
						position++;
					} else {
						new BlogListTask(this).execute("more",
								ids.get(position), page);
					}
					bLooding = true;
					Toast toast = Toast.makeText(getApplicationContext(),
							"���ڼ���...", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} else {
				viewFlip.showNext();
			}
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			viewFlip.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			viewFlip.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			if (0 == viewFlip.getDisplayedChild()) {

			} else {
				viewFlip.showPrevious();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}

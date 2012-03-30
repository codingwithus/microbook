package com.junzhilu.util.zoomimage;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ZoomControls;

import com.junzhilu.R;
import com.junzhilu.interfaces.ICallBack;
import com.junzhilu.task.GetImageTask;
import com.junzhilu.util.ImageDownloader;

public class ImageZoomActivity extends Activity implements ICallBack {
	/** Called when the activity is first created. */
	private ProgressDialog progressDialog;
	private ImageZoomView mZoomView;
	private ZoomState mZoomState;
	private Bitmap mBitmap;
	private SimpleZoomListener mZoomListener;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mZoomView.setImage(mBitmap);
			mZoomState = new ZoomState();
			mZoomView.setZoomState(mZoomState);
			mZoomListener = new SimpleZoomListener();
			mZoomListener.setZoomState(mZoomState);
			mZoomView.setOnTouchListener(mZoomListener);
			resetZoomState();
		}
	};
	private String original_pic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zoom_image);
		mZoomView = (ImageZoomView) findViewById(R.id.zoomView);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// mBitmap = (Bitmap)bundle.get("BitmapImage");
		original_pic = (String) bundle.get("url");
		mBitmap = (Bitmap) ImageDownloader.getBitmapFromCacheOrDB(original_pic);
		if (mBitmap != null) {
			handler.sendEmptyMessage(0);
		} else {
			progressDialog = new ProgressDialog(this);
			ShowProgressDialog();
			new GetImageTask(this).execute(original_pic);
		}
		ZoomControls zoomCtrl = (ZoomControls) findViewById(R.id.zoomCtrl);
		zoomCtrl.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float z = mZoomState.getZoom() + 0.25f;
				mZoomState.setZoom(z);
				mZoomState.notifyObservers();
			}
		});
		zoomCtrl.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float z = mZoomState.getZoom() - 0.25f;
				mZoomState.setZoom(z);
				mZoomState.notifyObservers();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmap != null)
			mBitmap.recycle();
		// mZoomView.setOnTouchListener(null);
		// mZoomState.deleteObservers();
	}

	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}

	@Override
	public void doCallBack(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String err = (String) map.get("err");
		if (err.equalsIgnoreCase("0")) {
			if ((Bitmap) map.get("bmp") != null) {
				mBitmap = (Bitmap) map.get("bmp");
				handler.sendEmptyMessage(0);
			} else {
				finish();
			}
		} else {
			// new GetImageTask(this).execute(original_pic);
		}
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

	public void ShowProgressDialog() {
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// תȦ���
		// ��������ΪProgressDialog.STYLE_HORIZONTAL,ʹ��setMax,setProgress,incrementProgressBy�������ý��
		progressDialog.setMessage("	���ڼ���......");
		// ����ʹ��setButton2,setButton3����Ӹ�ఴť
		// progressDialog.setCancelable(false);//���ɱ����ؼ�ȡ��Ի���
		progressDialog.show();
	}
}
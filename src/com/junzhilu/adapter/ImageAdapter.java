/**
 * 
 */
package com.junzhilu.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.junzhilu.R;

/**
 * @author eureka
 * 
 */
public class ImageAdapter extends BaseAdapter {
	private Drawable btn_bottom_selector;
	private Context context;
	private ArrayList<String> typeName;
	private int screenWidth;

	public ImageAdapter(Context context, ArrayList<String> typeName) {
		this.context = context;
		this.typeName = typeName;
		btn_bottom_selector = context.getResources().getDrawable(
				R.drawable.btn_bottom_selector);
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Button button = new Button(this.context);
		button.setBackgroundDrawable(btn_bottom_selector);
		// ImageView image = new ImageView(this.context);
		// image.setBackgroundDrawable(btn_bottom_foc);
		// image.setScaleType(ImageView.ScaleType.FIT_XY);
		// image.setLayoutParams(new
		// Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));
		if (position < 0) {
			position = position + 8;
		}
		button.setText(typeName.get(position % typeName.size()));
		button.setTextSize(9);
		button.setGravity(Gravity.CENTER);
		return button;
	}

	// /* ���ݾ��������λ���� ����getScale����views�Ĵ�С(0.0f to 1.0f) */
	// public float getScale(boolean focused, int offset)
	// {
	// return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	// }

}

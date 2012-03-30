/**
 * 
 */
package com.junzhilu.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.junzhilu.task.ImageViewTask;
import com.junzhilu.util.ImageDownloader;

/**
 * @author eureka
 * 
 */
public class GridAdapter extends SimpleAdapter {

	private GridView gridview;

	public GridAdapter(Context context, GridView gridview,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		this.gridview = gridview;
	}

	@Override
	public void setViewImage(ImageView v, String value) {
		// TODO Auto-generated method stub
		if (value != null) {
			v.setTag(value);
			Bitmap bitmap = ImageDownloader.GetInstance().getBitmapFromCache(
					value);
			if (bitmap != null) {
				v.setImageBitmap(bitmap);
			} else {
				new ImageViewTask().execute(value, gridview);
			}
		}
	}

}

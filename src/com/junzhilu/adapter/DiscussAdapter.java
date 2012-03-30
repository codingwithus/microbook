/**
 * 
 */
package com.junzhilu.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author eureka
 * 
 */
public class DiscussAdapter extends SimpleAdapter {
	private Context context = null;
	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater mInflater;
	private ListView listView;

	public DiscussAdapter(Context context,
			ArrayList<HashMap<String, Object>> data, int resource,
			String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		this.mInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

}

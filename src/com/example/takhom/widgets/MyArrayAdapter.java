package com.example.takhom.widgets;

import java.util.List;

import com.example.takhom.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<ListObject> {
	private final Context context;
	private final List<ListObject> values;

	public MyArrayAdapter(Context context, List<ListObject> values) {
		super(context, R.layout.list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_item, parent, false);
		TextView text = (TextView) rowView.findViewById(R.id.text);
		TextView number = (TextView) rowView.findViewById(R.id.number);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		
		ListObject tmp = values.get(position);
		text.setText(tmp.text);
		text.setTextColor(tmp.color);
		number.setText(tmp.number);
		number.setTextColor(tmp.color);
		imageView.setImageResource(tmp.imageId);

		return rowView;
	}
}
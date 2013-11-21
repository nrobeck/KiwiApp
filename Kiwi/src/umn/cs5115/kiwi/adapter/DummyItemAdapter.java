package umn.cs5115.kiwi.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class DummyItemAdapter<T> extends ArrayAdapter<T> {
	private final T dummy;
	private final List<T> objects;
	
	private void init() {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	@Override
	public final T getItem(int position) {
		if (position == 0) {
			return dummy;
		} else {
			return objects.get(position - 1);
		}
	}

	@Override
	public final int getPosition(T item) {
		if (item == dummy) {
			return 0;
		} else if (objects.contains(item)) {
			return objects.indexOf(item) + 1;
		} else {
			return -1;
		}
	}

	public DummyItemAdapter(Context context, int textViewResourceId, List<T> objects, T dummy) {
		super(context, textViewResourceId, objects);
		if (dummy == null) {
			throw new NullPointerException("Dummy object must not be null!");
		}
		this.dummy = dummy;
		this.objects = objects;
		init();
	}
	
	public DummyItemAdapter(Context context, int textViewResourceId, T[] objects, T dummy) {
		this(context, textViewResourceId, Arrays.asList(objects), dummy);
	}
	
	public abstract Object getViewTag(T obj);
	
	public abstract String getViewText(int position, T obj, boolean isDummy);

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		TextView text = (TextView)v.findViewById(android.R.id.text1);
		if (position == 0) {
			text.setTextColor(Color.GRAY);
		} else {
			text.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
		}
		
		T object = getItem(position);
		text.setText(getViewText(position, object, position == 0));
		v.setTag(getViewTag(object));
		return v;
	}

	@Override
	public final View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = super.getDropDownView(position, convertView, parent);
		TextView text = (TextView)v.findViewById(android.R.id.text1);
		if (position == 0) {
			text.setTextColor(Color.GRAY);
		} else {
			text.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
		}

		text.setText(getViewText(position, getItem(position), position == 0));
		return v;
	}
}

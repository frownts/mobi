package com.join.mobi.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import com.join.android.app.common.R;

import java.util.ArrayList;

public class MySpinner extends Button {

	private Context context = null;
	private OnItemSelectedListener listener = null;
	private ArrayList<String> data = null;
	private SpinnerDropDownPopupWindow dropDown = null;


	public MySpinner(Context context) {
		this(context, null);
	}
	
	public MySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public MySpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init( Context context ){
		this.context = context;
		data = new ArrayList<String>();
		setOnClickListener( new SpinnerButtonOnClickListener() );
	}
	

	public void setOnItemSelectedListener( OnItemSelectedListener listener ){
		this.listener = listener;
	}
	

	public void setData( ArrayList<String> data ){
		this.data = data;
	}
	
	class SpinnerButtonOnClickListener implements OnClickListener {

		public void onClick(View v) {
			if(dropDown == null){
				dropDown = new SpinnerDropDownPopupWindow(context);
			}
			if(!dropDown.isShowing()){
				dropDown.showAsDropDown(MySpinner.this);
			}
		}
		
	}
	
	class SpinnerDropDownPopupWindow extends PopupWindow {
		
		private LayoutInflater inflater = null;
		
		private ListView listView = null;
		
		private SpinnerDropdownAdapter adapter = null;
		
		public SpinnerDropDownPopupWindow( Context context ){
			super(context);
			inflater = LayoutInflater.from(context);
			
			adapter = new SpinnerDropdownAdapter();
			
			View view = inflater.inflate(R.layout.my_spinner, null);
			listView = (ListView)view.findViewById(R.id.my_spinner_list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener( new SpinnerListOnItemClickListener() );
			
			setWidth(MySpinner.this.getLayoutParams().width);
			setHeight(LayoutParams.WRAP_CONTENT);

			setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
		}
		
		
		
		public void showAsDropDown(View view) {
			showAsDropDown(view, 0, 0);
			update();		//ˢ��
		}


		private final class SpinnerDropdownAdapter extends BaseAdapter {

			public int getCount() {
				return data.size();
			}

			public Object getItem(int position) {
				return data.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = inflater.inflate(R.layout.my_spinner_item, null);
					holder.txt = (TextView) convertView.findViewById(R.id.my_spinner_item_text);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.txt.setText(data.get(position));
				
				return convertView;
			}
			
		}
		

		private final class ViewHolder {
			TextView txt;
		}
		
		class SpinnerListOnItemClickListener implements OnItemClickListener {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView mTextView = (TextView) view.findViewById(R.id.my_spinner_item_text);
				String content = mTextView.getText().toString();
				MySpinner.this.setText(content);
				listener.onItemSelected(parent, view, position, id);
				SpinnerDropDownPopupWindow.this.dismiss();
			}
			
		}

	}

}
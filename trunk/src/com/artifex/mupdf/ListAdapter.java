package com.artifex.mupdf;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Context context;
		private List<Map<String, Object>> mData;
	 
		
		public ListAdapter(Context context)
		{
			this.context=context;
			this.mInflater=LayoutInflater.from(context);
		}
		
		public ListAdapter(Context context,List<Map<String, Object>> list)
		{
			this.mInflater=LayoutInflater.from(context);
			this.mData=list;//传递要显示的列表进来
			this.context=context;
		}
		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		//View中的setTag(Object)表示给View添加一个格外的数据，以后可以用getTag()将这个数据取出来。
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			/* Adapter的作用就是ListView界面与数据之间的桥梁，当列表里的每一项显示到页面时，
			 * 都会调用Adapter的getView方法返回一个View。如果每个view都用inflate的话，列表项数一多就会占用极大的系统资源。
			 * View中的setTag(Object)表示给View添加一个格外的数据（ViewHolder类），以后可以用getTag()将这个数据取出来。
			 * 这样做可以优化性能！*/
			ViewHolder holder = null;
			if (convertView == null) {		
				
			}else {	
				holder = (ViewHolder)convertView.getTag();
			}
			holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
			holder.title.setText((String)mData.get(position).get("title"));
			holder.info.setText((String)mData.get(position).get("info"));
			/*为什么还要加个StringHolder呢？将数据依附到button中，向button的OnClicklistener传参！*/
			StringHolder strholder=new StringHolder();
			strholder.title=(String)mData.get(position).get("title");
			strholder.info=(String)mData.get(position).get("info");
			strholder.id=(String) mData.get(position).get("id");
			holder.viewBtn.setTag(strholder);//setTag
	
			return convertView;
		}
		
		public final class ViewHolder{
			public ImageView img;
			public TextView title;
			public TextView info;
			public Button viewBtn;
		}
		
		public final class StringHolder{
			public String title;
			public String info;
			public String id;
		}
}

package com.artifex.mupdf;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MainActivity extends Activity {

	private File mDirectory;
	private File cacheDirectory;
	private File imgBook;
	private File[] mFiles;
	private Utilities utility = new Utilities();
	private Button btnEdit;
	private Button btnList;
	private Button btnGrid;
	private boolean smodel = true;	//true : grid, false : list
	private ArrayList<HashMap<String, Object>> bookList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                        R.layout.titlebar);
        
        btnList = (Button)findViewById(R.id.btnList);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnGrid = (Button)findViewById(R.id.btnGrid);
		
		String storageState = Environment.getExternalStorageState();

		if (!Environment.MEDIA_MOUNTED.equals(storageState)
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.no_media_warning);
			builder.setMessage(R.string.no_media_hint);
			AlertDialog alert = builder.create();
			alert.setButton(AlertDialog.BUTTON_POSITIVE, "Dismiss",
					new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alert.show();
			return;
		}

		mDirectory = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		
		mFiles = mDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				if (name.toLowerCase().endsWith(".pdf"))
					return true;
				if (name.toLowerCase().endsWith(".xps"))
					return true;
				if (name.toLowerCase().endsWith(".cbz"))
					return true;
				return false;
			}

		});
		
		/* 搜索缓存文件夹，若不存在则新建缓存文件夹 */
		cacheDirectory = new File("/mnt/sdcard/ushelf_cache");
		if ( !cacheDirectory.exists() ) {
			cacheDirectory.mkdir();
		}
		
		bookList = new ArrayList<HashMap<String, Object>>();

		for (File f : mFiles) {
			String tmpName = f.getName().substring(0, f.getName().length() - 4);
			HashMap<String, Object> map = new HashMap<String, Object>();
			/* 对于每个文件，查找是否已经存在缩略图，若不存在，自动生成一个并缓存，若存在直接添加到ItemImage中 */
			imgBook = new File("/mnt/sdcard/ushelf_cache/"
					+ tmpName + "_thumb.png");
			if ( !imgBook.exists() ) {
				Log.i("file is not exist!", "thumbimage" );
				utility.saveThumbnail(tmpName ,0);
				
			}
	
			Bitmap bm = BitmapFactory.decodeFile("/mnt/sdcard/ushelf_cache/"
					+ tmpName + "_thumb.png");
			map.put("ItemImage", bm ); 
			map.put("ItemText", f.getName());
			bookList.add(map);
		}
		
		btnGrid.setOnClickListener(listernGrid);
		btnList.setOnClickListener(listernList);
	
		if ( smodel == true ) {
			showGrid();
		} else {
			showList();
		}
		
	}
	
	private void showGrid() {
		/* 表格显示 */
		GridView gridview = (GridView) findViewById(R.id.GridView);
		
		SimpleAdapter saItem = new SimpleAdapter(this, bookList, // 数据源
				R.layout.item, // xml实现
				new String[] { "ItemImage" }, new int[] { R.id.ItemImage });
		
		saItem.setViewBinder(new ViewBinder(){

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if ( (view instanceof ImageView) & (data instanceof Bitmap) ) {
					ImageView iv = (ImageView) view;
					Bitmap bm = (Bitmap) data;
					iv.setImageBitmap(bm);
					return true;
				}
				return false;
			}  
			
		});

		// 添加Item到网格中
		gridview.setAdapter(saItem);
		// 添加点击事件
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				Uri uri = Uri.parse(mFiles[position].getAbsolutePath());
				Intent intent = new Intent(MainActivity.this,
						MuPDFActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
			}
		});

	}
	
	private void showList() {
		
	}

	private void setGridView() {
		setContentView(R.layout.main);
	}
	
	private void setListView() {
		setContentView(R.layout.main_list);
	}
	
	private android.view.View.OnClickListener listernList = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setListView();
			btnGrid.setBackgroundResource(R.drawable.grid_on_30);
			btnList.setBackgroundResource(R.drawable.list_in_30);
		}
	};
	
	private android.view.View.OnClickListener listernGrid = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setGridView();
			btnGrid.setBackgroundResource(R.drawable.grid_in_30);
			btnList.setBackgroundResource(R.drawable.list_on_30);
		}
	};
	
	private android.view.View.OnClickListener listernEdit = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};
}

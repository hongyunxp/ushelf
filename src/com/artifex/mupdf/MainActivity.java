package com.artifex.mupdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private File mDirectory;
	private File cacheDirectory;
	private File imgBook;
	private File[] mFiles;
	private File[] cacheImgs;
	private Utilities utility;
	//private ImageButton imgBtnGrid;
	//private ImageButton imgBtnList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		
		//imgBtnGrid = (ImageButton)this.findViewById(R.id.grid_btn);
		//imgBtnList = (ImageButton)this.findViewById(R.id.list_btn);
		
		/*imgBtnGrid.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  displayToast("Clicked!");
			}
		});*/
		
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

		/* 表格显示 */
		GridView gridview = (GridView) findViewById(R.id.GridView);
		ArrayList<HashMap<String, Object>> bookList = new ArrayList<HashMap<String, Object>>();

		for (File f : mFiles) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			/* 对于每个文件，查找是否已经存在缩略图，若不存在，自动生成一个并缓存，若存在直接添加到ItemImage中 */
			imgBook = new File("/mnt/sdcard/ushelf_cache/"
					+ f.getName().substring(0, f.getName().length() - 4)
					+ ".png");
			if ( !imgBook.exists() ) {
				Log.i("file is not exist!", "thumimage" );
				
			}
			map.put("ItemImage", R.drawable.shadow);
			map.put("ItemText", f.getName());
			bookList.add(map);
		}

		SimpleAdapter saItem = new SimpleAdapter(this, bookList, // 数据源
				R.layout.item, // xml实现
				new String[] { "ItemImage" }, new int[] { R.id.ItemImage });
		// new String[]{"ItemImage","ItemText"}, //对应map的Key
		// new int[]{R.id.ItemImage,R.id.ItemText}); //对应R的Id

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
	
	 private void displayToast(String s)
     {
         Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
     }
}

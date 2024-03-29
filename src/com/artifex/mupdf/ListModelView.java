package com.artifex.mupdf;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ListModelView extends ListActivity {
	private File mDirectory;
	private File[] mFiles;
	private Button btnGrid;
	private Button btnList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

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
		List<String> fileNames = new ArrayList<String>();
		for (File f : mFiles)
			fileNames.add(f.getName());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.picker_entry, fileNames);
		setListAdapter(adapter);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);

		btnGrid = (Button) findViewById(R.id.btnGrid);
		btnList = (Button) findViewById(R.id.btnList);

		btnGrid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListModelView.this,
						MainActivity.class);
				btnGrid.setBackgroundResource(R.drawable.grid_in_30);
				btnList.setBackgroundResource(R.drawable.list_on_30);
				ListModelView.this.startActivity(intent);
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Uri uri = Uri.parse(mFiles[position].getAbsolutePath());
		Intent intent = new Intent(this, MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
	}
}

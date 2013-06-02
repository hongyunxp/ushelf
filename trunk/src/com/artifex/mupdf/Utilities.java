package com.artifex.mupdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utilities {

	private MuPDFCore core;
	private String mFileName;
	private File orgImage;
	private File thumbImage;
	private Bitmap orgBm;
	private Bitmap thumbBm;

	public void saveThumbnail(String filePath, int page) {

		core = openFile("/mnt/sdcard/Download/" + filePath + ".pdf");
		if (core == null) {
			Log.i("saveThumbnail", "open file failed!");
			return;
		}

		Log.i("saveThumbnail", "pagenum:" + core.countPages());
		orgBm = Bitmap.createBitmap(885, 1323, Bitmap.Config.ARGB_8888);
		core.drawPage(page, orgBm, 885, 1323, 0, 0, 885, 1323);

		orgImage = new File("/mnt/sdcard/ushelf_cache/" + filePath + ".png");

		/* 保存原始图片 */
		try {
			orgImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fOut = null;

		try {
			fOut = new FileOutputStream(orgImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		orgBm.compress(Bitmap.CompressFormat.PNG, 100, fOut);

		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		thumbImage = new File("/mnt/sdcard/ushelf_cache/" + filePath
				+ "_thumb.png");
		/* 保存缩略图 */
		try {
			thumbImage.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream fOutT = null;

		try {
			fOutT = new FileOutputStream(thumbImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		thumbBm = decodeFile(orgImage);
		thumbBm.compress(Bitmap.CompressFormat.PNG, 100, fOutT);

		try {
			fOutT.flush();
			fOutT.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MuPDFCore openFile(String path) {
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1 ? path
				: path.substring(lastSlashPos + 1));
		System.out.println("Trying to open " + path);
		try {
			core = new MuPDFCore(path);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return core;
	}

	private Bitmap decodeFile(File f) {
		try {
			// 修改图片尺寸
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, option);
			// 设置新的尺寸
			final int REQUIRED_HEIGHT = 267;
			final int REQUIRED_WIDTH = 210;
			int width_tmp = option.outWidth, height_tmp = option.outHeight;

			Log.w("thumbnail scale", (width_tmp + "  " + height_tmp));

			int scale = 3;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			Log.e("thumbnail", e.getMessage());
		}
		return null;
	}
}

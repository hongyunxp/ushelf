package com.artifex.mupdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.GridView;

public class BookShelf extends GridView {
	
	private Bitmap background;  
	private Paint paint = new Paint();
	   
    public BookShelf(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        background = BitmapFactory.decodeResource(getResources(), R.drawable.list);  
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = getChildCount();
        int top = 0;//count > 0 ? getChildAt(0).getTop() : 0;
        int backgroundWidth = background.getWidth();
        int backgroundHeight = background.getHeight();
        int width = getWidth();
        int height = getHeight();
        RectF rectf = null;
        NinePatch np = null;
     
        for (int y = top; y < height; y += backgroundHeight) {
            //for (int x = 0; x < width; x += backgroundWidth){
            	rectf = new RectF(0, y, width, backgroundHeight + y);
            	canvas.drawBitmap(background, null, rectf, paint); 
            	np = new NinePatch(background, background.getNinePatchChunk(), null);
            	np.draw(canvas, rectf);
            //}
        }
     
        super.dispatchDraw(canvas);
    }
}

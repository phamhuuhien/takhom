package com.example.takhom.widgets;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class MyGraphview extends View {
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
	private float[] value_degree;  
	RectF rectf = new RectF(120, 0, 300, 300);  
	float temp = 0;  

	public MyGraphview(Context context, float[] values) {  
		super(context);  
		value_degree = new float[values.length];  
		for (int i = 0; i < values.length; i++) {  
			value_degree[i] = values[i];  
		}  
	}  

	@Override  
	protected void onDraw(Canvas canvas) {  
		super.onDraw(canvas);  
		Random r;  
		for (int i = 0; i < value_degree.length; i++) {  
			if (i == 0) {  
				r = new Random();  
				int color = Color.argb(100, r.nextInt(256), r.nextInt(256),  
						r.nextInt(256));  
				paint.setColor(color);  
				canvas.drawArc(rectf, 0, value_degree[i], true, paint);  
			} else {  
				temp += value_degree[i - 1];  
				r = new Random();  
				int color = Color.argb(255, r.nextInt(256), r.nextInt(256),  
						r.nextInt(256));  
				paint.setColor(color);  
				canvas.drawArc(rectf, temp, value_degree[i], true, paint);  
			}  
		}  
	}    
}

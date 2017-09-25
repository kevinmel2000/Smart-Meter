package com.app.croptest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class Rectangle extends View {
    Paint paint = new Paint();

    public Rectangle(Context context) {
        super(context);
    }





    @Override
    public void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        //param 1 :  left , param 2 :  top , param 3 : right (width) , param 4 : bottom (height)
        //Rect rect = new Rect(130, canvas.getHeight()/3,canvas.getWidth()-130, canvas.getHeight()/2);
        Rect rect = new Rect(130, 50,canvas.getWidth()-130, canvas.getHeight()/5);
        canvas.drawRect(rect, paint );
    }


}

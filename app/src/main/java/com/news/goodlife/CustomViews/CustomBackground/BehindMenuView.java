package com.news.goodlife.CustomViews.CustomBackground;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BehindMenuView extends View {
    Paint paint;
    RectF rectF;

    public BehindMenuView(Context context) {
        super(context);
    }

    public BehindMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPaints();

        setWillNotDraw(false);
    }

    private void setPaints() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        rectF = new RectF(0,0,0,0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = getWidth();
        rectF.bottom = getHeight();

        //Draw BG
        paint.setColor(Color.parseColor("#151619"));
        paint.setAlpha(170);
        canvas.drawRoundRect(rectF, dpToPx(25),dpToPx(25), paint);
        setClipToOutline(false);

        //Drawfirstcircle
        paint.setColor(Color.parseColor("#000000"));
        paint.setAlpha(50);
        //canvas.drawCircle(getWidth()/5, 0, getHeight()/2, paint);

        //Drawfirstcircle
        paint.setColor(Color.parseColor("#FB2364"));
        paint.setAlpha(20);
        //canvas.drawCircle((float)(getWidth()/5)*4, 0, getHeight()/1.5f, paint);

        //Drawfirstcircle
        paint.setColor(Color.parseColor("#153E34"));
        paint.setAlpha(50);
        //canvas.drawCircle((float)(getWidth()/5)*3, (float)getHeight()/2, getHeight(), paint);

        //Drawfirstcircle
        paint.setColor(Color.parseColor("#54AFEE"));
        paint.setAlpha(250);
        //canvas.drawCircle((float)(getWidth()/5)/2, (float)getHeight()/2, (float)getHeight()/4, paint);

        paint.setStyle(Paint.Style.FILL);

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

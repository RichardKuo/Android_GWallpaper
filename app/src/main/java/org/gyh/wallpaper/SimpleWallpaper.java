package org.gyh.wallpaper;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;


/**
 * Created by Richard.Kuo on 17-9-4.
 */

public class SimpleWallpaper extends WallpaperService {
    private static final String TAG = SimpleWallpaper.class.getSimpleName();

    private SimpleEngine mSimpleEngine = null;
    private WindowManager mWindowManager = null;
    private int mWidth = 0;
    private int mHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate()");
        if(null == mSimpleEngine) {
            mSimpleEngine = new SimpleEngine();
        }

        mWindowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        Log.i(TAG, "onCreate()::mWidth = " + mWidth + ", mHeight = " + mHeight);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy()");
    }

    @Override
    public Engine onCreateEngine() {
        Log.i(TAG, "onCreateEngine()");

        if(null == mSimpleEngine) {
            mSimpleEngine = new SimpleEngine();
        }
        return mSimpleEngine;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.i(TAG, "onConfigurationChanged()");
    }

    class SimpleEngine extends WallpaperService.Engine {
        private final String TAG = SimpleEngine.class.getSimpleName();

        private Handler mH = new Handler();
        private Bitmap mBitmap = null;
        private int mBitmapWidth = 0;
        private int mBitmapHeight = 0;

        @Override
        public boolean isVisible() {
            return super.isVisible();
        }

        @Override
        public boolean isPreview() {
            return super.isPreview();
        }

        @Override
        public void setTouchEventsEnabled(boolean enabled) {
            super.setTouchEventsEnabled(enabled);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            Log.i(TAG, "onCreate()");
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_default, option);
            Log.i(TAG, "onCreate(), option.outWidth = " + option.outWidth + ", option.outHeight = " + option.outHeight);
            option.inJustDecodeBounds = false;
            option.inSampleSize = option.outWidth / mWidth;
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper_default, option);
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            Matrix matrix = new Matrix();
            // 这里必须将参数转换为float值之后再做除法计算，否则得出来的scale数据均为0.0，
            // 会导致后面的Bitmap.createBitmap()的参数异常
            float scaleWidth = (float)mWidth / width;
            float scaleHeight = (float)mHeight / height;
            matrix.postScale(scaleWidth, scaleHeight);
            Log.i(TAG, "onCreate(), width = " + width + ", height = " + height + ", scaleWidth = " + scaleWidth + ", scaleHeight = " + scaleHeight);
            Bitmap bitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
            mBitmap.recycle();
            mBitmap = bitmap;
            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
            Log.i(TAG, "onCreate(), mBitmapWidth = " + mBitmapWidth + ", mBitmapHeight = " + mBitmapHeight);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            Log.i(TAG, "onDestroy()");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            Log.i(TAG, "onVisibilityChanged(), visible = " + visible);
            if(visible) {
                draw();
            } else {
                clear();
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            Log.i(TAG, "onTouchEvent(), event = " + event);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);

            Log.i(TAG, "onOffsetsChanged()");
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            Log.i(TAG, "onSurfaceChanged()");
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);

            Log.i(TAG, "onSurfaceRedrawNeeded()");
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);

            Log.i(TAG, "onSurfaceCreated()");
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            Log.i(TAG, "onSurfaceDestroyed()");
            clear();
        }

        private void draw() {
            Log.i(TAG, "draw()");
            Canvas canvas = getSurfaceHolder().lockCanvas();
            Paint paint = new Paint();
            // clear screen
            canvas.drawColor(Color.GRAY);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
            getSurfaceHolder().unlockCanvasAndPost(canvas);
        }

        private void clear() {
            Log.i(TAG, "clear()");
            Canvas canvas = getSurfaceHolder().lockCanvas();
            Paint paint = new Paint();
            // clear screen
            canvas.drawColor(Color.GRAY);
            getSurfaceHolder().unlockCanvasAndPost(canvas);
        }
    }
}

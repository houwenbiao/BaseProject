package com.qtimes.utils.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import androidx.collection.LruCache;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by plu on 2016/4/21.
 */
public class BitmapHelper {
    public final static int CACHE_KEY_GLOBAL = 2;
    public final static int CACHE_KEY_LIVE = 3;

    private final static int BITMAP_HEIGHT = 800;
    private final static int BITMAP_WIGHT = 480;
    private final static int MAX_SIZE = 60;

    private static LruCache<String, Bitmap> mMemoryCache;
    private static BitmapHelper mBitmapHelper;
    public static String KEY_DEF = "key_def_img";

    private static SparseArray<List<String>> mCacheKeyList;

    public static BitmapHelper getInstance() {
        if (mBitmapHelper == null) {
            mBitmapHelper = new BitmapHelper();
        }
        return mBitmapHelper;
    }

    private BitmapHelper() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        mCacheKeyList = new SparseArray();
    }

    public void addToMemoryCacheByType(int type, String key, Bitmap bitmap) {
        List<String> list = mCacheKeyList.get(type);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(key);
        mCacheKeyList.put(type, list);
        addBitmapToMemoryCache(key, bitmap);
    }

    public synchronized void removeMemoryCacheByType(int type) {
        try {
            List<String> list = mCacheKeyList.get(type);
            if (list != null) {
                for (String key : list) {
                    removeBitmapFromMemCache(key);
                }
                list.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            PluLog.e(">>>removeMemoryCacheByType:" + e.toString());
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && !bitmap.isRecycled()) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public void removeBitmapFromMemCache(String key) {
        if (mMemoryCache != null && !TextUtils.isEmpty(key)) {
            mMemoryCache.remove(key);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        if (mMemoryCache == null || TextUtils.isEmpty(key)) {
            return null;
        }
        Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap = null;//bitmap回收后主动置空
        }
        return bitmap;
    }

    public void cleanLruCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }

        if (mCacheKeyList != null) {
            mCacheKeyList.clear();
        }
    }

    /**
     * get the orientation of the bitmap {@link android.media.ExifInterface}
     *
     * @param path
     * @return
     */
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate the bitmap
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }


    /**
     * caculate the bitmap sampleSize
     *
     * @param options
     * @return
     */
    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) {
            return 1;
        }
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 压缩指定路径的图片，并得到图片对象
     *
     * @return Bitmap {@link android.graphics.Bitmap}
     */
    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static String compressByQuality(Context context, String srcPath) {
        return compressByQuality(context, srcPath, MAX_SIZE, BITMAP_WIGHT, BITMAP_HEIGHT);
    }


    /**
     * 通过降低图片的质量来压缩图片
     */
    public static String compressByQuality(Context context, String srcPath, int maxSize, int rqsW, int rqsH) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        Bitmap decoeBitmap = null;
        File srcFile = new File(srcPath);
        String desPath = FileUtils.getPath(context) + File.separator + srcFile.getName();

        int degree = getDegress(srcPath);
        try {
            if (degree != 0) {
                bitmap = rotateBitmap(bitmap, degree);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            System.out.println("图片压缩前大小：" + baos.toByteArray().length / 1024 + "kb");
            boolean isCompressed = false;
            while (baos.toByteArray().length / 1024 > maxSize) {
                quality -= 10;
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                System.out.println("质量压缩到原来的" + quality + "%时大小为："
                        + baos.toByteArray().length / 1024 + "kb");
                isCompressed = true;
            }
            System.out.println("图片压缩后大小：" + baos.toByteArray().length / 1024 + "kb");
            if (isCompressed) {
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                        baos.toByteArray(), 0, baos.toByteArray().length);
                recycleBitmap(bitmap);
                decoeBitmap = compressedBitmap;
            } else {
                decoeBitmap = bitmap;
            }

            PluLog.e(" des path : " + desPath);
            File file = new File(desPath);
            FileOutputStream fos = new FileOutputStream(file);
            decoeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            System.out.println("图片压缩后文件：" + file.length() / 1024 + "kb");
            fos.close();

        } catch (Exception e) {
            // TODO: handle exception

        }
        return desPath;
    }


    /**
     * 根据屏幕最大分辨率来裁剪图片
     *
     * @param context
     * @param bitmap  要压缩的图片
     * @return
     */
    public static Bitmap compressBitmapByDisplay(Context context, Bitmap bitmap) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int windowHeight = dm.heightPixels;
        int windowWidth = dm.widthPixels;
        if (windowWidth * windowHeight < width * height) {//自身屏幕分辨率小于图片尺寸，则按照屏幕分辨率进行压缩
            bitmap = compressBySize(bitmap, windowWidth, windowHeight);
        }
        return bitmap;
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小
     *
     * @param bitmap       要压缩图片
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     */
    public static Bitmap compressBySize(Bitmap bitmap, int targetWidth, int targetHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
                baos.toByteArray().length, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内存；
        opts.inJustDecodeBounds = false;
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                baos.toByteArray(), 0, baos.toByteArray().length, opts);
        recycleBitmap(bitmap);
        return compressedBitmap;
    }

    /**
     * 回收位图对象
     *
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            PluLog.d("recycleBitmap " + bitmap);
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
// 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 图片画圆角矩形
     *
     * @param bitmap , roundPx表示弧度，0代表画圆
     * @return
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap, float roundPx) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            if (roundPx == 0) {
                roundPx = width / 2;
            }
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            if (roundPx == 0) {
                roundPx = height / 2;
            }
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height,
                android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static Bitmap getScaleImg(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null || bm.isRecycled()) {
            return null;
        }
        // 图片源
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 处理图片
     *
     * @param bm        所要转换的bitmap
     * @param newWidth
     * @param newHeight
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int min) {
        bitmap = zoomImg(bitmap, min, min);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        //保证是方形，并且从中心画
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w;
        int deltaX = 0;
        int deltaY = 0;
        if (width <= height) {
            w = width;
            deltaY = height - w;
        } else {
            w = height;
            deltaX = width - w;
        }
        final Rect rect = new Rect(deltaX, deltaY, w, w);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //圆形，所有只用一个
        int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 创建bitmap
     *
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap createBitmap(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outHeight > maxHeight || outWidth > maxWidth) {
            float widthRatio = outWidth * 1.0f / maxWidth;
            float heightRatio = outHeight * 1.0f / maxHeight;
            float ratio = widthRatio > heightRatio ? widthRatio : heightRatio;
            options.inSampleSize = Math.round(ratio);
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }
}

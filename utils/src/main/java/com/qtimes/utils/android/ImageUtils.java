/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.qtimes.utils.android;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.opengl.GLException;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Utility class for manipulating images.
 */
public class ImageUtils {

    static {
        try {
            System.loadLibrary("imageutils");
            PluLog.i("Native library found, native RGB -> YUV conversion available.");
        } catch (UnsatisfiedLinkError e) {
            PluLog.w("Native library not found, native RGB -> YUV conversion may be unavailable.");
        }
    }

    // This value is 2 ^ 18 - 1, and is used to clamp the RGB values before their ranges
    // are normalized to eight bits.
    static final int kMaxChannelValue = 262143;

    // Always prefer the native implementation if available.
    private static boolean useNativeConversion = false;


    /**
     * Utility method to compute the allocated size in bytes of a YUV420SP image of the given
     * dimensions.
     */
    public static int getYUVByteSize(final int width, final int height) {
        // The luminance plane requires 1 byte per pixel.
        final int ySize = width * height;

        // The UV plane works on 2x2 blocks, so dimensions with odd size must be rounded up.
        // Each 2x2 block takes 2 bytes to encode, one each for U and V.
        final int uvSize = ((width + 1) / 2) * ((height + 1) / 2) * 2;

        return ySize + uvSize;
    }

    /**
     * Saves a Bitmap object to disk for analysis.
     *
     * @param bitmap The bitmap to save.
     */
    public static void saveBitmap(final Bitmap bitmap) {
        saveBitmap(bitmap, "wonly_" + TimeUtils.getCurrentTimeMSStr() + ".jpg");
    }

    public static void saveYuv(byte[] data, int width, int height) {
        String filename = "wonly_" + TimeUtils.getCurrentTimeMSStr() + ".jpg";
        final String root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Wonly/images";
        final File myDir = new File(root);

        if (!myDir.mkdirs()) {
            PluLog.i("Make dir failed or dir is created");
        } else {
            PluLog.i("Make dir success");
        }

        final String fname = filename;
        final File file = new File(myDir, fname);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
            image.compressToJpeg(new Rect(0, 0, width, height), 99, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    /**
     * Saves a Bitmap object to disk for analysis.
     *
     * @param bitmap The bitmap to save.
     */
    public static void saveBitmap(final Bitmap bitmap, final File parent) {
        saveBitmap(bitmap, "wonly_" + TimeUtils.getCurrentTimeMSStr() + ".jpg", parent);
    }

    /**
     * Saves a Bitmap object to disk for analysis.
     *
     * @param bitmap   The bitmap to save.
     * @param filename The location to save the bitmap to.
     */
    public static void saveBitmap(final Bitmap bitmap, final String filename) {
        final String root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Wonly/images";
        final File myDir = new File(root);

        if (!myDir.mkdirs()) {
            PluLog.i("Make dir failed or dir is created");
        } else {
            PluLog.i("Make dir success");
        }

        final String fname = filename;
        final File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            final FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, out);
            out.flush();
            out.close();
        } catch (final Exception e) {
            PluLog.e(e.toString());
        }
    }

    /**
     * Saves a Bitmap object to disk for analysis.
     *
     * @param bitmap   The bitmap to save.
     * @param filename The location to save the bitmap to.
     */
    public static void saveBitmap(final Bitmap bitmap, final String filename, final File parent) {
        if (!parent.exists()) {
            PluLog.e("save bitmap failed, parent file not exist");
            return;
        }
        final String fname = filename;
        final File file = new File(parent, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            final FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, out);
            out.flush();
            out.close();
        } catch (final Exception e) {
            PluLog.e(e.toString());
        }
    }

    private static int YUV2RGB(int y, int u, int v) {
        // Adjust and check YUV values
        y = (y - 16) < 0 ? 0 : (y - 16);
        u -= 128;
        v -= 128;

        // This is the floating point equivalent. We do the conversion in integer
        // because some Android devices do not have floating point in hardware.
        // nR = (int)(1.164 * nY + 2.018 * nU);
        // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
        // nB = (int)(1.164 * nY + 1.596 * nV);
        int y1192 = 1192 * y;
        int r = (y1192 + 1634 * v);
        int g = (y1192 - 833 * v - 400 * u);
        int b = (y1192 + 2066 * u);

        // Clipping RGB values to be inside boundaries [ 0 , kMaxChannelValue ]
        r = r > kMaxChannelValue ? kMaxChannelValue : (r < 0 ? 0 : r);
        g = g > kMaxChannelValue ? kMaxChannelValue : (g < 0 ? 0 : g);
        b = b > kMaxChannelValue ? kMaxChannelValue : (b < 0 ? 0 : b);

        return 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    }

    public static void convertYUV420SPToARGB8888(byte[] input, int width, int height, int[] output) {
        if (useNativeConversion) {
            try {
                //这里的native方法比较低效，速度太慢
                PluLog.i("Native YUV420SP -> RGB convert success   000000");
                ImageUtils.convertYUV420SPToARGB8888(input, output, width, height, false);
                PluLog.i("Native YUV420SP -> RGB convert success   111111");
                return;
            } catch (UnsatisfiedLinkError e) {
                PluLog.w("Native YUV420SP -> RGB implementation not found, falling back to Java implementation");
                useNativeConversion = false;
            }
        }

        // Java implementation of YUV420SP to ARGB8888 converting
        final int frameSize = width * height;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width;
            int u = 0;
            int v = 0;

            for (int i = 0; i < width; i++, yp++) {
                int y = 0xff & input[yp];
                if ((i & 1) == 0) {
                    v = 0xff & input[uvp++];
                    u = 0xff & input[uvp++];
                }

                output[yp] = YUV2RGB(y, u, v);
            }
        }
    }

    public static void convertYUV420ToARGB8888(
            byte[] yData,
            byte[] uData,
            byte[] vData,
            int width,
            int height,
            int yRowStride,
            int uvRowStride,
            int uvPixelStride,
            int[] out) {
        int yp = 0;
        for (int j = 0; j < height; j++) {
            int pY = yRowStride * j;
            int pUV = uvRowStride * (j >> 1);

            for (int i = 0; i < width; i++) {
                int uv_offset = pUV + (i >> 1) * uvPixelStride;

                out[yp++] = YUV2RGB(0xff & yData[pY + i], 0xff & uData[uv_offset], 0xff & vData[uv_offset]);
            }
        }
    }


    /**
     * Converts YUV420 semi-planar data to ARGB 8888 data using the supplied width and height. The
     * input and output must already be allocated and non-null. For efficiency, no error checking is
     * performed.
     *
     * @param input    The array of YUV 4:2:0 input data.
     * @param output   A pre-allocated array for the ARGB 8:8:8:8 output data.
     * @param width    The width of the input image.
     * @param height   The height of the input image.
     * @param halfSize If true, downsample to 50% in each dimension, otherwise not.
     */
    private static native void convertYUV420SPToARGB8888(
            byte[] input, int[] output, int width, int height, boolean halfSize);

    /**
     * Converts YUV420 semi-planar data to ARGB 8888 data using the supplied width
     * and height. The input and output must already be allocated and non-null.
     * For efficiency, no error checking is performed.
     *
     * @param y
     * @param u
     * @param v
     * @param uvPixelStride
     * @param width         The width of the input image.
     * @param height        The height of the input image.
     * @param halfSize      If true, downsample to 50% in each dimension, otherwise not.
     * @param output        A pre-allocated array for the ARGB 8:8:8:8 output data.
     */
    private static native void convertYUV420ToARGB8888(
            byte[] y,
            byte[] u,
            byte[] v,
            int[] output,
            int width,
            int height,
            int yRowStride,
            int uvRowStride,
            int uvPixelStride,
            boolean halfSize);

    /**
     * Converts YUV420 semi-planar data to RGB 565 data using the supplied width
     * and height. The input and output must already be allocated and non-null.
     * For efficiency, no error checking is performed.
     *
     * @param input  The array of YUV 4:2:0 input data.
     * @param output A pre-allocated array for the RGB 5:6:5 output data.
     * @param width  The width of the input image.
     * @param height The height of the input image.
     */
    private static native void convertYUV420SPToRGB565(
            byte[] input, byte[] output, int width, int height);

    /**
     * Converts 32-bit ARGB8888 image data to YUV420SP data.  This is useful, for
     * instance, in creating data to feed the classes that rely on raw camera
     * preview frames.
     *
     * @param input  An array of input pixels in ARGB8888 format.
     * @param output A pre-allocated array for the YUV420SP output data.
     * @param width  The width of the input image.
     * @param height The height of the input image.
     */
    private static native void convertARGB8888ToYUV420SP(
            int[] input, byte[] output, int width, int height);

    /**
     * Converts 16-bit RGB565 image data to YUV420SP data.  This is useful, for
     * instance, in creating data to feed the classes that rely on raw camera
     * preview frames.
     *
     * @param input  An array of input pixels in RGB565 format.
     * @param output A pre-allocated array for the YUV420SP output data.
     * @param width  The width of the input image.
     * @param height The height of the input image.
     */
    private static native void convertRGB565ToYUV420SP(
            byte[] input, byte[] output, int width, int height);


    /**
     * Returns a transformation matrix from one reference frame into another. Handles cropping (if
     * maintaining aspect ratio is desired) and rotation.
     *
     * @param srcWidth            Width of source frame.
     * @param srcHeight           Height of source frame.
     * @param dstWidth            Width of destination frame.
     * @param dstHeight           Height of destination frame.
     * @param applyRotation       Amount of rotation to apply from one frame to another. Must be a multiple
     *                            of 90.
     * @param maintainAspectRatio If true, will ensure that scaling in x and y remains constant,
     *                            cropping the image if necessary.
     * @return The transformation fulfilling the desired requirements.
     */
    public static Matrix getTransformationMatrix(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation,
            final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
                PluLog.w("Rotation of %d % 90 != 0", applyRotation);
            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }

        // Account for the already applied rotation, if any, and then determine how
        // much scaling is needed for each axis.
        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;

        final int inWidth = transpose ? srcHeight : srcWidth;
        final int inHeight = transpose ? srcWidth : srcHeight;

        // Apply scaling if necessary.
        if (inWidth != dstWidth || inHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) inWidth;
            final float scaleFactorY = dstHeight / (float) inHeight;

            if (maintainAspectRatio) {
                // Scale by minimum factor so that dst is filled completely while
                // maintaining the aspect ratio. Some image may fall off the edge.
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                // Scale exactly to fill dst from src.
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }

        if (applyRotation != 0) {
            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;
    }

    /**
     * createBitmapFromGLSurface
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gl
     * @return
     */
    public static Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {
        int[] bitmapBuffer = new int[w * h];
        int[] bitmapSource = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

}
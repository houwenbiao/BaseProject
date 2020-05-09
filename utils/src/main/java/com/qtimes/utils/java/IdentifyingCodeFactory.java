package com.qtimes.utils.java;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

public class IdentifyingCodeFactory {
    private static final char[] CHARS = {
            '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    //default settings
    private static final int DEFAULT_CODE_LENGTH = 4;
    private static final int DEFAULT_FONT_SIZE = 25;
    private static final int DEFAULT_LINE_NUMBER = 3;
    private static final int DEFAULT_POINT_NUMBER = 300;
    private static final int BASE_PADDING_LEFT = 15, RANGE_PADDING_LEFT = 5, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 20;
    private static final int DEFAULT_WIDTH = 100, DEFAULT_HEIGHT = 40;
    private static IdentifyingCodeFactory bmpCode;
    //settings decided by the layout xml
    //canvas width and height
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
    //random word space and pading_top
    private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT,
            base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;
    //number of chars, lines; font size
    private int codeLength = DEFAULT_CODE_LENGTH, line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE, point_number = DEFAULT_POINT_NUMBER;
    //variables
    private String code;
    private int padding_left, padding_top;
    private Random random = new Random();

    public static IdentifyingCodeFactory getInstance() {
        if (bmpCode == null)
            bmpCode = new IdentifyingCodeFactory();
        return bmpCode;
    }
    public IdentifyingCodeFactory setWidth(int width){
        this.width=width;
        return this;
    }
    public IdentifyingCodeFactory setHeight(int height){
        this.height=height;
        return this;
    }
    public Bitmap createBitmap() {
        padding_left = 0;

        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();

//        c.drawColor(0x11000000);
        Paint paint = new Paint();
        paint.setTextSize(font_size);

        for (int i = 0; i < point_number; i++) {
            drawPoint(c, paint);
        }

        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }

        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }

        c.save();
        c.restore();
        return bp;
    }

    public Bitmap scaleBitmap(Bitmap bm, float s) {
        Matrix matrix = new Matrix();
        matrix.postScale(s, s);
        Bitmap resizeBmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        return resizeBmp;
    }

    public String getCode() {
        return code;
    }

    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private void drawPoint(Canvas canvas, Paint paint) {
        int color = randomColor();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());
//        float skewX = random.nextInt(11) / 10;
//        skewX = random.nextBoolean() ? skewX : -skewX;
//        paint.setTextSkewX(skewX);
        paint.setUnderlineText(random.nextBoolean());
//        paint.setStrikeThruText(random.nextBoolean());
    }

    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }
}

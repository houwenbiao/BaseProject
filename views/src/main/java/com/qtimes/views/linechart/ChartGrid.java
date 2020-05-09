package com.qtimes.views.linechart;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseArray;
import android.util.TypedValue;

/**
 * Class for holding values to draw grid by {@link LineChartView LineChartView}
 */
class ChartGrid {
    int stepHor = 10;
    int stepVer = 10;

    int horSubLinesCount = 0;
    int verSubLinesCount = 0;

    boolean horMainLinesEnabled = true;
    boolean horSubLinesEnabled = true;
    boolean verMainLinesEnabled = true;
    boolean verSubLinesEnabled = true;
    boolean verMainValuesEnabled = true;
    boolean horMainValuesEnabled = true;

    Paint mainHorLinesPaint = new Paint();
    Paint mainVerLinesPaint = new Paint();
    Paint subHorLinesPaint = new Paint();
    Paint subVerLinesPaint = new Paint();

    Paint mainVerValuesPaint = new Paint();
    Paint mainHorValuesPaint = new Paint();
    Paint mainCheckHorValuesPaint = new Paint();
    Paint mainCheckReactHorValuesPaint = new Paint();

    int horValuesMarginBottom = 0;
    int horValuesMarginTop = 0;
    int horValuesMarginLeft = 0;
    int horValuesMarginRight = 0;

    int verValuesMarginBottom = 20+LineValue.DEFAULT_Y;
    int verValuesMarginTop = 0;
    int verValuesMarginLeft = 10;
    int verValuesMarginRight = 10;

    int horValuesAlign = TextAlign.HORIZONTAL_CENTER | TextAlign.BOTTOM;
    int verValuesAlign = TextAlign.LEFT | TextAlign.BOTTOM;

    SparseArray<String> horValuesText = null;
    SparseArray<String> verValuesText = null;

    public ChartGrid(Context context) {
        mainVerLinesPaint.setColor(0xaa888888);
        subVerLinesPaint.setColor(0x44888888);
        mainHorLinesPaint.setColor(0xaa888888);
        subHorLinesPaint.setColor(0x44888888);

        mainVerValuesPaint.setColor(0xff444444);
        mainVerValuesPaint.setAntiAlias(true);
        mainVerValuesPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                                                 10,
                                                                 context.getResources().getDisplayMetrics()));

        mainHorValuesPaint.setColor(0xff444444);
        mainHorValuesPaint.setAntiAlias(true);
        mainHorValuesPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                                                 12,

                                                                  context.getResources().getDisplayMetrics()));
        mainCheckHorValuesPaint.setColor(0xffff8800);
        mainCheckHorValuesPaint.setAntiAlias(true);
        mainCheckHorValuesPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                12,
                context.getResources().getDisplayMetrics()));

        mainCheckReactHorValuesPaint.setColor(0xffff8800);
        mainCheckReactHorValuesPaint.setAntiAlias(true);

    }

}

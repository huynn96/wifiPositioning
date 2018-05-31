package com.uet.wifiposition.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.getposition.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ducnd on 11/24/17.
 */

public class TrackingView extends View {
    private Path pathRoot;
    private Paint pRoot;
    private int sizeCell;
    private Paint pCel;
    private Path pathCel;
    private Paint pTracking;
    private Path pathTracking;
    private Paint pPoint;
    private Paint pPointReal;
    private Paint pText;
    private boolean isStart;
    private List<LocationModel> locationModels;
    private List<LocationModel> locationReals;

    public TrackingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        locationModels = new ArrayList<>();
        pRoot = new Paint();
        pRoot.setAntiAlias(true);
        pRoot.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        pRoot.setStyle(Paint.Style.STROKE);
        pRoot.setStrokeWidth(8);

        pCel = new Paint();
        pCel.setAntiAlias(true);
        pCel.setColor(Color.BLACK);
        pCel.setStyle(Paint.Style.STROKE);
        pCel.setStrokeWidth(1.0f);

        pathTracking = new Path();

        pTracking = new Paint();
        pTracking.setAntiAlias(true);
        pTracking.setColor(Color.RED);
        pTracking.setStyle(Paint.Style.STROKE);
        pTracking.setStrokeWidth(8.0f);

        pPoint = new Paint();
        pPoint.setAntiAlias(true);
        pPoint.setColor(Color.GREEN);

        pPointReal = new Paint();
        pPointReal.setAntiAlias(true);
        pPointReal.setColor(Color.BLACK);

        pText = new Paint();
        pText.setTextSize(20);
        pText.setAntiAlias(true);
        pText.setColor(Color.parseColor("#E91E63"));

        initTrackingReal();
    }

    private void initTrackingReal() {
        locationReals = new ArrayList<>();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int numRows = 10;
        int numCols = 10;
        if (sizeCell == 0) {
            sizeCell = (w-20) / numCols;
            pathRoot = new Path();

            pathCel = new Path();

            for (int i = 0; i < numCols; i++) {
                Path path = new Path();
                path.moveTo(0, i * sizeCell);
                path.lineTo((numCols - 1)  * sizeCell, i * sizeCell);
                pathCel.addPath(path);
            }

            for (int i = 0; i < numRows; i++) {
                Path path = new Path();
                path.moveTo(i * sizeCell , 0);
                path.lineTo(i * sizeCell, (numRows - 1)* sizeCell);

                pathCel.addPath(path);
            }

            pathTracking.moveTo(10 * sizeCell, 7 * sizeCell);
            for (int i = 1; i < locationModels.size(); i++) {
                pathTracking.lineTo(locationModels.get(i).getX() * sizeCell, locationModels.get(i).getY() * sizeCell);
            }


            invalidate();
        }
    }

    public void addPath(int x, int y, boolean isStart) {
        if (isStart) {
            pathTracking.moveTo(x * sizeCell, y * sizeCell);
        } else {
            pathTracking.lineTo(x * sizeCell, y * sizeCell);
        }
        LocationModel model = new LocationModel();
        model.setX(x);
        model.setY(y);
        locationModels.add(model);
        invalidate();
    }

    public void clearPath() {
        locationModels = new ArrayList<>();
        pathTracking = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("width: ", String.valueOf(getWidth()));
        Log.d("height: ", String.valueOf(getHeight()));
        canvas.translate(20, getHeight() - 250);
        canvas.scale(1, -1);
        if (pathCel != null) {
            canvas.drawPath(pathCel, pCel);
        }
        if (pathRoot != null) {
            canvas.drawPath(pathRoot, pRoot);
        }

        int increase = 0;
        if (locationModels.size() > 0) {
            canvas.drawPath(pathTracking, pTracking);
            int index = 0;
            for (LocationModel locationModel : locationModels) {
                canvas.drawCircle(
                        locationModel.getX() * sizeCell,
                        locationModel.getY() * sizeCell,
                        10,
                        pPoint
                );
                canvas.drawText((index+increase) + "", locationModel.getX() * sizeCell, locationModel.getY() * sizeCell, pText);
                if ( index == 11 ) {
                    increase = 2;
                }
                index++;
            }
        }
        int index = 0;
        for (LocationModel locationReal : locationReals) {
            canvas.drawCircle(
                    locationReal.getX() * sizeCell,
                    locationReal.getY() * sizeCell,
                    10,
                    pPointReal
            );

            if (index == 12 || index == 13) {
                canvas.drawText("Miss", locationReal.getX() * sizeCell, locationReal.getY() * sizeCell, pText);
            } else {
                canvas.drawText(index+ "", locationReal.getX() * sizeCell, locationReal.getY() * sizeCell, pText);
            }
            index++;
        }
        canvas.restore();
    }

    public boolean isPathEmpty() {
        return locationModels.size() == 0;
    }

    private Point convertXYtoPoint(int x, int y) {
        return new Point(x * sizeCell, y * sizeCell);
    }
}

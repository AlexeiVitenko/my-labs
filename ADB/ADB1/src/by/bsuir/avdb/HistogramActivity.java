package by.bsuir.avdb;

import static by.bsuir.avdb.Table.MAX_RATE_EXTRA;
import static by.bsuir.avdb.Table.MIN_RATE_EXTRA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class HistogramActivity extends Activity implements OnTouchListener {
    private static final String IMAGE_PATH = "historgam.jpg";
    private static final int ACTION_SHARE_MAIL = 101;
    private static final int ACTION_SHARE = 102;

    private XYPlot mySimpleXYPlot;
    private Cursor mCursor;
    private PointF minXY;
    private PointF maxXY;
    private float mMinSpain;
    private float mMaxSpain;
    private float mMaxX;
    private float mMinX;

    private String mMinRate;
    private String mMaxRate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plot);

        mMinRate = getIntent().getStringExtra(MIN_RATE_EXTRA);
        mMaxRate = getIntent().getStringExtra(MAX_RATE_EXTRA);
        CursorQueryHandler cqh = new CursorQueryHandler(this, new DBHelper(this).getReadableDatabase());
        mCursor = cqh.getWithRates(mMinRate, mMaxRate);
        List<Operation> operations = cqh.getOperationsList(mCursor);
        Collections.sort(operations, new Comparator<Operation>() {

            @Override
            public int compare(Operation lhs, Operation rhs) {
                return lhs.rate.compareTo(rhs.rate);
            }
        });
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

        List<Float> xP = new ArrayList<Float>();
        List<Integer> yP = new ArrayList<Integer>();
        List<Float> xN = new ArrayList<Float>();
        List<Integer> yN = new ArrayList<Integer>();
        List<Integer> xDelta = new ArrayList<Integer>();
        for (Operation operation : operations) {
            Log.d("operation", ""+operation.rate.toString() + " " + operation.count + " " + operation.buy_sell);
            if (operation.buy_sell) {
                xP.add(operation.rate.floatValue());
                yP.add(operation.count);
            } else {
                xN.add(operation.rate.floatValue());
                yN.add(operation.count);

            }
        }
        
        for (int i = 0; i < xP.size(); i++) {
            xDelta.add(yP.get(i) - yN.get(i));
        }
        
        mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[] { 1, 1 }, 1));
        mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        mySimpleXYPlot.getGraphWidget().setMarginRight(15);
        mySimpleXYPlot.getGraphWidget().setMarginTop(15);
        mySimpleXYPlot.setTitle(getString(R.string.statistics));
        mySimpleXYPlot.setDomainBoundaries(Float.parseFloat(mMinRate), Float.parseFloat(mMaxRate), BoundaryMode.FIXED);

        Collections.sort(operations, new Comparator<Operation>() {

            @Override
            public int compare(Operation lhs, Operation rhs) {
                return new Integer(lhs.count).compareTo(rhs.count);
            }
        });
        
        mySimpleXYPlot.setRangeBoundaries(Collections.min(xDelta) <= 0? Collections.min(xDelta) - 10: 0, operations.get(operations.size() - 1).count + 10, BoundaryMode.FIXED);
        mySimpleXYPlot.addSeries(new SimpleXYSeries(xP, yP, "BUY"), new LineAndPointFormatter(Color.RED, Color.MAGENTA,
                Color.TRANSPARENT));
        mySimpleXYPlot.addSeries(new SimpleXYSeries(xN, yN, "SELL"), new LineAndPointFormatter(Color.BLUE, Color.CYAN,
                Color.TRANSPARENT));
        mySimpleXYPlot.addSeries(new SimpleXYSeries(xN, xDelta, "BUY - SELL"), new LineAndPointFormatter(Color.GREEN, Color.GREEN,
                Color.TRANSPARENT));
        mySimpleXYPlot.setDomainStepValue(5);
        mySimpleXYPlot.setTicksPerRangeLabel(3);
        mySimpleXYPlot.setDomainLabel(getString(R.string.rate));
        mySimpleXYPlot.getDomainLabelWidget().pack();
        mySimpleXYPlot.setRangeLabel(getString(R.string.count));
        mySimpleXYPlot.getRangeLabelWidget().pack();
        mySimpleXYPlot.disableAllMarkup();

        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        mySimpleXYPlot.getBorderPaint().setStrokeWidth(1);
        mySimpleXYPlot.getBorderPaint().setAntiAlias(false);
        mySimpleXYPlot.getBorderPaint().setColor(Color.WHITE);
        mySimpleXYPlot.setOnTouchListener(this);

        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(), mySimpleXYPlot.getCalculatedMinY()
                .floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(), mySimpleXYPlot.getCalculatedMaxY()
                .floatValue());
        mMinSpain = (maxXY.x - minXY.x) / 2f;
        mMaxSpain = mMinSpain * 10f;
        mMaxX = maxXY.x;
        mMinX = minXY.x;

    }

    @Override
    protected void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

    // Definition of the touch states
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float lastScrolling;
    float distBetweenFingers;
    float lastZooming;

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: 
            firstFinger = new PointF(event.getX(), event.getY());
            mode = ONE_FINGER_DRAG;
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    while (Math.abs(lastScrolling) > 1f || Math.abs(lastZooming - 1) < 1.01) {
                        lastScrolling *= .8;
                        scroll(lastScrolling);
                        lastZooming += (1 - lastZooming) * .2;
                        zoom(lastZooming);
                        mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);
                        try {
                            mySimpleXYPlot.postRedraw();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0);
            mode = ONE_FINGER_DRAG;
            break;
        case MotionEvent.ACTION_POINTER_DOWN: 
            distBetweenFingers = spacing(event);
            if (distBetweenFingers > 5f) {
                mode = TWO_FINGERS_DRAG;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == ONE_FINGER_DRAG) {
                PointF oldFirstFinger = firstFinger;
                firstFinger = new PointF(event.getX(), event.getY());
                lastScrolling = oldFirstFinger.x - firstFinger.x;
                scroll(lastScrolling);
                lastZooming = (firstFinger.y - oldFirstFinger.y) / mySimpleXYPlot.getHeight();
                if (lastZooming < 0)
                    lastZooming = 1 / (1 - lastZooming);
                else
                    lastZooming += 1;
                zoom(lastZooming);
                mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);
                mySimpleXYPlot.redraw();

            } else if (mode == TWO_FINGERS_DRAG) {
                float oldDist = distBetweenFingers;
                distBetweenFingers = spacing(event);
                lastZooming = oldDist / distBetweenFingers;
                zoom(lastZooming);
                Log.d("Zoom", "" + minXY.x + " " + maxXY.x);
                mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);
                mySimpleXYPlot.redraw();
            }
            break;
        }
        return true;
    }

    private void zoom(float scale) {
        float domainSpan = maxXY.x - minXY.x;
        float domainMidPoint = maxXY.x - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;
        if (mMinSpain > offset) {
            minXY.x = domainMidPoint - offset;
            maxXY.x = domainMidPoint + offset;
        }

    }

    private void scroll(float pan) {
        float domainSpan = maxXY.x - minXY.x;
        float step = domainSpan / mySimpleXYPlot.getWidth();
        float offset = pan * step;
        minXY.x += offset;
        maxXY.x += offset;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ACTION_SHARE_MAIL, 0, R.string.send_mail);
        menu.add(0, ACTION_SHARE, 0, R.string.send);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //
        switch (item.getItemId()) {
        case ACTION_SHARE_MAIL:
            emailIntent.setType("message/rfc822");
            break;
        case ACTION_SHARE:
            emailIntent.setType("image/*");
            break;

        default:
            break;
        }
        File newFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            newFile = new File(getExternalCacheDir(), IMAGE_PATH);
        } else {
            newFile = new File(getCacheDir(), IMAGE_PATH);
        }

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                String.format(getString(R.string.rates_range), minXY.x, maxXY.x));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.message_text));
        Bitmap bitmap = Bitmap.createBitmap(mySimpleXYPlot.getWidth(), mySimpleXYPlot.getHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bitmap);
        mySimpleXYPlot.draw(c);
        try {
            bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(newFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(newFile));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_via)));
        return super.onOptionsItemSelected(item);
    }

}

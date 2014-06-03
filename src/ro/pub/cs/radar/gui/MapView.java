package ro.pub.cs.radar.gui;

import ro.pub.cs.radar.R;
import ro.pub.cs.radar.data.Collector;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import android.widget.ImageView;

@SuppressLint("ViewConstructor")
public class MapView extends ImageView {

	private Activity parent;
	private Context context;
	private Bitmap bitmap;

	private float minX = 0;
	private float minY = 0;
	private float maxX, maxY;
	private float gridSpacingX = 10;
	private float gridSpacingY = 10;

	private ScaleGestureDetector mScaleDetector;
	private GestureDetector mSimpleDetector;

	private float mScaleFactor = 1.f;

	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;
	private float mLastTouchX;
	private float mLastTouchY;
	private float mPosX = 0;
	private float mPosY = 0;

	// just one collector allowed
	private Collector collector = null;
	private static boolean busy = false;

	private static final String TAG_DUMP = "DUMP";
	private static final String TAG_SIZE = "SIZE";
	private static final String TAG_COORD = "COORD";

	public MapView(Activity parent, Context context) {
		super(context);
		this.parent = parent;
		this.context = context;

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
		display.getSize(size);
		this.maxX = size.x;
		this.maxY = size.y;

		Log.d(TAG_SIZE, "(" + size.x + ", " + size.y + ")");

		this.bitmap = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.freescale), size.x, size.y,
				false);

		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mSimpleDetector = new GestureDetector(context, new SingleTapListener());

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		dumpEvent(ev);
		// Let the scale and simple GestureDetectors inspect all events.
		mScaleDetector.onTouchEvent(ev);
		mSimpleDetector.onTouchEvent(ev);

		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();

			mLastTouchX = x;
			mLastTouchY = y;
			mActivePointerId = ev.getPointerId(0);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = ev.findPointerIndex(mActivePointerId);
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);

			// Only move if the ScaleGestureDetector isn't processing a gesture.
			if (!mScaleDetector.isInProgress()) {
				final float dx = x - mLastTouchX;
				final float dy = y - mLastTouchY;

				mPosX += dx;
				mPosY += dy;

				invalidate();
			}

			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = MotionEvent.INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = MotionEvent.INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = ev.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);
			}
			break;
		}
		}

		return true;
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP",
				"7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		Log.d(TAG_DUMP, sb.toString());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(mPosX, mPosY);

		canvas.drawBitmap(this.bitmap, 0, 0, null);
		this.drawGrid(canvas);

		canvas.restore();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	private void drawGrid(Canvas canvas) {

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.rgb(230, 230, 230));

		int counterX = 0;
		for (float x = minX; Math.floor(x) <= maxX; x += gridSpacingX) {
			if (counterX % 10 == 0) {
				paint.setColor(Color.rgb(220, 220, 220));
				paint.setStrokeWidth(2);
			} else if (counterX % 5 == 0) {
				paint.setColor(Color.rgb(220, 220, 220));
			}

			canvas.drawLine(x, minY, x, maxY, paint);
			paint.setStrokeWidth(0);
			paint.setColor(Color.rgb(230, 230, 230));
			counterX++;
		}

		int counterY = 0;
		for (float y = minY; Math.floor(y) <= maxY; y += gridSpacingY) {
			if (counterY % 10 == 0) {
				paint.setColor(Color.rgb(220, 220, 220));
				paint.setStrokeWidth(2);
			} else if (counterY % 5 == 0) {
				paint.setColor(Color.rgb(220, 220, 220));
			}

			canvas.drawLine(minX, y, maxX, y, paint);
			paint.setStrokeWidth(0);
			paint.setColor(Color.rgb(230, 230, 230));
			counterY++;
		}
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

			invalidate();
			return true;
		}
	}

	public static synchronized void setBusy(boolean value) {
		busy = value;
	}

	public static synchronized boolean getBusy() {
		return busy;
	}

	private class SingleTapListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {

			if (getBusy() == false) {

				int px = (int) (e.getX() / mScaleFactor - mPosX);
				int py = (int) (e.getY() / mScaleFactor - mPosY);
				Log.d(TAG_COORD, "X = " + px);
				Log.d(TAG_COORD, "Y = " + py);

				collector = new Collector(parent, new Point(px, py));
				collector.start();
				setBusy(true);

				try {
					collector.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			} else {
				parent.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(context)
								.setTitle("Please be patient..")
								.setNeutralButton("I promiz U",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.cancel();
											}
										}).show();
					}
				});

			}
			return true;
		}
	}
}

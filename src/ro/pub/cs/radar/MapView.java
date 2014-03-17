package ro.pub.cs.radar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.graphics.Paint;

public class MapView extends View {

	private Bitmap map;
	private float minX = 0;
	private float minY = 0;
	private float maxX, maxY;
	private float gridSpacingX = 10;
	private float gridSpacingY = 10;

	public MapView(Context context) {
		super(context);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
		display.getSize(size);
		this.maxX = size.x;
		this.maxY = size.y;

		this.map = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.freescale), size.x, size.y, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(this.map, 0, 0, null);
		this.drawGrid(canvas);
	}

	private void drawGrid(Canvas canvas) {
		canvas.save();

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

		canvas.restore();
	}
}

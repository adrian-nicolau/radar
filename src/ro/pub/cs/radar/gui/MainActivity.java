package ro.pub.cs.radar.gui;

import ro.pub.cs.radar.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ScanActivity.class);
				startActivity(intent);
			}
		});

		Button b2 = (Button) findViewById(R.id.button2);
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MapActivity.class);
				startActivity(intent);
			}
		});

		Button b3 = (Button) findViewById(R.id.button3);
		b3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				FileChooserDialogFragment df = new FileChooserDialogFragment();
				df.show(getFragmentManager(), "dialog");
			}
		});

		Button b4 = (Button) findViewById(R.id.button4);
		b4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),
						WhereAmIActivity.class);
				startActivity(intent);
			}
		});

	}
}

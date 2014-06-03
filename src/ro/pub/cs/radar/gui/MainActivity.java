package ro.pub.cs.radar.gui;

import ro.pub.cs.radar.R;
import ro.pub.cs.radar.data.Parser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button APs = (Button) findViewById(R.id.button1);
		APs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
				startActivity(intent);
			}
		});

		Button map = (Button) findViewById(R.id.button2);
		map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(intent);
			}
		});

		Button choose = (Button) findViewById(R.id.button3);
		choose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				FileChooserDialogFragment df = new FileChooserDialogFragment();
				df.show(getFragmentManager(), "dialog");
			}
		});

		Button find = (Button) findViewById(R.id.button4);
		find.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (Parser.points == null) {
					Toast.makeText(getApplicationContext(), "Please select a database first!",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getApplicationContext(), WhereAmIActivity.class);
					startActivity(intent);
				}
			}
		});

	}
}

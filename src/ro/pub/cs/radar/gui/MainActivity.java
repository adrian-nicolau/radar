package ro.pub.cs.radar.gui;

import ro.pub.cs.radar.Constants;
import ro.pub.cs.radar.R;
import ro.pub.cs.radar.data.Parser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
				R.array.algorithms, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner1.setAdapter(adapter1);

		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				WhereAmIActivity.algorithm = parent.getItemAtPosition(pos).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
				R.array.methods, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		Button APs = (Button) findViewById(R.id.button1);
		APs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
				startActivity(intent);
			}
		});

		Button chooseMap = (Button) findViewById(R.id.button2);
		chooseMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				FileChooserDialogFragment df = new FileChooserDialogFragment(Constants.BMP_EXT);
				df.show(getFragmentManager(), "dialog");
			}
		});

		Button map = (Button) findViewById(R.id.button3);
		map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(intent);
			}
		});

		Button chooseDb = (Button) findViewById(R.id.button4);
		chooseDb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				FileChooserDialogFragment df = new FileChooserDialogFragment(Constants.JSON_EXT);
				df.show(getFragmentManager(), "dialog");
			}
		});

		Button find = (Button) findViewById(R.id.button5);
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

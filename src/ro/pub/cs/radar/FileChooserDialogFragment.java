package ro.pub.cs.radar;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import ro.pub.cs.radar.parser.Parser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;

public class FileChooserDialogFragment extends DialogFragment {

	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory()
			.toString());
	private String mChosenFile;
	private static final String FTYPE = ".json";
	
	private void loadFileList() {
		if (mPath.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.contains(FTYPE);
				}
			};
			mFileList = mPath.list(filter);
		} else {
			mFileList = new String[0];
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		loadFileList();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Choose JSON file");
		if (mFileList == null) {
			return builder.create();
		}

		builder.setItems(mFileList, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mChosenFile = mFileList[which];
				Parser parser = new Parser(mChosenFile);
				try {
					parser.execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		return builder.show();
	}
}
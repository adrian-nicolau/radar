package ro.pub.cs.radar.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Locale;

import ro.pub.cs.radar.Constants;
import ro.pub.cs.radar.data.Parser;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;

@SuppressLint("ValidFragment")
public class FileChooserDialogFragment extends DialogFragment {

	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory().toString());
	private String mChosenFile;
	private String fileType;

	public FileChooserDialogFragment(String fileType) {
		this.fileType = fileType;
	}

	private void loadFileList() {
		if (mPath.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return filename.contains(fileType);
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

		builder.setTitle("Choose " + fileType.toUpperCase(Locale.getDefault()) + " file");
		if (mFileList == null) {
			return builder.create();
		}

		builder.setItems(mFileList, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mChosenFile = mFileList[which];
				if (fileType.equals(Constants.JSON_EXT)) {
					Parser parser = new Parser(mChosenFile);
					try {
						parser.execute();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (fileType.equals(Constants.BMP_EXT)) {
					MapView.customMap = mPath + "/" + mChosenFile;
				}
			}
		});

		return builder.show();
	}
}

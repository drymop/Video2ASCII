package lmtuan.video2ascii.app;

import java.awt.Font;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import lmtuan.video2ascii.core.Converter;
import lmtuan.video2ascii.core.FontList;
import lmtuan.video2ascii.gui.FileChooser;
import lmtuan.video2ascii.gui.MainGUI;
import lmtuan.video2ascii.gui.SettingGUI;
import lmtuan.video2ascii.util.SaveUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;

public class Control implements ActionListener {

	private final MainGUI gui = new MainGUI(this);
	private final FileChooser fchooser = new FileChooser();
	private final FontList createFontList = new FontList();


	/*
	 * Setting variables
	 */
	private int charWidth = 2, charHeight = 8;
	private double frameRate = 26;

	public void startApplication() {
		SwingWorker<String[], Void> createFontWorker = new SwingWorker<>() {
			@Override
			public String[] doInBackground() {
				loadSettings(); //load setting variables
				return createFontList.getMonospacedFonts();
			}
		};
		createFontWorker.execute();
		gui.showGUI();
		try {
			gui.setFontOptions((String[])createFontWorker.get(), createFontList.getLastUsedFont());
		} catch (Exception e) {}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case MainGUI.BROWSE_FILE: browseFile(); break;
		case MainGUI.SET_FILE_PATH: setFilePath(); break;
		case MainGUI.SETTING: openSettingGUI(); break;
		case MainGUI.CONVERT: doConversion(); break;
		}
	}

	private void browseFile() {
		File chosenFile = fchooser.openFile();
		if (chosenFile != null) {
			gui.setCurrFile(chosenFile);
		}
	}

	private void setFilePath() {
		File chosenFile = new File(gui.getFilePath());
		if (chosenFile.isFile()) {
			gui.setCurrFile(chosenFile);
		} else {
			gui.setCurrFile(null);
		}
	}

	/*
	 * Allow user to change and save settings
	 */
	private void openSettingGUI() {
		gui.showSettingGUI( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				switch (command) {
				case SettingGUI.CONFIRM: 
					charWidth = gui.getCharWidthSetting();
					charHeight = gui.getCharHeightSetting();
					frameRate = gui.getFrameRateSetting();
				case SettingGUI.CANCEL: gui.closeSettingGUI(); break;
				case SettingGUI.SET_DEFAULT: 
					charWidth = gui.getCharWidthSetting();
					charHeight = gui.getCharHeightSetting();
					frameRate = gui.getFrameRateSetting();
					saveSettings();
					break;
				}
			}
		}, charWidth, charHeight, frameRate);
	}

	private void saveSettings() {
		double[] settings = new double[] {charWidth, charHeight, frameRate};
		try {
			SaveUtil.save(settings, "Settings");
		} catch (Exception e) {}
	}

	private void loadSettings() {
		double[] settings = null;
		try {
			settings = (double[]) SaveUtil.load("Settings");
			charWidth = (int) settings[0];
			charHeight = (int) settings[1];
			frameRate = settings[2];
		} catch (Exception e) {}
	}

	/*
	 * Convert, save as txt file and open Notepad
	 */
	private void doConversion() {
		final File f;
		String fontName = null;
		boolean isBold = false;
		boolean isItalic = false;
		// get Selections
		try {
			f = gui.getFile();
			fontName = gui.getFont(); createFontList.setLastUsedFont(fontName);
			isBold = gui.getBoldSelection();
			isItalic = gui.getItalicSelection();
		} catch (NullPointerException e) {return;}
		// parse Selections to Converter
		int style = (isBold? Font.BOLD : 0) | (isItalic? Font.ITALIC : 0);

		SwingWorker<String[][], Void> converter = new Converter(f, fontName, style, charWidth, charHeight, frameRate);
		converter.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				switch (event.getPropertyName()) {
				case "progress": //update progress
					gui.setProgress((int)event.getNewValue());
					break;
				case "state":
					switch ((StateValue) event.getNewValue()) {
					case DONE:
						gui.closeProgressGUI();
						try {
							String[][] result = converter.get();
							saveTextFile(f, result);
						} catch (IOException e) {
							//cannot save in that location
							return;
						} catch (Exception e) {
							return;
						}
						break;
					case PENDING:
					case STARTED:
						break;
					}
					break;
				}
			}
		});
		converter.execute();

		gui.showProgressGUI(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				converter.cancel(true);
			}
		}, new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				converter.cancel(true);
				gui.closeProgressGUI();
			}    
		});
	}
	
	private void saveTextFile(File originalFile, String[][] content) throws Exception{
		System.out.println("Control: Saving text");
		String saveName = originalFile.getName();
		int pos = saveName.lastIndexOf(".");
		if (pos > 0) {
			saveName = saveName.substring(0, pos);
		}
		String savePath = fchooser.saveFile(saveName);
		if (savePath == null) throw new Exception();
		try {
			SaveUtil.write(savePath, content);
		} catch (IOException e) {}
		try {
			openNotepad(savePath);
		} catch (Exception e) {}
	}

	private void openNotepad(String path) throws Exception {
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().edit(new File(path));
		}
	}
}
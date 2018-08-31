package lmtuan.video2ascii.gui;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UIManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

import java.awt.Insets;
import java.awt.Container;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.io.File;


public class MainGUI {
  public static final String
  	BROWSE_FILE = "faweadc",
  	SET_FILE_PATH = "efdffe",
  	SETTING = "vklm",
  	CONVERT = "nkdferi";


  private ActionListener control;
  private ProgressGUI pGUI;
  private SettingGUI sGUI;

  // Components on GUI
  private JFrame f;
  private JLabel imgLabel, fontLabel;
  private JTextField imgTextField;
  private JComboBox<String> fontChooseComboBox;
  private JCheckBox useBoldFont, useItalicFont;
  private JButton browseButton, settingButton, convertButton;

  // Data from GUI
  private  File chosenFile = null;

  public MainGUI(ActionListener listener) {
  	control = listener;
  }

  public void showGUI(){
    // Set Look and Feel to Native. If cannot then Nimbus
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) {
      try {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Nimbus".equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        }
      } catch (Exception e) {}
    }
    createGUI();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }
  private void createGUI() {
    f = new JFrame("Image To ASCII Converter");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initComponents();
    
    Insets insets = f.getInsets();
    f.setSize( 460 + insets.left+insets.right, 245 + insets.top + insets.bottom );
    f.setResizable(false);
  }
  private void initComponents() {    
    Container pane = f.getContentPane();
    pane.setLayout(null);
    // init Components
    imgLabel = new JLabel("Choose File");
    fontLabel = new JLabel("Choose Font");
    imgTextField = new JTextField();
    fontChooseComboBox = new JComboBox<>();
    useBoldFont = new JCheckBox("Bold", false);
    useItalicFont = new JCheckBox("Italic", false);
    browseButton = new JButton("Browse");
    settingButton = new JButton("Advanced Settings >>");
    convertButton = new JButton("Convert");
    // add Components
    pane.add(imgLabel);
    pane.add(fontLabel);
    pane.add(imgTextField);
    pane.add(fontChooseComboBox);
    pane.add(useBoldFont);
    pane.add(useItalicFont);
    pane.add(browseButton);
    pane.add(settingButton);
    pane.add(convertButton);
    
    // Set positions
    Insets insets = pane.getInsets();
    imgLabel.setBounds(10 + insets.left, 22 + insets.top, 80, 26);
    imgLabel.setHorizontalAlignment(JLabel.CENTER);
    fontLabel.setBounds(10 + insets.left, 68 + insets.top, 80, 26);
    fontLabel.setHorizontalAlignment(JLabel.CENTER);
    imgTextField.setBounds(100 + insets.left, 22 + insets.top, 236, 28);
    fontChooseComboBox.setBounds(100 + insets.left, 68 + insets.top, 207, 28);
    fontChooseComboBox.setMaximumRowCount(5);
    //fontChooseComboBox.setEditable(true);
    useBoldFont.setBounds(320 + insets.left, 68 + insets.top, 50, 28);
    useItalicFont.setBounds(375 + insets.left, 68 + insets.top, 53, 28);
    browseButton.setBounds(347 + insets.left, 22 + insets.top, 90, 28);
    browseButton.setMargin(new Insets(0, 0, 0, 0));
    settingButton.setBounds(263 + insets.left, 113 + insets.top, 174, 32);
    settingButton.setMargin(new Insets(0, 0, 0, 0));
    convertButton.setBounds(145 + insets.left, 160 + insets.top, 140, 44);
    convertButton.setMargin(new Insets(0, 0, 0, 0));

    //set Font //monospaced
    imgLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    fontLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    imgTextField.setFont(new Font("SansSerif", Font.PLAIN, 12));
    fontChooseComboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
    useBoldFont.setFont(new Font("SansSerif", Font.BOLD, 12));
    useItalicFont.setFont(new Font("SansSerif", Font.ITALIC, 12));
    browseButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    settingButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    convertButton.setFont(new Font("SansSerif", Font.BOLD, 16));

    //set Command
    imgTextField.setActionCommand(SET_FILE_PATH);
    browseButton.setActionCommand(BROWSE_FILE);
    settingButton.setActionCommand(SETTING);
    convertButton.setActionCommand(CONVERT);
    
    //set Action Listener
    imgTextField.addActionListener(control);
    browseButton.addActionListener(control);
    settingButton.addActionListener(control);
    convertButton.addActionListener(control);
  }

  // Get selection
  public File getFile() throws NullPointerException {
  	if (chosenFile == null) throw new NullPointerException();
  	return chosenFile;
  }
  public String getFont() throws NullPointerException {
  	return fontChooseComboBox.getSelectedItem().toString();
  }
  public boolean getBoldSelection() {
  	return useBoldFont.isSelected();
  }
  public boolean getItalicSelection() {
  	return useItalicFont.isSelected();
  }
  public String getFilePath() {
    return imgTextField.getText();
  }


  public void setCurrFile(File newFile) {
		if (newFile == null) {
      chosenFile = null;
      imgTextField.setText("");
      return;
    } else {
      chosenFile = newFile;
      imgTextField.setText(chosenFile.getAbsolutePath());
    }
  }
  public void setFontOptions(String[] options, String chosenOption) {
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(options);
		fontChooseComboBox.setModel(model);
    fontChooseComboBox.setSelectedItem(chosenOption);
  }

  /*
   * Progress GUI: show conversion progress
   * Settings GUI: show current settings
   */

  public void showProgressGUI(ActionListener al, WindowAdapter wa) {
    pGUI = new ProgressGUI(f, al, wa);
    pGUI.showGUI();
  }
  public void closeProgressGUI() {
    pGUI.closeGUI();
  }
  public void setProgress(int n) {
    pGUI.setProgress(n);
  }
  public void showSettingGUI(ActionListener al, int charWidth, int charHeight, double frameRate) {
    sGUI = new SettingGUI(f, al, charWidth, charHeight, frameRate);
    sGUI.showGUI();
  }
  public void closeSettingGUI() {
    sGUI.closeGUI();
  }
  public int getCharWidthSetting() {
    return sGUI.getCharWidth();
  }
  public int getCharHeightSetting() {
    return sGUI.getCharHeight();
  }
  public double getFrameRateSetting() {
    return sGUI.getFrameRate();
  }
}

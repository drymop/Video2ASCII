package lmtuan.video2ascii.gui;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;

public class SettingGUI {

  public static final String
    SET_DEFAULT = "set as default",
    CONFIRM = "confirm",
    CANCEL = "cancel";

  private int charWidth, charHeight;
  private double frameRate;

  private ActionListener buttonListener;
  private JFrame parentFrame;

  private JDialog dlg;
  private JLabel imageLabel, videoLabel,
                 charWidthLabel, charHeightLabel, frameRateLabel,
                 pxLabel1, pxLabel2, fpsLabel;
  private JSpinner charWidthSpin, charHeightSpin, frameRateSpin;
  private JButton setDefaultButton, confirmButton, cancelButton;

  
  public SettingGUI(JFrame f, ActionListener al, int charW, int charH, double fRate) {
    this.parentFrame = f;
    this.buttonListener = al;
    this.charWidth = charW;
    this.charHeight = charH;
    this.frameRate = fRate;
    createGUI();
  }
  public void showGUI() {
    dlg.setLocationRelativeTo(parentFrame);
    dlg.setVisible(true);
  }
  public void closeGUI() {
    dlg.setVisible(false);
    dlg.dispose();
  }
  private void createGUI() {
    /*
     * Init components
     */
    dlg = new JDialog(parentFrame, "Settings", true);
    dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dlg.setLayout(null);
    dlg.setResizable(false);

    imageLabel = new JLabel("Image Settings");
    videoLabel = new JLabel("Video Settings");
    charWidthLabel = new JLabel("Width");
    charHeightLabel = new JLabel("Height");
    frameRateLabel = new JLabel("Frame rate");
    pxLabel1 = new JLabel("characters");
    pxLabel2 = new JLabel("characters");
    fpsLabel = new JLabel("frames/second");

    charWidthSpin = new JSpinner( new SpinnerNumberModel(charWidth, 1, 9999, 1));
    charHeightSpin = new JSpinner( new SpinnerNumberModel(charHeight, 1, 9999, 1));
    frameRateSpin = new JSpinner( new SpinnerNumberModel(frameRate, 1, 100, 0.1));

    setDefaultButton = new JButton("Set As Default");
    confirmButton = new JButton("OK");
    cancelButton = new JButton("Cancel");

    /*
     * Set listener and commands
     */
    setDefaultButton.addActionListener(buttonListener);
    confirmButton.addActionListener(buttonListener);
    cancelButton.addActionListener(buttonListener);

    setDefaultButton.setActionCommand(SET_DEFAULT);
    confirmButton.setActionCommand(CONFIRM);
    cancelButton.setActionCommand(CANCEL);

    /*
     * Font
     */
    imageLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
    videoLabel.setFont(new Font("SansSerif", Font.BOLD, 13));


    charWidthLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    charWidthSpin.setFont(new Font("SansSerif", Font.PLAIN, 12));
    pxLabel1.setFont(new Font("SansSerif", Font.PLAIN, 12));

    charHeightLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    charHeightSpin.setFont(new Font("SansSerif", Font.PLAIN, 12));
    pxLabel2.setFont(new Font("SansSerif", Font.PLAIN, 12));

    frameRateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    frameRateSpin.setFont(new Font("SansSerif", Font.PLAIN, 12));
    fpsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    
    setDefaultButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    confirmButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 12));

    /*
     * Add Components
     */
    dlg.add(imageLabel);
    dlg.add(videoLabel);

    dlg.add(charWidthLabel);
    dlg.add(charWidthSpin);
    dlg.add(pxLabel1);

    dlg.add(charHeightLabel);
    dlg.add(charHeightSpin);
    dlg.add(pxLabel2);
    
    dlg.add(frameRateLabel);
    dlg.add(frameRateSpin);
    dlg.add(fpsLabel);

    dlg.add(setDefaultButton);
    dlg.add(confirmButton);
    dlg.add(cancelButton);

    /*
     * Set components' size and position
     */
    Insets insets = dlg.getInsets();

    imageLabel.setBounds(10+insets.left, 10+insets.top, 150, 25);
    
    charWidthLabel.setBounds(30+insets.left, 45+insets.top, 80, 25);
    charWidthSpin.setBounds(115+insets.left, 45+insets.top, 60, 25);
    pxLabel1.setBounds(190+insets.left, 45+insets.top, 120, 25);

    charHeightLabel.setBounds(30+insets.left, 80+insets.top, 80, 25);
    charHeightSpin.setBounds(115+insets.left, 80+insets.top, 60, 25);
    pxLabel2.setBounds(190+insets.left, 80+insets.top, 120, 25);

    videoLabel.setBounds(10+insets.left, 115+insets.top, 150, 25);

    frameRateLabel.setBounds(30+insets.left, 150+insets.top, 80, 25);
    frameRateSpin.setBounds(115+insets.left, 150+insets.top, 60, 25);
    fpsLabel.setBounds(190+insets.left, 150+insets.top, 120, 25);

    setDefaultButton.setMargin(new Insets(0, 0, 0, 0));
    setDefaultButton.setBounds(10+insets.left, 210+insets.top, 100, 25);
    confirmButton.setMargin(new Insets(0, 0, 0, 0));
    confirmButton.setBounds(150+insets.left, 210+insets.top, 70, 25);
    cancelButton.setMargin(new Insets(0, 0, 0, 0));
    cancelButton.setBounds(230+insets.left, 210+insets.top, 70, 25);

    dlg.setSize(320+insets.left+insets.right, 280+insets.top+insets.bottom);
  }

  /*
   * Getters
   */

  public int getCharWidth() {
    return (int)charWidthSpin.getValue();
  }
  public int getCharHeight() {
    return (int)charHeightSpin.getValue();
  }
  public double getFrameRate() {
    return (double)frameRateSpin.getValue();
  }


}

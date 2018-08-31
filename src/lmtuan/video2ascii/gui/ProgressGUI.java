package lmtuan.video2ascii.gui;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JProgressBar;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class ProgressGUI {
  private ActionListener buttonListener;
  private WindowAdapter windowListener;

  // Components on GUI
  private final JFrame parentFrame;
  private JDialog dlg;
  private JProgressBar progressBar; 
  private JButton cancelButton;

  public ProgressGUI(JFrame f, ActionListener al, WindowAdapter wa) {
    this.parentFrame = f;
    this.buttonListener = al;
    this.windowListener = wa;
    createGUI();
  }

  public void showGUI() {
    progressBar.setValue(0);
    dlg.setLocationRelativeTo(parentFrame);
    dlg.setVisible(true);
  }

  public void closeGUI() {
    dlg.setVisible(false);
    dlg.dispose();
  }
  
  private void createGUI() {
    dlg = new JDialog(parentFrame, "Converting", true);
    dlg.addWindowListener(windowListener);
    dlg.setLayout(null);
    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    progressBar = new JProgressBar(0, 100);
    cancelButton = new JButton("Cancel");
    dlg.add(progressBar);
    dlg.add(cancelButton);
    
    Insets insets = dlg.getInsets();
    dlg.setResizable(false);
    dlg.setSize( 380 + insets.left+insets.right, 155 + insets.top + insets.bottom );

    progressBar.setBounds(40 + insets.left, 30 + insets.top, 300, 25);
    progressBar.setFont(new Font("SansSerif", Font.PLAIN, 12));
    progressBar.setStringPainted(true);

    cancelButton.setBounds(250 + insets.left, 80 + insets.top, 90, 30);
    cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    cancelButton.addActionListener(buttonListener);
  }

  public void setProgress(int progress) {
    progressBar.setValue(progress);
  }
}

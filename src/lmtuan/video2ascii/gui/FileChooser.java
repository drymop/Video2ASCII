package lmtuan.video2ascii.gui;

import javax.swing.JButton;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import lmtuan.video2ascii.util.SaveUtil;

public class FileChooser {  
  private String 
    savePath = null,
    loadPath = null;

  public File openFile() {
    loadDefaultPath();

    JButton open = new JButton();
    JFileChooser fc = new JFileChooser(loadPath);
    fc.setDialogTitle("Open");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Images and videos", "jpg", "jpeg", "png", "mp4");
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(filter);
    ImagePreviewPanel preview = new ImagePreviewPanel();
    fc.setAccessory(preview);
    fc.addPropertyChangeListener(preview);
    if (fc.showOpenDialog(open) == JFileChooser.CANCEL_OPTION) {
      return null;
    }

    File f = fc.getSelectedFile();
    
    loadPath = f.getAbsolutePath();
    saveDefaultPaths();
    
    return f;
  }

  public String saveFile(String name) {
    loadDefaultPath();

    JButton save = new JButton();
    JFileChooser fc = new JFileChooser(savePath);
    fc.setApproveButtonText("Save");
    fc.setSelectedFile(new File(name+".txt"));
    fc.setDialogTitle("Save As");

    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(filter);
    if (fc.showOpenDialog(save) == JFileChooser.CANCEL_OPTION) {
      return null;
    }

    File f = fc.getSelectedFile();
    savePath = f.getAbsolutePath();
    saveDefaultPaths();

    return savePath;
  }

  private void saveDefaultPaths() {
    String[] paths = new String[] {savePath, loadPath};
    try {
      SaveUtil.save(paths, "DefaultPaths");
    } catch (Exception e) {}
  }
  private void loadDefaultPath() {
    try {
      String[] paths = (String[]) SaveUtil.load("DefaultPaths");
      savePath = paths[0];
      loadPath = paths[1];
    } catch (Exception e) {
      savePath = null;
      loadPath = null;
    }
  }
}
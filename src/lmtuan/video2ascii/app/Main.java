package lmtuan.video2ascii.app;

import java.awt.EventQueue;

public class Main implements Runnable {
  public static void main(String[] args) {
    EventQueue.invokeLater(new Main());
  }

  public void run() {
    Control ctrl = new Control();
    ctrl.startApplication();
  }
}
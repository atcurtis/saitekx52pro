package org.xiphis.hid;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author antony
 * @since 2015-04-05
 */
public class DemoSaitekX52Pro {

  public static String testString = "This is a simple stupid test of something that scrolls...";

  public static void main(String args[]) throws InterruptedException {
    try (final SaitekX52Pro joy = SaitekX52Pro.init()) {
      System.out.println("Device type : " + joy.getType());

      final Timer timer = new Timer("Saitek Time", true);
      try
      {
        Runnable timerTask = new Runnable() {
          final Calendar date = Calendar.getInstance();
          final Runnable self = this;
          int index;

          @Override
          public void run() {
            date.setTimeInMillis(System.currentTimeMillis());
            joy.setDateTime(date);
            joy.setText(3, testString.subSequence(index++, Math.min(index + 15, testString.length())));
            if (index >= testString.length())
              index = 0;
            timer.schedule(new TimerTask() {
              public void run() {
                self.run();
              }
            }, 1000 - ((System.currentTimeMillis()+1) % 1000));
          }
        };
        timerTask.run();

        joy.setText(1, "Hello World");

        // turn off all the LEDs
        for (SaitekX52Pro.LED led : SaitekX52Pro.LED.values()) {
          joy.setLED(led, false);
        }

        // turn on all the LEDs
        for (SaitekX52Pro.LED led : SaitekX52Pro.LED.values()) {
        //  joy.setLED(led, true);
        }

        joy.setBrightness(false, 5);
        joy.setBrightness(true, 32);

        //Thread.sleep(120000);

        joy.setLED(SaitekX52Pro.LED.THROTTLE, true);
        joy.setLED(SaitekX52Pro.LED.FIRE, true);
      }
      finally {
        timer.cancel();

        joy.setText(1, "");
        joy.setText(2, "");
        joy.setText(3, "");

        joy.setBrightness(true, 0);

      }
    }

  }
}

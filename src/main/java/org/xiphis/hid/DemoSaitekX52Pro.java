/*
 * Copyright (c) 2015 Antony T Curtis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

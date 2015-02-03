package de.matrix44.dedolox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.SeekBar;

public class MainActivity extends ActionBarActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Button button = (Button) findViewById(R.id.button);
    button.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 60, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 60, 127);
        return false;
      }
    });

    final Button button10 = (Button) findViewById(R.id.button10);
    button10.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 61, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 61, 127);
        return false;
      }
    });

    final Button button2 = (Button) findViewById(R.id.button2);
    button2.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 62, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 62, 127);
        return false;
      }
    });

    final Button button11 = (Button) findViewById(R.id.button11);
    button11.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 63, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 63, 127);
        return false;
      }
    });

    final Button button3 = (Button) findViewById(R.id.button3);
    button3.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 64, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 64, 127);
        return false;
      }
    });

    final Button button4 = (Button) findViewById(R.id.button4);
    button4.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 65, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 65, 127);
        return false;
      }
    });

    final Button button13 = (Button) findViewById(R.id.button13);
    button13.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 66, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 66, 127);
        return false;
      }
    });

    final Button button5 = (Button) findViewById(R.id.button5);
    button5.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 67, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 67, 127);
        return false;
      }
    });

    final Button button14 = (Button) findViewById(R.id.button14);
    button14.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 68, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 68, 127);
        return false;
      }
    });

    final Button button6 = (Button) findViewById(R.id.button6);
    button6.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 69, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 69, 127);
        return false;
      }
    });

    final Button button15 = (Button) findViewById(R.id.button15);
    button15.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 70, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 70, 127);
        return false;
      }
    });

    final Button button7 = (Button) findViewById(R.id.button7);
    button7.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 71, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 71, 127);
        return false;
      }
    });

    final Button button8 = (Button) findViewById(R.id.button8);
    button8.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
          MainAudioThread.getAudioThread().noteOn(0, 72, 127);
        else if (event.getAction() == MotionEvent.ACTION_UP)
          MainAudioThread.getAudioThread().noteOff(0, 72, 127);
        return false;
      }
    });

    final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_MASTER_VOL, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
    seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_ATTACK, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
    seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_HOLD, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar4 = (SeekBar)findViewById(R.id.seekBar4);
    seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_DECAY, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar5 = (SeekBar)findViewById(R.id.seekBar5);
    seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_SUSTAIN, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar6 = (SeekBar)findViewById(R.id.seekBar6);
    seekBar6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_AMPENV_RELEASE, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar7 = (SeekBar)findViewById(R.id.seekBar7);
    seekBar7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_LFO1_SPEED, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar8 = (SeekBar)findViewById(R.id.seekBar8);
    seekBar8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTER_FREQ, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    final SeekBar seekBar9 = (SeekBar)findViewById(R.id.seekBar9);
    seekBar9.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Perform action on click
        MainAudioThread.getAudioThread().controlChange(0, MIDIImplementation.CC_FILTER_RESONANCE, progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) { }
    });

    HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
    ((Osc1Panel)findViewById(R.id.osc1panel)).setScrollView(scrollView);
    ((Osc2Panel)findViewById(R.id.osc2panel)).setScrollView(scrollView);
    ((MixerPanel)findViewById(R.id.mixerpanel)).setScrollView(scrollView);

    // Start to synthesise audio:
    MainAudioThread.getAudioThread().start();
  }


  @Override
  public void onDestroy(){
    super.onDestroy();

    try {
      MainAudioThread.getAudioThread().stopAudio();
      MainAudioThread.getAudioThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}

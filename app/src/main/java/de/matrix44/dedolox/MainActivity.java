package de.matrix44.dedolox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.HorizontalScrollView;
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

    HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
    ((SynthPanel)findViewById(R.id.osc1panel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.osc2panel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.mixerpanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.ampenvpanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.filterpanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.filterenvpanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.lfo1panel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.lfo2panel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.distortionpanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.phaserpanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.delaypanel)).setScrollView(scrollView);
    ((SynthPanel)findViewById(R.id.masterpanel)).setScrollView(scrollView);

    // Make sure that the screens doesn't turn off:
    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  @Override
  public void onResume() {
    super.onResume();

    // Create audio thread:
    MainAudioThread.createAudioThread();

    // Start to synthesise audio:
    MainAudioThread.getAudioThread().start();

    // Load current state:
    DedoloxPreset state = new DedoloxPreset();
    state.load(this, "statebuffer.xml", false);
    setPreset(state);
  }

  private void setPreset(DedoloxPreset preset) {

    // Fill synth:
    MainAudioThread.getAudioThread().loadPreset(preset);

    // Update UI:
    ((SynthPanel)findViewById(R.id.osc1panel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.osc2panel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.mixerpanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.ampenvpanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.filterpanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.filterenvpanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.lfo1panel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.lfo2panel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.distortionpanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.phaserpanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.delaypanel)).setPreset(preset);
    ((SynthPanel)findViewById(R.id.masterpanel)).setPreset(preset);
  }

  @Override
  public void onPause() {
    super.onPause();

    // save current state:
    DedoloxPreset state = MainAudioThread.getAudioThread().getPreset();
    if (state != null) {
      state.setName("Current Buffer");
      state.save(this, "statebuffer.xml");
    }

    // Stop audio system:
    MainAudioThread.disposeAudioThread();
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
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

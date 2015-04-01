package de.matrix44.dedolox;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

/**
 * Created by rollo on 28.03.15.
 */
public class DedoloxPreset {

  public DedoloxPreset() {
    for (int i = 0; i < 128; i++) {
      values[i] = new MIDIEvent();
      values[i].message = 0xB0;
      values[i].value1 = i;
      values[i].value2 = -1;
    }
  }

  public DedoloxPreset(DedoloxPreset other) {
    for (int i = 0; i < 128; i++) {
      values[i] = new MIDIEvent();
      values[i].message = 0xB0;
      values[i].value1 = i;
      values[i].value2 = other.values[i].value2;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    name = newName;
  }

  public int getValue(int controlNo) {
    if (controlNo >= 0 && controlNo < 128)
      return values[controlNo].value2;
    return -1;
  }

  public MIDIEvent getValueEvent(int controlNo) {
    if (controlNo >= 0 && controlNo < 128)
      return values[controlNo];
    return null;
  }

  public void setValue(int controlNo, int newVal) {
    if (controlNo >= 0 && controlNo < 128)
      values[controlNo].value2 = newVal;
  }

  public MIDIEvent[] getValues() {
    return values;
  }

  public void copyMeta(DedoloxPreset preset) {
    name = preset.name;
  }

  public void clear() {
    name = "cleared";
    for (int i = 0; i < 128; i++)
      values[i].value2 = -1;
  }

  public void save(Context context, String fileName) {

    try {
      FileOutputStream out_s = null;
      try {
        out_s = context.openFileOutput(fileName, Context.MODE_PRIVATE);

        OutputStreamWriter writer = new OutputStreamWriter(out_s);

        XmlSerializer xmlSerializer = Xml.newSerializer();

        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument("UTF-8", true);

        xmlSerializer.startTag("", "preset");

        xmlSerializer.startTag("", "name");
        xmlSerializer.text(name);
        xmlSerializer.endTag("", "name");

        for (int i = 0; i < 128; i++) {
          if (values[i].value2 < 0)
            continue;
          xmlSerializer.startTag("", "cc");
          xmlSerializer.attribute("", "id", Integer.toString(i));
          xmlSerializer.attribute("", "value", Integer.toString(values[i].value2));
          xmlSerializer.endTag("", "cc");
        }

        xmlSerializer.endTag("", "preset");

        xmlSerializer.endDocument();
      } finally {
        if (out_s != null)
          out_s.close();
      }

    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  public void load(Context context, String fileName, boolean asset) {

    clear();

    XmlPullParserFactory pullParserFactory;
    try {
      InputStream in_s = null;
      try {
        in_s = asset ? context.getAssets().open(fileName) : context.openFileInput(fileName);

        pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullParserFactory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in_s, null);

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

          String tagName;

          switch (eventType) {
          case XmlPullParser.START_TAG:
            tagName = parser.getName();

            if (tagName.equals("name")) {
              name = parser.nextText();

            } else if (tagName.equals("cc")) {
              int id = -1, value = -1;
              for (int i = 0; i < parser.getAttributeCount(); i++) {
                if (parser.getAttributeName(i).equals("id")) {
                  id = Integer.parseInt(parser.getAttributeValue(i));
                } else if (parser.getAttributeName(i).equals("value")) {
                  value = Integer.parseInt(parser.getAttributeValue(i));
                }
              }
              if (id >= 0 && id < 128 && value >= 0)
                values[id].value2 = value;
            }
            break;
          }
          eventType = parser.next();
        }
      } finally {
        if (in_s != null)
        in_s.close();
      }

    } catch (XmlPullParserException e) {

      e.printStackTrace();
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  private String name = "init";
  private final MIDIEvent[] values = new MIDIEvent[128];
}

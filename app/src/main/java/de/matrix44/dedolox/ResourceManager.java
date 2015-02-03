package de.matrix44.dedolox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rollo on 01.02.15.
 */
public class ResourceManager {

  public static Bitmap getBitmap(Context context, int id) {

    if (bitmaps.containsKey(id))
      return bitmaps.get(id);

    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
    bitmaps.put(id, bmp);

    return bmp;
  }

  private static final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
}

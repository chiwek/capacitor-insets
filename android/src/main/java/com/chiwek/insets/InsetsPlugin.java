package com.chiwek.insets;

import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Insets")
public class InsetsPlugin extends Plugin {

  private static final boolean DECOR_FITS_SYSTEM_WINDOWS = false;

  private boolean autoPadEnabled = false;
  private boolean padTop = false;
  private boolean padBottom = true; // default
  private View targetView; // root na koji kačimo listener

  @Override
  public void load() {
    getActivity().runOnUiThread(() -> {
      final Window window = getActivity().getWindow();

      WindowCompat.setDecorFitsSystemWindows(window, DECOR_FITS_SYSTEM_WINDOWS);

      View content = window.getDecorView().findViewById(android.R.id.content);
      targetView = (content != null) ? content : getBridge().getWebView();

      ViewCompat.setOnApplyWindowInsetsListener(targetView, (v, insets) -> {
        final int types = WindowInsetsCompat.Type.statusBars()
                      | WindowInsetsCompat.Type.navigationBars()
                      | WindowInsetsCompat.Type.displayCutout();

        Insets bars = insets.getInsetsIgnoringVisibility(types);

        if (autoPadEnabled) {
          int left   = v.getPaddingLeft();
          int right  = v.getPaddingRight();
          int top    = padTop    ? bars.top    : v.getPaddingTop();
          int bottom = padBottom ? bars.bottom : v.getPaddingBottom();
          v.setPadding(left, top, right, bottom);
          notifyInsets(bars);
          return WindowInsetsCompat.CONSUMED;
        } else {
          notifyInsets(bars);
          return insets;
        }
      });

      if (!targetView.isAttachedToWindow()) {
        targetView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
          @Override public void onViewAttachedToWindow(View v) {
            ViewCompat.requestApplyInsets(v);
            v.removeOnAttachStateChangeListener(this);
          }
          @Override public void onViewDetachedFromWindow(View v) { }
        });
      }

      ViewCompat.requestApplyInsets(targetView);
    });
  }

  private void notifyInsets(Insets bars) {
    JSObject payload = new JSObject();
    payload.put("top", bars.top);
    payload.put("bottom", bars.bottom);
    payload.put("left", bars.left);
    payload.put("right", bars.right);
    notifyListeners("insetsChange", payload);
  }

  @PluginMethod
  public void get(PluginCall call) {
    View root = getActivity().getWindow().getDecorView();
    WindowInsetsCompat wi = ViewCompat.getRootWindowInsets(root);
    final int types = WindowInsetsCompat.Type.statusBars()
                   | WindowInsetsCompat.Type.navigationBars()
                   | WindowInsetsCompat.Type.displayCutout();
    Insets bars = (wi != null) ? wi.getInsetsIgnoringVisibility(types) : Insets.NONE;

    JSObject res = new JSObject();
    res.put("top", bars.top);
    res.put("bottom", bars.bottom);
    res.put("left", bars.left);
    res.put("right", bars.right);
    call.resolve(res);
  }

  @PluginMethod
  public void autoPad(PluginCall call) {
    boolean enable = call.getBoolean("enable", true);
    String edges = call.getString("edges", "bottom");

    autoPadEnabled = enable;
    padTop = "top".equals(edges) || "both".equals(edges);
    padBottom = !"top".equals(edges);

    getActivity().runOnUiThread(() -> {
      View v = (targetView != null) ? targetView : getBridge().getWebView();
      ViewCompat.requestApplyInsets(v);
      call.resolve();
    });
  }
}

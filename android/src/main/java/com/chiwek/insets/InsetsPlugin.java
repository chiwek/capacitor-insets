package com.chiwek.insets;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Insets")
public class InsetsPlugin extends Plugin {

  private boolean autoPadEnabled = false;
  private boolean padTop = false;
  private boolean padBottom = true; // default behavior

  @Override
  public void load() {
    getBridge().getWebView().post(() -> {
      View webView = getBridge().getWebView();
      ViewCompat.setOnApplyWindowInsetsListener(webView, (v, insets) -> {
        Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

        // Auto-pad branch: set padding and CONSUME system bar insets
        if (autoPadEnabled) {
          int left   = v.getPaddingLeft();
          int right  = v.getPaddingRight();
          int top    = padTop    ? bars.top    : v.getPaddingTop();
          int bottom = padBottom ? bars.bottom : v.getPaddingBottom();
          v.setPadding(left, top, right, bottom);
          notifyInsets(bars);
          return WindowInsetsCompat.CONSUMED;
        }

        // Read-only branch: just notify JS
        notifyInsets(bars);
        return insets;
      });

      ViewCompat.requestApplyInsets(webView);
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
    Insets bars = (wi != null) ? wi.getInsets(WindowInsetsCompat.Type.systemBars()) : Insets.NONE;

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
    padBottom = !"top".equals(edges); // bottom for 'bottom' or 'both'

    getActivity().runOnUiThread(() -> {
      View webView = getBridge().getWebView();
      ViewCompat.requestApplyInsets(webView);
      call.resolve();
    });
  }
}

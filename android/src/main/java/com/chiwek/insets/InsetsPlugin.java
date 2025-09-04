package com.chiwek.insets;

import android.graphics.Insets;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.PluginMethod;

@CapacitorPlugin(name = "Insets")
public class InsetsPlugin extends Plugin {

  @Override
  public void load() {
    // Listen for WindowInsets and push events to JS
    getBridge().getWebView().post(() -> {
      View webView = getBridge().getWebView();
      ViewCompat.setOnApplyWindowInsetsListener(webView, (v, insets) -> {
        Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        JSObject payload = new JSObject();
        payload.put("top", bars.top);
        payload.put("bottom", bars.bottom);
        payload.put("left", bars.left);
        payload.put("right", bars.right);
        notifyListeners("insetsChange", payload);
        return insets;
      });
      ViewCompat.requestApplyInsets(webView);
    });
  }

  @PluginMethod
  public void get(PluginCall call) {
    View root = getActivity().getWindow().getDecorView();
    WindowInsetsCompat wi = ViewCompat.getRootWindowInsets(root);

    int top = 0, bottom = 0, left = 0, right = 0;
    if (wi != null) {
      Insets bars = wi.getInsets(WindowInsetsCompat.Type.systemBars());
      top = bars.top; bottom = bars.bottom; left = bars.left; right = bars.right;
    }

    JSObject res = new JSObject();
    res.put("top", top);
    res.put("bottom", bottom);
    res.put("left", left);
    res.put("right", right);
    call.resolve(res);
  }
}

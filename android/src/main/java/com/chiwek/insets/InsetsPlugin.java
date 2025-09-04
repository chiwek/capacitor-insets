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

  @Override
  public void load() {
    getBridge().getWebView().post(() -> {
      View webView = getBridge().getWebView();
      ViewCompat.setOnApplyWindowInsetsListener(webView, (v, insets) -> {
        Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

        // Ako je autoPad uključen – podešavamo padding i CONSUME-ujemo insets
        if (autoPadEnabled) {
          v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bars.bottom);
          notifyInsets(bars);
          return WindowInsetsCompat.CONSUMED;
        }

        // U "read-only" režimu samo šaljemo event i ništa ne diramo
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
    autoPadEnabled = enable;
    // Forsiraj re-apply
    getActivity().runOnUiThread(() -> {
      View webView = getBridge().getWebView();
      ViewCompat.requestApplyInsets(webView);
      call.resolve();
    });
  }
}

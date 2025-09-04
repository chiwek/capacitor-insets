package com.chiwek.insets

import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@CapacitorPlugin(name = "Insets")
class InsetsPlugin : Plugin() {

  override fun load() {
    // Listen for insets changes and notify JS
    bridge.webView.post {
      ViewCompat.setOnApplyWindowInsetsListener(bridge.webView) { _, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val payload = JSObject().apply {
          put("top", bars.top)
          put("bottom", bars.bottom)
          put("left", bars.left)
          put("right", bars.right)
        }
        notifyListeners("insetsChange", payload)
        insets
      }
      ViewCompat.requestApplyInsets(bridge.webView)
    }
  }

  @PluginMethod
  fun get(call: PluginCall) {
    val root = activity.window?.decorView
    val wi = root?.let { ViewCompat.getRootWindowInsets(it) }
    val bars = wi?.getInsets(WindowInsetsCompat.Type.systemBars())

    val res = JSObject().apply {
      put("top", bars?.top ?: 0)
      put("bottom", bars?.bottom ?: 0)
      put("left", bars?.left ?: 0)
      put("right", bars?.right ?: 0)
    }
    call.resolve(res)
  }
}

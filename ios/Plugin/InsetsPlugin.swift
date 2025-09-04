import Foundation
import Capacitor
import UIKit

@objc(InsetsPlugin)
public class InsetsPlugin: CAPPlugin {

    private var autoPadEnabled = false
    private var padTop = false
    private var padBottom = true

    public override func load() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.emitInsets),
            name: UIDevice.orientationDidChangeNotification,
            object: nil
        )
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.05) {
            self.emitInsets()
        }
    }

    @objc func get(_ call: CAPPluginCall) {
        let insets = currentInsets()
        call.resolve([
            "top": insets.top,
            "bottom": insets.bottom,
            "left": insets.left,
            "right": insets.right
        ])
    }

    @objc func autoPad(_ call: CAPPluginCall) {
        let enable = call.getBool("enable", true) ?? true
        let edges = call.getString("edges") ?? "bottom"

        autoPadEnabled = enable
        padTop = (edges == "top" || edges == "both")
        padBottom = (edges != "top")

        DispatchQueue.main.async {
            self.applyAutoPad()
            call.resolve()
        }
    }

    @objc private func emitInsets() {
        let insets = currentInsets()
        self.notifyListeners("insetsChange", data: [
            "top": insets.top,
            "bottom": insets.bottom,
            "left": insets.left,
            "right": insets.right
        ])

        if autoPadEnabled {
            DispatchQueue.main.async { self.applyAutoPad() }
        }
    }

    private func currentInsets() -> UIEdgeInsets {
        guard let view = self.bridge?.viewController?.view else { return .zero }
        view.layoutIfNeeded()
        return view.safeAreaInsets
    }

    private func applyAutoPad() {
        guard let web = self.bridge?.webView else { return }
        let i = currentInsets()

        var top = web.scrollView.contentInset.top
        var bottom = web.scrollView.contentInset.bottom

        if padTop { top = i.top }
        if padBottom { bottom = i.bottom }

        let newInset = UIEdgeInsets(top: top, left: 0, bottom: bottom, right: 0)
        web.scrollView.contentInset = newInset
        web.scrollView.scrollIndicatorInsets = newInset
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}

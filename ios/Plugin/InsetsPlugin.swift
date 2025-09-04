import Foundation
import Capacitor
import UIKit

@objc(InsetsPlugin)
public class InsetsPlugin: CAPPlugin {

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

    @objc private func emitInsets() {
        let insets = currentInsets()
        self.notifyListeners("insetsChange", data: [
            "top": insets.top,
            "bottom": insets.bottom,
            "left": insets.left,
            "right": insets.right
        ])
    }

    private func currentInsets() -> UIEdgeInsets {
        guard let view = self.bridge?.viewController?.view else { return .zero }
        view.layoutIfNeeded()
        return view.safeAreaInsets
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}

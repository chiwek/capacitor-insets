# @chiwek/capacitor-insets

Capacitor plugin that exposes **Android WindowInsets** and **iOS safe area** to JavaScript — so your UI can avoid overlapping the **bottom navigation bar / gesture pill** and the **status bar**.

- ✅ Capacitor **6 & 7** compatible
- ✅ Android **WindowInsetsCompat.Type.systemBars()** (keyboard excluded)
- ✅ iOS safe area (`safeAreaInsets`)
- ✅ Simple API: `get()`, `insetsChange` event, and `autoPad({ edges })` to pad natively
- ✅ Works with or without edge-to-edge; you choose

---

## Install

```bash
npm i @chiwek/capacitor-insets
npx cap sync
```

### Usage
```typescript
import { CapacitorInsets } from '@chiwek/capacitor-insets';

// Pad the bottom only (default)
await CapacitorInsets.autoPad({ enable: true, edges: 'bottom' });

// Or pad both top and bottom:
await CapacitorInsets.autoPad({ enable: true, edges: 'both' });

// (Optional) observe changes (rotation, switching gestures/3-button nav, etc.)
CapacitorInsets.addListener('insetsChange', v => console.log('insets', v));

```
import { registerPlugin } from '@capacitor/core';
import type { CapacitorInsetsPlugin, InsetsData } from './definitions';

export const CapacitorInsets = registerPlugin<CapacitorInsetsPlugin>('Insets', {
    // Tiny web fallback: returns zeros; real platforms are iOS/Android
    web: () => ({
        async get(): Promise<InsetsData> { return { top:0, bottom:0, left:0, right:0 }; },
        async addListener() { return { remove: () => {} }; }
    })
});

export * from './definitions';

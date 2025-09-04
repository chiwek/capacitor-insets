import { registerPlugin } from '@capacitor/core';
import type { CapacitorInsetsPlugin } from './definitions';

export const CapacitorInsets = registerPlugin<CapacitorInsetsPlugin>('Insets', {
    web: () => ({
        async get() { return { top:0, bottom:0, left:0, right:0 }; },
        async addListener() { return { remove: () => {} }; },
        async autoPad() { /* no-op on web */ }
    })
});
export * from './definitions';

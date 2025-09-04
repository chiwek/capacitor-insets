export interface InsetsData {
    top: number; bottom: number; left: number; right: number;
}

export interface CapacitorInsetsPlugin {
    /** One-shot read of current system bar/safe-area insets (keyboard excluded). */
    get(): Promise<InsetsData>;
    /** Emits on rotation/config changes, gesture vs 3-button switch, etc. */
    addListener(eventName: 'insetsChange', cb: (data: InsetsData) => void): Promise<{ remove: () => void }>;
}

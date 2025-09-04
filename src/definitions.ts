export interface InsetsData { top: number; bottom: number; left: number; right: number; }
export type AutoPadEdges = 'bottom' | 'top' | 'both';

export interface CapacitorInsetsPlugin {
    get(): Promise<InsetsData>;
    addListener(eventName: 'insetsChange', cb: (data: InsetsData) => void): Promise<{ remove: () => void }>;
    autoPad(options?: { enable?: boolean; edges?: AutoPadEdges }): Promise<void>;
}

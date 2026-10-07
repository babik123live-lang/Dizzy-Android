# NES world reconstruction

## Persistent object record
A persistent object is stored as ten bytes:

| byte | meaning |
|---|---|
| 0 | area id |
| 1 | x coordinate |
| 2 | sub-area / room id |
| 3 | y coordinate |
| 4-5 | little-endian sprite-data address |
| 6 | interaction id |
| 7-8 | little-endian item-description address |
| 9 | interaction sub-id |

Verified examples:
- Star Plant: `0F 2F 08 9E 63 8B 02 C8 82 03`
- Plank: `0E F4 01 A8 5C A5 1D F4 83 00`

This gives the remake a direct route to reconstructing item placement independently of the NES renderer.

## World topology
The NES game scrolls horizontally. Vertical travel changes room/sub-area; neighboring screens may deliberately repeat scenery. The remake therefore models each original area as world-space room strips and explicit exits rather than treating each 256x240 framebuffer as an immutable screen.

## Wide-screen rule
Adjacent geometry may be visible when it belongs to the same continuous strip. Room transitions that are logically separate remain explicit transitions. This prevents the wide camera from revealing unrelated rooms merely because the NES used screen-sized presentation.

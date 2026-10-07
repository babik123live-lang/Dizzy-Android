# Game specification

## Timing
Simulation clock: 50 ticks/second. Rendering is decoupled from simulation so 60/90/120 Hz Android displays cannot accelerate gameplay or audio.

## Camera
World units are independent of physical pixels. Reference vertical gameplay span is 240 world units. Camera width derives from device aspect ratio; a ~19.5:9 display exposes roughly 520 world units horizontally where world data permits it. No non-uniform stretching.

## Lives
User-facing values: 1..99 or infinity. Internal GameState stores lives as an integer; infinity is a separate boolean. Death never decrements lives when infinity is enabled.

## Localization
UTF-8 throughout. Polish glyph set required: ą ć ę ł ń ó ś ź ż Ą Ć Ę Ł Ń Ó Ś Ź Ż.

## Rendering
NES tiles/sprites are reference material only. Final renderer accepts replacement HD assets without changing collision geometry.

## Input
Touch D-pad and A/B controls are translucent overlays over gameplay. Input is state-based and sampled by the fixed simulation tick.

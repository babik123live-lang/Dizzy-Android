# Fantastic Adventures of Dizzy — Remake

Clean-room style remake workspace driven from the supplied Europe (En/Fr/De) ROM as behavioral/data reference.

## Fixed requirements
- no JSNES / no NES framebuffer as final renderer
- responsive wide camera; world geometry keeps its proportions
- HD/remastered renderer
- touch controls over the game
- deterministic 50 Hz gameplay simulation
- Polish localization with ą ć ę ł ń ó ś ź ż
- lives setting 1–99 and ∞
- Android APK target

## ROM facts
- iNES size: 262160 bytes
- PRG: 16 × 16 KiB = 256 KiB
- CHR: 0 banks (CHR-RAM)
- mapper: 71

## Reverse-engineering anchors
- lives RAM: $009A
- health: $00F2/$00F3
- oxygen: $009E
- frame/random counter observed by original logic: $0047
- English room-description text confirmed in PRG, including "YOU ARE IN GRAND DIZZY'S HUT NEAR A LARGE CAST IRON COOKING CAULDRON"

The remake code must model these systems independently rather than patching the emulator.

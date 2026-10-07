# NES world reconstruction

## Confirmed directly from the supplied Europe (En/Fr/De) ROM

The room-description table starts at PRG offset `0x19738`. It contains **49 room-description IDs**, each stored three times in the order English, French, German. Byte `FE` is a line break and `FF` terminates one language record.

This is now decoded by `tools/room_text_extract.py` and gives a verified index of the game's major locations: Yolkfolk treehouse village, countryside, Carber Bay, Crystal Falls, graveyard/caves, pirate ship, Keldor, clouds/Zak's castle, tunnels, diamond mines and troll castle.

## Persistent-object record research

External reverse-engineering documents a 10-byte object record layout (area, x, sub-area, y, sprite pointer, interaction id, description pointer, interaction sub-id). However, the two published example offsets are from a different ROM layout and **do not match the supplied European ROM**. They are retained only as structural clues; no object placement is considered verified until its byte sequence is found in this ROM.

## Wide-screen rule

The original game groups multiple horizontally connected rooms into areas. The remake will model those as continuous world strips where the ROM data proves continuity. Vertical or logical transitions remain explicit exits. This permits a genuinely wider camera without exposing unrelated rooms.

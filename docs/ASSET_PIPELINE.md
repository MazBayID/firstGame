# Cyber Protocol — Asset Pipeline

## Runtime resource layout

Use Android resource directories as the final runtime destination:

```text
app/src/main/res/
├── drawable/             # Vector XML icons, shapes, small scalable UI artwork
├── drawable-nodpi/       # Pixel-sized WebP/PNG artwork; no density rescaling
├── font/                 # Licensed font files and font family XML
├── mipmap-*/             # App launcher icon variants only
└── values/               # Resource strings, colors, and dimensions when appropriate
```

Editable source files and large working files should live outside runtime resources (for example in a separate design source archive), not be loaded by the application.

## Naming convention

Use lowercase snake_case and semantic names:
- cp_emblem
- bg_command_center
- character_recruiter_portrait
- character_recruiter_serious
- mission_card_active
- ic_nav_intelligence

Avoid temporary names such as final2, new_red, or generator output IDs.

## Format guidance

| Asset | Preferred format | Notes |
|---|---|---|
| Simple UI icons and marks | Vector drawable XML | Scales cleanly |
| Opaque background/scene | WebP | Optimize dimensions and quality |
| Character portrait with transparency | WebP or PNG | Preserve alpha |
| Complex illustration | WebP | Avoid oversized source resolution |
| Small pixel-perfect transparent detail | PNG | Use only when needed |
| Fonts | OTF/TTF + font XML | Verify license before adding |
| Short UI animation | Compose animation/vector | Prefer native drawing for simple effects |
| Character animation | Frame sequence/sprite sheet | Profile memory before selecting format |

## Asset manifest

Every imported asset should be documented with:
- Semantic resource name and repository path.
- Intended screen/character/scene.
- Pixel dimensions and format.
- Source/creator and license or generation provenance.
- Transparency and animation notes.
- Approximate file size.

## Integration rules

1. Keep UI copy outside images so localization remains possible.
2. Use CyberAssets for semantic references to replaceable drawable resources.
3. Prefer vectors for interface symbols; use raster images for actual illustration.
4. Load large artwork at display-appropriate size; do not decode full-resolution originals unnecessarily.
5. Keep decorative art separate from controls and touch targets.
6. Test portrait layouts on the target Android device after adding each major visual batch.
7. Do not add placeholder artwork that looks like final story canon.

## Initial asset slots

The first vector resources establish the emblem and six navigation symbols. Future illustration slots can be added under drawable-nodpi using the naming convention above. No character or story artwork is considered canon until approved in the Story Bible.

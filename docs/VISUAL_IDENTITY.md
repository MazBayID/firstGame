# Cyber Protocol — Visual Identity Guide

**Version:** 1.0  
**Status:** Initial approved art direction  
**Art direction:** Cinematic  
**Character style:** Anime  
**Color direction:** Obsidian / Charcoal / Crimson Red

## 1. Creative identity

Cyber Protocol is a cinematic cyber-intelligence RPG. Its visual language combines a grounded intelligence-thriller environment with expressive anime character artwork. The result should feel like a serious fictional organization with human characters—not a generic neon hacker dashboard.

Core mood:
- Mysterious, controlled, and professional.
- High-stakes without becoming horror.
- Futuristic but grounded in believable tools and spaces.
- Character-led storytelling with readable information hierarchy.

## 2. Color tokens

| Token | Hex | Intended use |
|---|---|---|
| Obsidian | #090A0E | App background |
| Charcoal | #15171D | Cards and primary surfaces |
| Panel | #1C1F27 | Nested surfaces |
| Crimson | #D62839 | Primary actions and active states |
| Blood Red | #8E1525 | Severe warnings and story danger |
| Soft White | #F1F1F3 | Primary text and icon artwork |
| Steel Gray | #858A96 | Secondary labels and inactive icons |
| Muted Text | #B2B5BE | Secondary body text |
| Divider | #292B33 | Subtle separators |
| Success | #55B887 | Verified/success states only |
| Warning | #E3A344 | Caution states only |

Approximate visual balance: 70% dark surfaces, 20% neutral text/surfaces, 10% accent color. Do not flood entire screens with red. Red is reserved for hierarchy, state, and emphasis.

## 3. Typography

- Display/headings: Rajdhani or Orbitron are candidates, subject to readability and font licensing.
- Body: Inter or Roboto.
- Code and operational identifiers: JetBrains Mono or a platform monospace fallback.
- Never bake UI labels or dialogue into raster artwork; render all interface text natively for localization and accessibility.

## 4. Character art direction

- Anime character rendering with consistent face, proportions, costume, and silhouette.
- Cinematic lighting and environment treatment.
- Modern functional clothing; red accents are selective, not universal uniforms.
- Each recurring character needs a model sheet before producing many scene illustrations.
- Required expression set for key cast: neutral, happy, serious, concerned, angry, surprised, thinking, speaking.
- Character names, biographies, and official organization identity remain subject to Story Bible approval.

## 5. UI principles

- Mobile-first, responsive Compose layouts.
- Obsidian background, charcoal cards, restrained borders.
- Crimson primary actions and selected states.
- Steel gray for secondary/inactive controls.
- Reserve Blood Red for danger, restricted access, or severe story events.
- Keep body text contrast high; never use glow as a substitute for contrast.
- Use subtle gradients, texture, and lighting only as supporting decoration.
- Maintain consistent spacing, corner radii, and semantic component states.

## 6. Asset governance

- Use semantic resource names, not names based on temporary concepts or generated filenames.
- Keep editable/source artwork outside Android runtime resources.
- Export optimized runtime files into app/src/main/res.
- Prefer vector XML for simple interface icons and marks.
- Prefer WebP for opaque illustrations/backgrounds; PNG only when alpha/transparency or lossless edges are required.
- Keep character animation frame sizes and naming consistent; choose sprite sheets or frame sequences only after profiling memory and decoding cost.
- Include provenance, license, author/source, dimensions, and intended use in the asset manifest.
- Do not commit unlicensed fonts, stock art, or generated art with unclear rights.

## 7. Non-goals

- No generic green hacker-terminal palette.
- No excessive neon glow, scanlines, or glitch on every screen.
- No text baked into interface backgrounds.
- No real-person likeness or allegations presented as real evidence.
- No large animation bundles before the Android memory/performance budget is measured.

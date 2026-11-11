# Cyber Protocol — UI Asset Catalog

All 48 raster UI assets are stored in app/src/main/res/drawable-nodpi/ as 128×128 PNG files. Use CyberAssets as the semantic resource registry instead of scattering raw drawable IDs through screens.

| Asset | Intended role |
|---|---|
| ui_home | Home navigation |
| ui_learn, ui_practice | Learning and practice entry points |
| ui_intelligence, ui_investigation, ui_intelligence_report | Intelligence and mission navigation |
| ui_operative, ui_organization, ui_command | Player and organization identity |
| ui_code, ui_terminal, ui_system | Coding and execution surfaces |
| ui_data_analysis, ui_database, ui_network, ui_connection, ui_server, ui_signal | Technical and analysis concepts |
| ui_evidence_file, ui_document, ui_archive, ui_financial_record, ui_digital_footprint | Investigation evidence |
| ui_encryption, ui_decryption, ui_firewall, ui_surveillance | Cyber-operation concepts |
| ui_experience, ui_level_up, ui_rank, ui_skill_tree, ui_unlock | Player progression |
| ui_achievement, ui_achievements, ui_special_achievement, ui_mission_completed | Achievements and completion states |
| ui_profile, ui_settings, ui_access_clearance | Profile and preferences |
| ui_field_operations, ui_intelligence_division, ui_investigation_division, ui_technical_division | Organization divisions |
| ui_corrupted_data, ui_warning_evidence, ui_verification, ui_search | Evidence states and investigation tools |

## Display rules

- Preserve the PNG source dimensions and use explicit Compose Modifier.size(...) values for predictable Android density-independent sizing.
- Use contentDescription = null for decorative icons when adjacent text already communicates the action.
- Keep text labels separate from image files; assets contain no baked-in labels.
- Launcher artwork is a separate asset and is not part of this 48-icon UI set.

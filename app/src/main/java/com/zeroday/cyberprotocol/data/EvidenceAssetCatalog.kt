package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.EvidenceOrigin

/** Immutable fictional source assets shared by Messenger, Browser, Terminal, and the evidence index. */
data class EvidenceAsset(
    val id: String,
    val evidenceId: String,
    val title: String,
    val path: String,
    val content: String,
    val sourceLabel: String,
    val origin: EvidenceOrigin
)

object EvidenceAssetCatalog {
    const val MISSION_ID = "first_contact.recruitment"
    val assets = listOf(
        EvidenceAsset(
            id = "asset_rx14_aegis_receipt_v1",
            evidenceId = "rx14_aegis_receipt",
            title = "A.E.G.I.S. Receipt Log — RX-14",
            path = "/A.E.G.I.S/evidence/first_contact/rx14_aegis_receipt.txt",
            content = "A.E.G.I.S. RECEIPT LOG\nREFERENCE: RX-14\nRECEIVED: 09:41:12\nCHANNEL: TRAINING-RELAY-01\nRECORD: LOCAL SIMULATION\n",
            sourceLabel = "A.E.G.I.S. Intranet / Receipt Log",
            origin = EvidenceOrigin.INTRANET
        ),
        EvidenceAsset(
            id = "asset_rx14_archive_receipt_v1",
            evidenceId = "rx14_archive_receipt",
            title = "Archive Receipt Log — RX-14",
            path = "/A.E.G.I.S/evidence/first_contact/rx14_archive_receipt.txt",
            content = "ARCHIVE RECEIPT LOG\nREFERENCE: RX-14\nRECEIVED: 09:41:16\nCHANNEL: ARCHIVE-INGEST-02\nRECORD: LOCAL SIMULATION\n",
            sourceLabel = "A.E.G.I.S. Archive / Receipt Log",
            origin = EvidenceOrigin.DATABASE
        )
    )
    fun find(assetId: String): EvidenceAsset? = assets.firstOrNull { it.id == assetId }

    /** Returns an asset only when Terminal successfully cats its exact canonical source path. */
    fun sourceAssetReadByTerminal(command: String, output: String): EvidenceAsset? {
        val normalized = command.trim()
        if (!normalized.startsWith("cat ")) return null
        val requestedPath = normalized.removePrefix("cat ").trim().removeSurrounding("\"").removeSurrounding("'")
        if (requestedPath.isBlank()) return null
        return assets.firstOrNull { it.path == requestedPath && it.content == output }
    }
}

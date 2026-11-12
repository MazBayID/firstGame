package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.GameLanguage

/** Presentation-only translations. Mission IDs, Python drafts, and evaluator contracts stay canonical. */
object Campaign01StoryLocalization {
    val englishBeats = listOf(
        StoryBeat("A.E.G.I.S. ORIENTATION", "A.E.G.I.S.  /  MANILA",
            "The invitation link brings you to the official orientation channel. A.E.G.I.S. is an independent international intelligence organization handling analysis, investigations, cyber operations, technical research, and field operations. Its headquarters are in Manila.\n\nThe invitation states that someone issued an order to recruit you. The identity of the person who gave that order is not included in the files you can access. The organization will not invent an answer just to fill that gap.",
            "REVIEW CLASSIFIED FILE"),
        StoryBeat("CLASSIFIED FILE 001", "RECRUITMENT NOTE  /  RESTRICTED",
            "The orientation file contains a brief assessment and a fragment of a training message. It does not explain who nominated you.\n\nOne sentence remains visible:\n\n“Potential is not the same as experience. This candidate has the first. The second can be learned.”\n\nYour first task is designed to test attention to detail, not prior experience as an agent.",
            "BEGIN FIRST TASK"),
        StoryBeat("FIRST TASK", "GUIDED DECRYPTION  /  LAYER 01",
            "A message fragment was found on a training channel. A.E.G.I.S. asks you to examine it as your first exercise.\n\nStore the fragment in a variable named signal, then use print() to display it. This is a fictional training cipher, not a real cryptographic security system.",
            "OPEN CODE EDITOR"),
        StoryBeat("SYSTEM VERIFICATION", "GUIDED DECRYPTION  /  LAYER 02",
            "The first signal reveals a verification message. Reuse the concepts of variables and output, but store the second message in a variable named message.\n\nFreya will explain the purpose of the task. After that, you must write and inspect the code yourself before submitting the result for verification.",
            "VERIFY MESSAGE"),
        StoryBeat("OFFICIAL RECRUITMENT", "A.E.G.I.S.  /  PERSONNEL",
            "Two orientation tasks have been verified. A.E.G.I.S. now offers you Recruit status with limited access. You receive a personnel profile and initial permission to use the training workspace.\n\nFreya confirms that your training will continue and that every task has a clear purpose.",
            "CONTINUE ORIENTATION"),
        StoryBeat("THE FIRST DISCREPANCY", "INTERNAL NOTE  /  UNVERIFIED",
            "During a routine review, you find two receipt records for one training transmission. Their message content is identical, but the times recorded by the two systems differ by several seconds.\n\nThat discrepancy does not prove tampering. Time zones, processing queues, or clock synchronization can produce differences. Your task is not to guess the cause—it is to examine the available data.",
            "BEGIN TRACE AUDIT"),
        StoryBeat("SYSTEM LOG REVIEW", "OPERATION 001-A  /  SYSTEM REVIEW",
            "NEXUS displays excerpts from the training channel logs. Some lines are marked ERROR, while others are informational or warnings.\n\nAn ERROR marker helps identify a system area that deserves review, but it does not automatically explain the RX-14 timestamp difference. Record the source context and do not alter the original logs.",
            "CONTINUE REVIEW"),
        StoryBeat("SOURCE INVENTORY", "OPERATION 001-B  /  SOURCE INVENTORY",
            "A.E.G.I.S. provides an inventory of receipt records from two systems. The list helps establish which references are available and where each record originated.\n\nYou do not need to write code for this phase. Focus on source identity, the RX-14 reference, and the timestamp information that can be verified.",
            "REVIEW SOURCES"),
        StoryBeat("INITIAL REPORT", "A.E.G.I.S.  /  REVIEW RESULTS",
            "The audit found several ERROR entries, and the inventory shows how many records were received by the second source. Both results are now recorded with their respective origins.\n\nThere is not yet enough evidence to conclude that someone changed the transmission receipt time. Freya asks that the report state the limits of the findings: what is directly visible, what is only an inference, and what remains unknown.\n\nThe first case is not closed. But you have learned something important: simple data can support different conclusions when the context of its source is ignored.",
            "CONTINUE INVESTIGATION"),
        StoryBeat("RX-14 REFERENCE INSPECTION", "OPERATION 001-C  /  RECEIPT TRACE",
            "Freya directs you to two records containing the RX-14 reference. Open the original sources through Operations and inspect the identity of the system that issued each record.\n\nDo not rely on summaries alone. The original content and its recorded origin are the basis of the investigation.",
            "OPEN OPERATIONS"),
        StoryBeat("TWO-SOURCE COMPARISON", "OPERATION 001-D  /  SOURCE COMPARISON",
            "Both sources are now available for inspection. They contain the same reference, but the recording systems and receipt times differ.\n\nCompare what is actually written in each source. Do not alter the records, and do not treat a timestamp difference as proof that either source was manipulated.",
            "CONTINUE ANALYSIS"),
        StoryBeat("TIMESTAMP ANALYSIS", "OPERATION 001-E  /  INTELLIGENCE ANALYSIS",
            "A.E.G.I.S. records 09:41:12, while the archive records 09:41:16. The four-second difference can be calculated from those two timestamps.\n\nThat number is a verifiable observation. Its cause remains open: clock synchronization, processing queues, or different recording mechanisms are possibilities, not established facts.",
            "REVIEW CONCLUSION"),
        StoryBeat("OPERATIONAL DECISION", "OPERATION 001-F  /  RESPONSE REVIEW",
            "The source inspection has not established why the two systems recorded different times. As an analyst, you must choose a response proportionate to the strength of the evidence.\n\nYou may record the anomaly, request clock-synchronization or processing-queue logs, or state that the current evidence is insufficient to determine its cause. You are not required to guess.",
            "CHOOSE AN ACTION IN OPERATIONS"),
        StoryBeat("INVESTIGATION REPORT", "A.E.G.I.S.  /  FIRST CONTACT REVIEW",
            "Two sources record the same transmission reference with receipt times four seconds apart. You can now demonstrate the difference using records with clear origins and indexes. This finding still does not prove tampering. The discrepancy may come from a processing queue, clock synchronization, or the recording mechanism. Freya accepts your report and flags the anomaly for further review when additional evidence becomes available. You complete First Contact not because every question has been answered, but because you have learned to distinguish facts, inferences, and unknowns.",
            "COMPLETE FIRST CONTACT")
    )

    fun localizedBeat(index: Int, language: GameLanguage): StoryBeat {
        require(index in Campaign01Story.beats.indices) { "Story beat index is out of range." }
        return if (language == GameLanguage.ENGLISH) englishBeats[index] else Campaign01Story.beats[index]
    }
}

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
        StoryBeat("SYSTEM TRACE AUDIT", "OPERATION 001-A  /  LOG REVIEW",
            "A.E.G.I.S. provides fictional log excerpts from the training system. Some lines contain ERROR, while the rest are informational or warnings.\n\nUse a for loop to inspect each line, then if to count ERROR entries. Do not alter or delete the source data. Record the correct count so the team can decide the next review step.",
            "OPEN AUDIT LOG"),
        StoryBeat("RECORD INVENTORY", "OPERATION 001-B  /  SOURCE CHECK",
            "The second source contains a list of record references received by another system. Before comparing their contents, first count how many references are available.\n\nUse len() to count the items in the records list, store the result in record_count, then display it. The number of items does not prove that every record is valid—it is only an initial inventory measure.",
            "OPEN INVENTORY"),
        StoryBeat("INITIAL REPORT", "A.E.G.I.S.  /  REVIEW RESULTS",
            "The audit found several ERROR entries, and the inventory shows how many records were received by the second source. Both results are now recorded with their respective origins.\n\nThere is not yet enough evidence to conclude that someone changed the transmission receipt time. Freya asks that the report state the limits of the findings: what is directly visible, what is only an inference, and what remains unknown.\n\nThe first case is not closed. But you have learned something important: simple data can support different conclusions when the context of its source is ignored.",
            "CONTINUE INVESTIGATION"),
        StoryBeat("REFERENCE TRACE", "OPERATION 001-C  /  RECEIPT TRACE",
            "Freya sends excerpts from receipt lists maintained by several sources. Find the line referring to RX-14 and preserve each line's position. Use enumerate() with for to obtain both the index and the item. An if condition helps select the correct reference. Do not infer the cause of the time difference from the list order alone.",
            "TRACE REFERENCE"),
        StoryBeat("TWO-SOURCE COMPARISON", "OPERATION 001-D  /  SOURCE COMPARISON",
            "The two relevant records have now been found. Retrieve the first and second entries from the receipts list using indexing, store them in primary and archive, then display both. Pay attention to the source and recorded time. This task only confirms that the records can be compared—it does not establish why they differ.",
            "COMPARE RECORDS"),
        StoryBeat("TIME NORMALIZATION", "OPERATION 001-E  /  TIMESTAMP NORMALIZATION",
            "The two records can now be read side by side, but their times are still text. A.E.G.I.S. provides seconds since midnight as numeric strings of equal length. Convert both values to integers using int(), store them as first_time and second_time, then calculate delta while preserving source order. A positive, zero, or negative value only indicates the direction of the recorded difference—not its cause.",
            "NORMALIZE TIMESTAMP"),
        StoryBeat("DIFFERENCE MAGNITUDE", "OPERATION 001-F  /  DELTA VALIDATION",
            "The report needs the magnitude of the difference regardless of which source recorded the earlier time. Use abs() on the subtraction of the two times after converting them to integers. Store the result in difference and display it. The absolute value helps compare the time gap; it does not explain whether the cause was tampering, queue delay, or clock synchronization.",
            "VALIDATE DIFFERENCE"),
        StoryBeat("INVESTIGATION REPORT", "A.E.G.I.S.  /  FIRST CONTACT REVIEW",
            "Two sources record the same transmission reference with receipt times four seconds apart. You can now demonstrate the difference using records with clear origins and indexes. This finding still does not prove tampering. The discrepancy may come from a processing queue, clock synchronization, or the recording mechanism. Freya accepts your report and flags the anomaly for further review when additional evidence becomes available. You complete First Contact not because every question has been answered, but because you have learned to distinguish facts, inferences, and unknowns.",
            "COMPLETE FIRST CONTACT")
    )

    fun localizedBeat(index: Int, language: GameLanguage): StoryBeat {
        require(index in Campaign01Story.beats.indices) { "Story beat index is out of range." }
        return if (language == GameLanguage.ENGLISH) englishBeats[index] else Campaign01Story.beats[index]
    }
}

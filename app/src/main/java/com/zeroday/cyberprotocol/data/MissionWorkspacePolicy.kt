package com.zeroday.cyberprotocol.data

/** Pure permission checks used by the mission workspace capability handle. */
internal object MissionWorkspacePolicy {
    fun canRead(fileName: String, allowedFiles: Set<String>): Boolean =
        fileName in allowedFiles

    fun canWrite(fileName: String, contentLength: Int, writableFiles: Set<String>): Boolean =
        fileName in writableFiles && contentLength in 0..50_000

    fun canInstallAsset(fileName: String, contentLength: Int, allowedFiles: Set<String>, writableFiles: Set<String>): Boolean =
        fileName in allowedFiles && fileName !in writableFiles && contentLength in 0..50_000
}

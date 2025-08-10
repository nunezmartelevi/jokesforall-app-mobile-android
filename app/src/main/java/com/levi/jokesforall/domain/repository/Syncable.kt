package com.levi.jokesforall.domain.repository

/**
 * Represents a contract for objects that can be synchronized.
 * Implementations of this interface should define the logic for synchronizing the local database
 * with the remote data.
 */
interface Syncable {
    suspend fun sync(): Boolean
}

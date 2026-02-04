package com.funteknoloji.quakesafe.data.dao

import androidx.room.*
import com.funteknoloji.quakesafe.data.entities.MeshNodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeshNodeDao {
    @Query("SELECT * FROM mesh_nodes")
    fun getAllNodes(): Flow<List<MeshNodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNode(node: MeshNodeEntity)

    @Query("DELETE FROM mesh_nodes WHERE lastSeen < :timeout")
    suspend fun pruneNodes(timeout: Long)
}

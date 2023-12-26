package com.finitas.domain.services

import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.ports.GetRoomsFromVersionsDto
import com.finitas.domain.ports.RoomRepository

class RoomService(private val roomRepository: RoomRepository) {
    suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto {
        return roomRepository.createRoom(createRoomDto)
    }

    suspend fun getRoomsFromVersions(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): List<RoomDto> {
        return roomRepository.getRoomFromVersion(getRoomsFromVersionsDto)
    }
}
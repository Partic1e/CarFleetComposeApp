package com.example.data.mapper

import com.example.data.model.tripslot.TripSlotResponseDto
import com.example.data.model.tripslot.WaypointDto
import com.example.domain.model.tripslot.TripSlot
import com.example.domain.model.tripslot.Waypoint

fun TripSlotResponseDto.toDomain(): TripSlot {
    return TripSlot(
        id = this.id,
        carId = this.carId,
        driverId = this.driverId,
        fromAddress = this.fromAddress,
        toAddress = this.toAddress,
        departureTime = this.departureTime,
        arrivalPlanned = this.arrivalPlanned,
        seatCapacity = this.seatCapacity,
        seatsTaken = this.seatsTaken,
        status = this.status,
        carDisplayName = this.carDisplayName ?: "Машина не назначена",
        driverFullName = this.driverFullName,
        passengers = this.passengers?.map { it.toDomain() } ?: emptyList(),
        fromLat = this.fromLat,
        fromLon = this.fromLon,
        toLat = this.toLat,
        toLon = this.toLon,
        waypoints = this.waypoints?.map { it.toDomain() } ?: emptyList()
    )
}

fun WaypointDto.toDomain(): Waypoint {
    return Waypoint(
        passengerId = this.passengerId,
        address = this.address,
        type = this.type,
        estimatedArrivalTime = this.estimatedArrivalTime,
        lat = this.lat,
        lon = this.lon
    )
}

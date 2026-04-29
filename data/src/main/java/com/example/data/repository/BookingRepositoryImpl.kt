package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.mapper.toDto
import com.example.data.model.booking.UpdateBookingRequestDto
import com.example.data.network.BookingApi
import com.example.domain.model.booking.Booking
import com.example.domain.model.booking.TripRequest
import com.example.domain.model.booking.TripRequestResult
import com.example.domain.model.booking.UpdateBooking
import com.example.domain.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingRepositoryImpl(
    private val api: BookingApi
) : BookingRepository {
    override suspend fun requestTrip(request: TripRequest): Result<TripRequestResult> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.requestTrip(request.toDto())

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.toDomain())
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Ошибка при создании заявки"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMyBookings(): Result<List<Booking>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMyBookings()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.map { it.toDomain() })
                } else {
                    Result.failure(Exception("Не удалось загрузить заявки"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateBooking(bookingId: String, params: UpdateBooking): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val requestDto = UpdateBookingRequestDto(
                    fromAddress = params.fromAddress,
                    toAddress = params.toAddress,
                    desiredDeparture = params.desiredDeparture,
                    seats = params.seats,
                    purpose = params.purpose
                )

                val response = api.updateBooking(bookingId, requestDto)

                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("HTTP Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun cancelBooking(bookingId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.cancelBooking(bookingId)
                if (response.isSuccessful) {
                    val success = response.body()?.success ?: false
                    Result.success(success)
                } else {
                    Result.failure(Exception("HTTP Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

package com.example.carfleetapp.presentation.di

import com.example.carfleetapp.BuildConfig
import com.example.carfleetapp.presentation.ui.auth.login.LoginViewModel
import com.example.carfleetapp.presentation.ui.auth.logout.LogoutViewModel
import com.example.carfleetapp.presentation.ui.auth.registration.RegisterViewModel
import com.example.carfleetapp.presentation.ui.driver.car.CarUpdateViewModel
import com.example.carfleetapp.presentation.ui.employee.request.CreateBookingViewModel
import com.example.carfleetapp.presentation.ui.driver.list.DriverTripSlotsViewModel
import com.example.carfleetapp.presentation.ui.employee.list.EmployeeBookingsViewModel
import com.example.carfleetapp.presentation.ui.profile.ProfileViewModel
import com.example.data.local.NotificationManager
import com.example.data.local.TokenStorage
import com.example.data.network.AuthApi
import com.example.data.network.AuthInterceptor
import com.example.data.network.BookingApi
import com.example.data.network.CarApi
import com.example.data.network.NotificationApi
import com.example.data.network.TrackingApi
import com.example.data.network.TripSlotApi
import com.example.data.network.UserApi
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.BookingRepositoryImpl
import com.example.data.repository.CarRepositoryImpl
import com.example.data.repository.TripSlotRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BookingRepository
import com.example.domain.repository.CarRepository
import com.example.domain.repository.TripSlotRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.booking.GetMyBookingsUseCase
import com.example.domain.usecase.booking.GetMyBookingsUseCaseImpl
import com.example.domain.usecase.tripslot.GetMySlotsUseCase
import com.example.domain.usecase.tripslot.GetMySlotsUseCaseImpl
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.auth.LoginUseCaseImpl
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.LogoutUseCaseImpl
import com.example.domain.usecase.auth.RegisterUseCase
import com.example.domain.usecase.auth.RegisterUseCaseImpl
import com.example.domain.usecase.booking.CancelBookingUseCase
import com.example.domain.usecase.booking.CancelBookingUseCaseImpl
import com.example.domain.usecase.booking.RequestTripUseCase
import com.example.domain.usecase.booking.RequestTripUseCaseImpl
import com.example.domain.usecase.booking.UpdateBookingUseCase
import com.example.domain.usecase.booking.UpdateBookingUseCaseImpl
import com.example.domain.usecase.car.GetCarUseCase
import com.example.domain.usecase.car.GetCarUseCaseImpl
import com.example.domain.usecase.car.SendToMaintenanceUseCase
import com.example.domain.usecase.car.SendToMaintenanceUseCaseImpl
import com.example.domain.usecase.car.UpdateCarDataUseCase
import com.example.domain.usecase.car.UpdateCarDataUseCaseImpl
import com.example.domain.usecase.user.GetProfileUseCase
import com.example.domain.usecase.user.GetProfileUseCaseImpl
import com.example.domain.usecase.user.UpdateProfileUseCase
import com.example.domain.usecase.user.UpdateProfileUseCaseImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { TokenStorage(context = androidContext()) }
    single { NotificationManager(get(), get()) }

    single { AuthInterceptor(tokenStorage = get()) }

    single {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor: AuthInterceptor = get()

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }
    single<BookingApi> { get<Retrofit>().create(BookingApi::class.java) }
    single<TripSlotApi> { get<Retrofit>().create(TripSlotApi::class.java) }
    single<UserApi> { get<Retrofit>().create(UserApi::class.java) }
    single<CarApi> { get<Retrofit>().create(CarApi::class.java) }
    single<NotificationApi> { get<Retrofit>().create(NotificationApi::class.java) }
    single<TrackingApi> { get<Retrofit>().create(TrackingApi::class.java) }

    single<AuthRepository> { AuthRepositoryImpl(api = get(), tokenStorage = get()) }
    single<BookingRepository> { BookingRepositoryImpl(api = get()) }
    single<TripSlotRepository> { TripSlotRepositoryImpl(api = get()) }
    single<UserRepository> { UserRepositoryImpl(api = get()) }
    single<CarRepository> { CarRepositoryImpl(api = get()) }

    single<LoginUseCase> { LoginUseCaseImpl(authRepository = get()) }
    single<RegisterUseCase> { RegisterUseCaseImpl(authRepository = get()) }
    single<LogoutUseCase> { LogoutUseCaseImpl(authRepository = get()) }
    single<RequestTripUseCase> { RequestTripUseCaseImpl(bookingRepository = get()) }
    single<GetMyBookingsUseCase> { GetMyBookingsUseCaseImpl(bookingRepository = get()) }
    single<CancelBookingUseCase> { CancelBookingUseCaseImpl(bookingRepository = get()) }
    single<UpdateBookingUseCase> { UpdateBookingUseCaseImpl(bookingRepository = get()) }
    single<GetMySlotsUseCase> { GetMySlotsUseCaseImpl(tripSlotRepository = get()) }
    single<GetProfileUseCase> { GetProfileUseCaseImpl(userRepository = get()) }
    single<UpdateProfileUseCase> { UpdateProfileUseCaseImpl(userRepository = get()) }
    single<GetCarUseCase> { GetCarUseCaseImpl(carRepository = get()) }
    single<UpdateCarDataUseCase> { UpdateCarDataUseCaseImpl(carRepository = get()) }
    single<SendToMaintenanceUseCase> { SendToMaintenanceUseCaseImpl(carRepository = get()) }

    viewModel { RegisterViewModel(registerUseCase = get(), tokenStorage = get()) }
    viewModel { LoginViewModel(loginUseCase = get(), tokenStorage = get()) }
    viewModel { LogoutViewModel(logoutUseCase = get()) }
    viewModel { CreateBookingViewModel(requestTripUseCase = get()) }
    viewModel { EmployeeBookingsViewModel(getMyBookingsUseCase = get(), cancelBookingUseCase = get(), updateBookingUseCase = get()) }
    viewModel { DriverTripSlotsViewModel(getMySlotsUseCase = get()) }
    viewModel { CarUpdateViewModel(getCarUseCase = get(), updateCarUseCase = get(), maintenanceUseCase = get(), getSlotsUseCase = get()) }
    viewModel { ProfileViewModel(getProfileUseCase = get(), updateProfileUseCase = get()) }
}

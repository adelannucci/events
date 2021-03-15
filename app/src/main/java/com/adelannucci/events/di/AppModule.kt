package com.adelannucci.events.di

import com.adelannucci.events.repository.EventRepository
import com.adelannucci.events.source.EventService
import com.adelannucci.events.ui.viewmodel.DetailsEventViewModel
import com.adelannucci.events.ui.viewmodel.ListEventViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_BASE = "http://5f5a8f24d44d640016169133.mockapi.io/api/"

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<EventService> { get<Retrofit>().create(EventService::class.java) }
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}

val viewModelModule = module {
    viewModel<ListEventViewModel> { ListEventViewModel(get()) }
    viewModel<DetailsEventViewModel> { DetailsEventViewModel(get()) }
}

val repositoryModule = module {
    single<EventRepository> { EventRepository(get()) }
}

val appModules = listOf(
    retrofitModule,
    viewModelModule,
    repositoryModule
)
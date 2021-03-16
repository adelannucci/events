package com.adelannucci.events.datasource

import androidx.annotation.StringRes

class ResultRequest<T>(
    val data: T? = null,
    @StringRes val error: Int? = null
)

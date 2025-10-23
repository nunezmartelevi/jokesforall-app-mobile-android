package com.levi.jokesmachine.domain.repository

class SyncingDataException(message: String = "Error syncing data") : Exception(message)
class NoInternetException(message: String = "No internet connection") : Exception(message)
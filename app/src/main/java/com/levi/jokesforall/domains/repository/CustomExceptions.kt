package com.levi.jokesforall.domains.repository

class SyncingDataException(message: String = "Error syncing data") : Exception(message)
class NoInternetException(message: String = "No internet connection") : Exception(message)
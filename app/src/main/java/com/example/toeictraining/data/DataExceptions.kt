package com.example.toeictraining.data

private const val MSG_DATA_NOT_FOUND = "Data not found in data source"
private const val MSG_DATA_INSERT_FAILED = "Insert data failed"
private const val MSG_DATA_DELETE_FAILED = "Delete data failed"

open class DataSourceException(message: String? = null) : Exception(message)

class DataNotAvailableException : DataSourceException(MSG_DATA_NOT_FOUND)

class DataInsertFailed : DataSourceException(MSG_DATA_INSERT_FAILED)

class DataDeleteFailed : DataSourceException(MSG_DATA_DELETE_FAILED)
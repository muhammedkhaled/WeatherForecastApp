package com.mukhaled.weatherforecastapp.core.data.api.model.mappers

interface ApiMapper<E, D> {
  fun mapToDomain(apiEntity: E): D
}
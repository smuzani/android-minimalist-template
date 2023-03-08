package com.template.randomuser.network

data class RandomUserResponse(
  val results: List<RandomUser>,
  val info: Info
)

data class Info(
  val seed: String,
  val results: Int,
  val page: Int,
  val version: String
)

data class RandomUser(
  val gender: String,
  val name: Name,
  val location: Location,
  val email: String,
  val login: Login,
  val dob: Dob,
  val registered: Registered,
  val phone: String,
  val cell: String,
  val id: Id,
  val picture: Picture,
  val nat: String
)

data class Dob(
  val date: String,
  val age: Int
)

data class Id(
  val name: String,
  val value: String
)

data class Location(
  val street: Street,
  val city: String,
  val state: String,
  val country: String,
  val postcode: String,
  val coordinates: Coordinates,
  val timezone: Timezone
)

data class Coordinates(
  val latitude: String,
  val longitude: String
)

data class Login(
  val uuid: String,
  val username: String,
  val password: String,
  val salt: String,
  val md5: String,
  val sha1: String,
  val sha256: String
)

data class Name(
  val title: String,
  val first: String,
  val last: String
)

data class Picture(
  val large: String,
  val medium: String,
  val thumbnail: String
)

data class Registered(
  val date: String,
  val age: Int
)

data class Street(
  val number: Int,
  val name: String
)

data class Timezone(
  val offset: String,
  val description: String
)
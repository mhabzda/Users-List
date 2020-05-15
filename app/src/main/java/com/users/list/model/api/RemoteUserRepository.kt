package com.users.list.model.api

import com.users.list.model.UserRepository
import com.users.list.model.api.entities.UserEntity
import com.users.list.model.api.entities.UserRepositoryEntity
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteUserRepository : UserRepository {
  private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com")
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  private val userApi = retrofit.create(UserApi::class.java)

  override fun retrieveUsers(): Single<List<UserEntity>> {
    return userApi.fetchUsers()
  }

  override fun retrieveUserRepository(userLogin: String): Single<UserRepositoryEntity> {
    return userApi.fetchUserRepository(userLogin)
  }
}
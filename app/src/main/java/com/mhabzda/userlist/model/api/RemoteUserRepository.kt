package com.mhabzda.userlist.model.api

import com.mhabzda.userlist.model.api.dtos.UserRemoteDto
import com.mhabzda.userlist.model.api.dtos.UserRepositoryRemoteDto
import com.mhabzda.userlist.model.api.mapper.UserRemoteMapper
import com.mhabzda.userlist.model.domain.UserEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import io.reactivex.rxkotlin.flatMapIterable
import javax.inject.Inject

class RemoteUserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userRemoteMapper: UserRemoteMapper,
) : RemoteRepository {
    override fun retrieveUsers(): Single<List<UserEntity>> =
        userApi.fetchUsers()
            .map { it.take(USERS_NUMBER) }
            .toObservable()
            .flatMapIterable()
            .flatMap(this::mergeUserWithRepositories, true)
            .flattenCompositeException()
            .toList()

    private fun mergeUserWithRepositories(userRemoteDto: UserRemoteDto): Observable<UserEntity> =
        Observable.zip(
            Observable.just(userRemoteDto),
            fetchRepositories(userRemoteDto)
        ) { user, repositories -> userRemoteMapper.map(user, repositories) }

    private fun fetchRepositories(user: UserRemoteDto): Observable<List<UserRepositoryRemoteDto>> =
        userApi.fetchUserRepository(user.login)
            .map { it.take(REPOSITORIES_NUMBER) }
            .toObservable()

    private fun <T> Observable<T>.flattenCompositeException(): Observable<T> =
        onErrorResumeNext { originalError: Throwable ->
            val resultError =
                if (originalError is CompositeException) originalError.exceptions.first() else originalError
            Observable.error(resultError)
        }

    companion object {
        private const val USERS_NUMBER = 30
        private const val REPOSITORIES_NUMBER = 3
    }
}
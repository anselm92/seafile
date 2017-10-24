package com.bwksoftware.android.seasync.domain.interactor

import com.bwksoftware.android.seasync.domain.AccountTemplate
import com.bwksoftware.android.seasync.domain.executor.PostExecutionThread
import com.bwksoftware.android.seasync.domain.executor.ThreadExecutor
import com.bwksoftware.android.seasync.domain.repository.Repository
import io.reactivex.Observable
import javax.inject.Inject


class GetAllAccounts @Inject
internal constructor(val repository: Repository, threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread) : UseCase<List<AccountTemplate>, GetAllAccounts.Params>(
        threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Observable<List<AccountTemplate>> {
        return Observable.create {
            subscriber ->
            for (account in params.listAccounts) {
                val avatarTemplate = repository.getAvatar(account.username,
                        params.authToken).blockingLast()
                account.imageUrl = avatarTemplate.url
            }
            subscriber.onNext(params.listAccounts)
            subscriber.onComplete()
        }
    }

    class Params(val authToken: String,
                 val listAccounts: List<AccountTemplate>)

}
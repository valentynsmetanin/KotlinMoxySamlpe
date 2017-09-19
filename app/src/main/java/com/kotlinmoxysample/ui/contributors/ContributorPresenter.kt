package com.kotlinmoxysample.ui.contributors

import com.arellomobile.mvp.InjectViewState
import com.kotlingithubapi.network.Api
import com.kotlingithubapi.network.RestClient
import com.kotlinmoxysample.R
import com.kotlinmoxysample.ui.BaseRxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Valentyn on 9/19/17.
 */
@InjectViewState
class ContributorPresenter : BaseRxPresenter<ContributorView>() {

    fun loadContributor(login : String?) {
        if(login.isNullOrEmpty()) return

        view?.showProgress(true)
        val d = RestClient().createService(Api::class.java)
                .getContributor(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            view?.showProgress(false)
                            viewState.showContributor(it)
                            Timber.d("Contributor $it")
                        },

                        onError = {
                            view?.showProgress(false)
                            view?.toast(R.string.err_something_wrong)
                            Timber.e("Contributors ${it.message}")
                        }
                )
        mDisposable?.add(d)
    }
}
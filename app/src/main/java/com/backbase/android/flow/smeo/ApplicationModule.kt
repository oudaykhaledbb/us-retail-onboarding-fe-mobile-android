package com.backbase.android.flow.smeo

import com.backbase.android.Backbase
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.flow.FlowClient
import com.backbase.android.flow.contracts.FlowClientContract
import com.backbase.android.flow.smeo.Constants.Companion.ABOUT_YOU_ACTION_INIT
import com.backbase.android.flow.smeo.Constants.Companion.ABOUT_YOU_Action_SUBMIT_ABOUT_YOU
import com.backbase.android.flow.smeo.Constants.Companion.DBS_PATH
import com.backbase.android.flow.smeo.Constants.Companion.INTERACTION_NAME
import com.backbase.android.flow.smeo.Constants.Companion.SERVICE_NAME
import com.backbase.android.flow.smeo.aboutyou.AboutYouConfiguration
import com.backbase.android.flow.smeo.aboutyou.aboutYouJourneyModule
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCase
import com.backbase.android.flow.smeo.aboutyou.usecase.AboutYouUseCaseDefaultImpl
import com.backbase.android.flow.smeo.walkthrough.walkthroughConfiguration
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.net.URI

/**
 * Koin module defining the app-level Authentication Journey configurations.
 */
val applicationModule = module {

    factory {
        walkthroughConfiguration {
            pages = arrayListOf(welcomePage(), requirementsPage(), informationPage(), businessNotAllowedPage(), termsAndConditionsPages())
        }
    }

    single {
        NetworkDBSDataProvider(get())
    }

    single<FlowClientContract> {
        val dbsProvider: NetworkDBSDataProvider by inject()
        FlowClient(
                get(),
                URI("${Backbase.getInstance()?.configuration?.experienceConfiguration?.serverURL}/$DBS_PATH/$SERVICE_NAME"),
                dbsProvider,
                null,
                INTERACTION_NAME,
                null
        )
    }

    factory<AboutYouUseCase> {
        return@factory AboutYouUseCaseDefaultImpl(get(), get())
    }

    factory {
        AboutYouConfiguration {
            isOffline = true
            actionInit = ABOUT_YOU_ACTION_INIT
            actionAboutYouSubmit = ABOUT_YOU_Action_SUBMIT_ABOUT_YOU
        }
    }

    loadKoinModules(aboutYouJourneyModule)

}
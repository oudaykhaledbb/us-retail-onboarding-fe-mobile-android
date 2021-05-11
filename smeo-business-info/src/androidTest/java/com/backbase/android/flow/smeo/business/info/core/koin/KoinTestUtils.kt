package com.backbase.android.flow.smeo.business.info.core.koin

import androidx.lifecycle.ViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

/**
 * Provide a [dependency] for the test. Typically called before the activity or fragment under test is launched.
 */
inline fun <reified D : Any> provide(
    name: String? = null,
    override: Boolean = false,
    crossinline dependency: Scope.(DefinitionParameters) -> D
) = loadKoinModules(
    module {
        val qualifier = if (name == null) null else named(name)
        factory(qualifier, override) { definitionParameters -> dependency(definitionParameters) }
    }
)

inline fun <reified V : ViewModel> provideViewModel(
    crossinline viewModel: Scope.(DefinitionParameters) -> V
) = loadKoinModules(
    module {
        viewModel { definitionParameters -> viewModel(definitionParameters) }
    }
)

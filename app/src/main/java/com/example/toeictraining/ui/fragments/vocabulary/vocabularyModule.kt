package com.example.toeictraining.ui.fragments.vocabulary

import com.example.toeictraining.di.ScopeNames
import org.koin.core.qualifier.named
import org.koin.dsl.module

val vocabularyModule = module {

    single(named(ScopeNames.CATEGORY_ADAPTER)) {
        CategoryAdapter(CategoryAdapter.CategoryDiffUtilCallback())
    }
}
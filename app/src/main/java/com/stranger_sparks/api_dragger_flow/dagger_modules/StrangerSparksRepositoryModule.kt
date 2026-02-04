package com.stranger_sparks.api_dragger_flow.dagger_modules

import com.stranger_sparks.api_dragger_flow.di.ApplicationModule
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.api_dragger_flow.retrofit.StrangerSparksApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StrangerSparksRepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(zoLabApiInterface: StrangerSparksApiInterface): StrangerSparksRepository {
        return StrangerSparksRepository(zoLabApiInterface)
    }
}

@Module
object StrangerSparksApiModule {
    @Provides
    @Singleton
    fun provideUsersApiService(): StrangerSparksApiInterface {
        return ApplicationModule.getRetroFitInstance().create(StrangerSparksApiInterface::class.java)
    }
}
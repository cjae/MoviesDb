/*
 * Copyright (C) 2015 Saúl Molinero.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esod.moviesdb;

import android.app.Application;

import com.esod.moviesdb.di.components.AppComponent;
import com.esod.moviesdb.di.components.DaggerAppComponent;
import com.esod.moviesdb.di.modules.ApplicationModule;
import com.esod.moviesdb.di.modules.DomainModule;

public class MoviesDbApp extends Application {

    private AppComponent mAppComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeDependencyInjector();
    }

    private void initializeDependencyInjector() {
        mAppComponent = DaggerAppComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .domainModule(new DomainModule())
            .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}

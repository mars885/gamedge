/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("ClassName", "ConstPropertyName")

/***
 * A central place to declare every local module. In case of a
 * rename, the name should be changed here only.
 */
object localModules {
    const val core = ":core"
    const val database = ":database"
    const val igdbApi = ":igdb-api"
    const val igdbApicalypse = ":igdb-apicalypse"
    const val gamespotApi = ":gamespot-api"
    const val commonDomain = ":common-domain"
    const val commonData = ":common-data"
    const val commonUi = ":common-ui"
    const val commonUiWidgets = ":common-ui-widgets"
    const val commonApi = ":common-api"
    const val commonTestingDomain = ":common-testing-domain"
    const val commonTesting = ":common-testing"

    const val featureCategory = ":feature-category"
    const val featureDiscovery = ":feature-discovery"
    const val featureInfo = ":feature-info"
    const val featureImageViewer = ":feature-image-viewer"
    const val featureLikes = ":feature-likes"
    const val featureNews = ":feature-news"
    const val featureSearch = ":feature-search"
    const val featureSettings = ":feature-settings"
}

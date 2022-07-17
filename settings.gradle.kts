rootProject.name = "Gamedge"

include(":app")
include(":core")
include(":common-domain")
include(":common-data")
include(":common-ui")
include(":common-ui-widgets")
include(":common-api")
include(":common-testing")
include(":database")
include(":igdb-api")
include(":igdb-apicalypse")
include(":gamespot-api")
include(":feature-category")
include(":feature-discovery")
include(":feature-info")
include(":feature-image-viewer")
include(":feature-likes")
include(":feature-news")
include(":feature-search")
include(":feature-settings")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "Gamedge"

include(":app")
include(":domain")
include(":core")
include(":commons-data")
include(":commons-ui")
include(":commons-ui-widgets")
include(":commons-api")
include(":commons-testing")
include(":data")
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
include(":feature-splash")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

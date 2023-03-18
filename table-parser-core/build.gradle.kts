plugins {
    id("io.github.shanepark.tableparser.kotlin-common-conventions")
}

dependencies {
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("org.jsoup:jsoup:1.15.4")
}
repositories {
    mavenCentral()
}

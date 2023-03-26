plugins {
    id("io.github.shanepark.tableparser.kotlin-common-conventions")
}

dependencies {
    api("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("org.jsoup:jsoup:1.15.4")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.0.4")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
}
repositories {
    mavenCentral()
}

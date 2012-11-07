mtgox-java
==========

A Java API for the MtGox bitcoin currency exchange.

Release versions are available from Maven Central, but you can get developer snapshot releases by adding the following to your maven pom.xml

    <repositories>
        ...
        <repository>
            <id>sonatype-oss-public</id>
            <url>https://oss.sonatype.org/content/groups/public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        ...
        <dependency>
            <groupId>to.sparks</groupId>
            <artifactId>mtgox</artifactId>
            <version>0.0.2-SNAPSHOT</version>
        </dependency>
    </dependencies>


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
            <version>0.0.3-SNAPSHOT</version>
        </dependency>
    </dependencies>

Below is an example of how to use the mtgox java API.

    // Pass your mtgox api key and secret as the command line args.
    MTGOXAPI mtgox = new MTGOXAPI(Logger.getGlobal(), args[0], args[1]);

    // Example of parsing mtgox public JSON sources, such as the ticker price
    Ticker ticker = mtgox.getTicker();
    logger.log(Level.INFO, "Last price: {0}", ticker.getLast());

    // Example of performing a private API function.
    double orderPrice = 1.00D; // $1.00
    double orderVolume = 1.00D; // 1 bitcoin
    mtgox.placeOrder(MTGOXAPI.OrderType.bid, orderPrice, orderVolume);

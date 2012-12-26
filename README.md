mtgox-java
==========

A Java API for the MtGox bitcoin currency exchange.  

Warning:  Testing has not yet been done for each currency to ensure that mtgox's various 'magic' mutlipliers work.  Use at own risk and double-check order sizes, prices and volumes are working for your currency before doing any large trades or using in a production environment.

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
            <version>0.0.4-SNAPSHOT</version>
        </dependency>
    </dependencies>

Below is an example of how to use the mtgox java API.

        // Create a $USD instance of the API
        MtGoxAPI mtgoxUSD = new MtGoxApiImpl(Logger.getGlobal(), Currency.getInstance("USD"), args[0], args[1]);

        // Example of parsing mtgox public JSON sources, such as the ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast());

        // Example of performing a private API function.
        double orderPrice = 1.00D; // $1.00
        double orderVolume = 1.00D; // 1 bitcoin
        mtgoxUSD.placeOrder(MtGoxApiImpl.OrderType.Bid, orderPrice, orderVolume);

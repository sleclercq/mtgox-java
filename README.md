mtgox-java
==========

A Java API for the MtGox bitcoin currency exchange based on Spring & Maven.

Warning:  Testing has not yet been done for each currency to ensure that mtgox's various 'magic' mutlipliers work.  Use at own risk and double-check order sizes, prices and volumes are working for your currency before doing any large trades or using in a production environment.

Release versions are available from Maven Central, add the following dependency to your pom.xml.

        <dependency>
            <groupId>to.sparks</groupId>
            <artifactId>mtgox</artifactId>
            <version>0.0.4</version>
        </dependency>

You can get developer snapshot releases by adding the following to your maven pom.xml

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
            <version>0.0.5-SNAPSHOT</version>
        </dependency>
    </dependencies>

Below is an example of how to use the mtgox java API.

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/Beans.xml");
        MtGoxAPI mtgoxUSD = (MtGoxAPI) context.getBean("mtgoxUSD");

        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getValue());
        
Private API functions need a MtGox.Com API key & secret passed as JVM system properties as shown below.
        
        java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET to.sparks.MtGoxExample

Contact the author grant@sparks.to for reporting bugs or asking questions.  Follow the mtgox-java project on GitHub so that I know who you are.
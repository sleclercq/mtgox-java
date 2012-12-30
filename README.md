mtgox-java
==========

A Java API (based on Spring & Maven) for the MtGox bitcoin exchange WebSocket & HTTP services.

See https://en.bitcoin.it/wiki/MtGox/API for details of the underlying protocols.

This software is copyright 2012-2013 Grant Sparks, and is distributed under the terms of the Lesser GNU General Public License (LGPL) included in the file LICENSE.TXT.

Warning:  Testing has not yet been done for each currency to ensure that mtgox's various 'magic' mutlipliers work.  Use at own risk and double-check order sizes, prices and volumes are working for your currency before doing any large trades or using in a production environment.

TODO:  The private HTTP API functions and the websocker subscription functions are not yet all there.  Send me a request if you need a particular function that is not yet there.

Release versions are available from Maven Central, add the following dependency to your pom.xml.

        <dependency>
            <groupId>to.sparks</groupId>
            <artifactId>mtgox</artifactId>
            <version>0.0.5</version>
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
            <version>0.0.6-SNAPSHOT</version>
        </dependency>
    </dependencies>

Below is an example of how to use the mtgox java API.

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/Beans.xml");
        MtGoxAPI mtgoxUSD = (MtGoxAPI) context.getBean("mtgoxUSD");

        // Get the private account info
        Info info = mtgoxUSD.getInfo();
        logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());

        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getValue());
        
Private API functions need a MtGox.Com API key & secret passed as JVM system properties as shown below.
        
        java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET to.sparks.MtGoxExample

Contact the author grant@sparks.to for reporting bugs or asking questions.  Follow the mtgox-java project on GitHub so that I know who you are.

    The MtGox-Java API is free software: you can redistribute it and/or modify
    it under the terms of the Lesser GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The MtGox-Java API is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    Lesser GNU General Public License for more details.

    You should have received a copy of the Lesser GNU General Public License
    along with the MtGox-Java API .  If not, see <http://www.gnu.org/licenses/>.

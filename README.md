mtgox-java
==========

A Java API (based on Spring & Maven) for the MtGox bitcoin exchange WebSocket & HTTP services.

See https://en.bitcoin.it/wiki/MtGox/API for details of the underlying protocols.

This software is copyright 2012-2013 Grant Sparks, and is distributed under the terms of the Lesser GNU General Public License (LGPL) included in the file LICENSE.TXT.

You can support this work via bitcoin donation to my tip jar.  1DdDs3rPR9W37cbF5zQbUB8D9frcbAAoYu
![Bitcoin tipjar QR code](https://raw.github.com/GrantSparks/mtgox-java/master/tipjar.png "bitcoin tipjar address")

Current status
--------------

Some of the private HTTP API functions and the websocket subscription functions are not yet fully implemented.  Send me a request if you need a particular function that is not available.  You can see what methods are implemented in the java API interface here... http://goo.gl/NxBG6

Using in your own project
-------------------------

Release versions are available from Maven Central, add the following dependency to your pom.xml.

        <dependency>
            <groupId>to.sparks</groupId>
            <artifactId>mtgox</artifactId>
            <version>0.1.3</version>
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
            <version>0.1.4-SNAPSHOT</version>
        </dependency>
    </dependencies>

Below is an example of how to use the mtgox java API.
```java
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/Beans.xml");
        // Obtain a $USD instance of the API
        MtGoxAPI mtgoxUSD = (MtGoxAPI) context.getBean("mtgoxUSD");
        
        // Get handles to the currencies we'll be using
        CurrencyInfo currencyInfo = mtgoxUSD.getCurrencyInfo(mtgoxUSD.getBaseCurrency());
        logger.log(Level.INFO, "Base fiat currency: {0}", currencyInfo.getCurrency().getCurrencyCode());

        // Example of getting the current ticker price
        Ticker ticker = mtgoxUSD.getTicker();
        logger.log(Level.INFO, "Last price: {0}", ticker.getLast().getPriceValue().getCredits());

        // Get the private account info
        AccountInfo info = mtgoxUSD.getAccountInfo();
        logger.log(Level.INFO, "Logged into account: {0}", info.getLogin());
```       
Private API functions need a MtGox.Com API key & secret passed as JVM system properties as shown below, or you can add them as maven properties in your pom.xml or settings.xml.
        
        java -Dapi.key=YOUR_KEY -Dapi.secret=YOUR_SECRET to.sparks.MtGoxExample

Compiling from source
---------------------

To build on Windows...

1.  Make sure you have a recent Java JDK installed & configured as normal.

2.  Download Maven.  (Check the downloaded file's properties and "unblock" it if you have windows protection turned on)
http://mirror.mel.bkb.net.au/pub/apache/maven/maven-3/3.0.4/binaries/apache-maven-3.0.4-bin.zip

3.  Extract the maven zip into anywhere, e.g., c:\tools\maven-3.0.4

4.  Edit your environment PATH to include the maven 'bin' directory.  e.g., c:\tools\maven-3.0.4\bin

5.  Clone the GitHub repo to your local computer and then open the project in GitHub for Windows and in the "Tools" menu, select "Open a shell here"

6.  In the command-line shell that appears, type the following maven command to build the project...

        mvn install

Contact
-------

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


[Project page url](http://goo.gl/OJ02G "mtgox-java project page home")

![Project page QR code](https://raw.github.com/GrantSparks/mtgox-java/master/qr.png "mt-gox java project page QR code")  


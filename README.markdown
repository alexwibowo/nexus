NEXUS ESB
==============

Enterprise Service Bus


Description
------------------

Nexus is a lightweight Enterprise Service. At its core, Nexus routes messages based on a set of configuration.


Building Nexus
------------------
Before you build anything, you will need to setup the HSQL database.

### Setup Database ###
Grab a copy of HSQLDB from their website. At the time of writing, the latest version of
HSQLDB is 2.2.9.

Run HSQLDB in **Server** mode. e.g:

```
$ java -cp hsqldb.jar org.hsqldb.server.Server --database.0 mem:nexus --dbname.0 nexus --port 9090
```

Note that the port does not have to be 9090. But if you change it, you will have to change *spring-persistence.xml* file.
I havent got time to externalize this configuration. Mea culpa.

Now, connect to this database from an external SQL browser. I personally use Squirrel SQL client.
The URL that you need to use for the example above is 

```
jdbc:hsqldb:hsql://localhost:9090/nexus;mem:nexus
```

Run the database initialization script, which is located at

```
<BASE>/nexus-gateway/src/main/resources/db/migrations/hsql/init-db.sql
```

### Run Maven build ###
Now you can run maven as usual. 

```
mvn clean install
```

Enjoy!



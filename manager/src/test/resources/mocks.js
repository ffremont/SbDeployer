db.microservices.insert({
    cluster : "myCluster",
    node : "myNodeA",
    name : "msClient",
    gav : {
        groupId : "com.github.ffremont.microservices.myWeather",
        artifactId : "myWeather",
        version : "1.0.0-SNAPSHOT"
    },
    version : "000000-00000000000-0000000",
    nsProperties : "ffremont.msClient",
    sha1 : "3ec7f710e583c02b90d05cd26f8a1b071ff7f559",
    etat : "Inactif"
});

db.properties.insert({
    name : "logging.level.com.github.ffremont",
    namespace : "ffremont",
    value : "DEBUG"
});
db.properties.insert({
    name : "logging.file",
    namespace : "ffremont.msClient",
    value : "/tmp/msClient.log"
});
db.properties.insert({
    name : "server.port",
    namespace : "ffremont.msClient",
    value : "7777"
});

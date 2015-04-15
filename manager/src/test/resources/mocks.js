db.microservices.insert({
    cluster : "myCluster",
    node : "myNodeA",
    name : "msClient",
    gav : {
        groupId : "com.github.ffremont.microservices.springboot",
        artifactId : "hello",
        version : "1.0.0-SNAPSHOT"
    },
    version : "mav1",
    properties : [],
    nsProperties : "ffremont.msClient",
    sha1 : "ce203810e12ee17860947472eb427d41769b72b3",
    etat : "Actif"
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

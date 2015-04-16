# SbDeployer

Application Java décomposée en 2 permettant de livrer / relivrer des micro services Spring boot.
- manager : micro service maître gérant les micro services et connecté à MongoDb
- node : micro service hébergé sur l'instance hébergeant les micro services

## Fonctionnalités
* Définition des microservices à déployer dans MongoDb
* Cibler le déploiement d'un microservice sur un instant précise
* Accès sécurisé (BASIC) au manager

## Pré-requis
* MongoDb
* Une instance pour héberger le micro service "manager"
* Des instances pour héberger les micro services "node"


## Manager
### Installation
```bash
$ wget "https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=com.github.ffremont.microservices.springboot&a=manager&v=X.X.X&e=zip&c=package" -O manager_X.X.X.zip
$ unzip manager_X.X.X.zip
$ java -jar manager_X.X.X.jar
```
### Configuration
 * Fichier de configuration "config/application.properties"
```properties
server.port=8888

logging.file=logs/sb-manager.log

# Nexus config
nexus.baseurl=http://nexus.local:8081/nexus
# Nexus repo used
nexus.repo[0]=snapshots
nexus.repo[1]=releases

# Security access
app.security.users.admin.pwd=secret
app.security.users.admin.role=ADMIN
app.security.users.nodeUser.pwd=azerty
app.security.users.nodeUser.role=USER

spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=test

error.whitelabel.enabled=false
```
### API Rest
 * http://host:port/manager
 * Micro service (ressource) : /clusters/{clusterName}/nodes/{nodeName}/microservices
   * **/** GET [ADMIN, USER] : Retourne la liste des microservices
   * **/{msName}** GET [ADMIN, USER] : Retourne un micro service

  TODO

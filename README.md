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
 * http://host:port/manager/clusters/{clusterName}/nodes/{nodeName}/microservices
 
#### Récupération des micro services
 * Description : Retourne la liste de tous les micro services du cluster
 * Verbe : GET
 * Accept : application/vnd.microservice+json
 * Chemin : /
 * Habilitation : ADMIN , USER
 * Retour : MicroServiceRest[]
 
#### Récupération d'un micro service
 * Description : Retourne les données sur le micro service
 * Verbe : GET
 * Accept : application/vnd.microservice+json
 * Chemin : /{msName}
 * Habilitation : ADMIN , USER
 * Retour : MicroServiceRest[]
 
#### Récupération du jar
 * Description : Retourne le jar venant de Nexus
 * Verbe : GET
 * Accept : application/java-archive
 * Chemin : /{msName}/binary
 * Cache : cache par validation avec etag
 * Habilitation : ADMIN , USER
 * Retour : le binaire (content-type = application/java-archive)
  
#### Récupération du fichier de propriétés
 * Description : Retourne le contenu du fichier de propriété. Il contient les properties du "namespaceProperties" du micro service. Cette liste peut être affimée via une "whitelist" qui se nomme "properties". Si la liste est null, tout le namespace sera utilisé.
 * Verbe : GET
 * Accept : text/plain"
 * Chemin : /{msName}/properties
 * Cache par expiration : 86400
 * Habilitation : ADMIN , USER
 * Retour : le binaire (content-type = application/java-archive)

#### Ajout d'un micro service
 * Description : Permet d'ajouter un micro service à un node sur un cluster.
 * Verbe : POST
 * Contenu : json de l'objet "MicroServiceRest"
 * Accept : application/vnd.microservice+json
 * Chemin : /
 * Habilitation : ADMIN 
 * Retour : code 200

#### Modification d'un micro service
 * Description : Permet de modifier un micro service existant.
 * Verbe : PUT
 * Contenu : json de l'objet "MicroServiceRest"
 * Accept : application/vnd.microservice+json
 * Chemin : /{msName}
 * Habilitation : ADMIN 
 * Retour : code 200
 
#### Suppression d'un micro service
 * Description : Permet de supprimer un micro service sur un node d'un cluster. Le micro service doit être "inactif" depuis au moins 3 minutes pour être supprimé.
 * Verbe : DELETE
 * Contenu : json de l'objet "MicroServiceRest"
 * Chemin : /{msName}
 * Habilitation : ADMIN 
 * Retour : code 200

## Node
### Installation
```bash
$ wget "https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=com.github.ffremont.microservices.springboot&a=node&v=X.X.X&e=zip&c=package" -O node_X.X.X.zip
$ unzip node_X.X.X.zip
$ java -jar node_X.X.X.jar
```
### Configuration
 * Fichier de configuration "config/application.properties"
```properties
server.port=8889

# debug
logging.level.com.github.ffremont=DEBUG

# Possibilité de définir la version de java à utiliser, sinon JAVA_HOME par défaut
#app.java.exec=/home/florent/Applications/jdk1.8.0_20/bin/java

logging.file=logs/sb-manager.log
logging.config=config/logback.xml

# Master config
app.master.host=localhost
app.master.port=8888
app.master.contextRoot=manager

# Master access
app.master.user=nodeUser
app.master.pwd=azerty

# Node config
app.node=myNodeA
app.cluster=myCluster
# Répertoire où seront stockés les livrables
app.base=/tmp/testSbNode

error.whitelabel.enabled=false
```

# Manager Spring Boot

## Exposition REST

                MicroService
GET             :   /clusters/{clName}/nodes/{nodeName}/ms
GET             :   /clusters/{clName}/nodes/{nodeName}/ms/{appName}
POST            :   /clusters/{clName}/nodes/{nodeName}/ms/{appName}
PUT             :   /clusters/{clName}/nodes/{nodeName}/ms/{appName}
DEL             :   /clusters/{clName}/nodes/{nodeName}/ms/{appName}

                Properties
GET             :   /clusters/{clName}/nodes/{nodeName}/ms/{appName}/properties

                Binary
GET             :   /clusters/{clName}/nodes/{nodeName}/ms/{appName}/binary

                Property
GET             :   /properties/{namespace}/{name}
POST            :   /properties/{namespace}/{name}
DEL             :   /properties/{namespace}/{name}
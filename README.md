# version2
# Iniciar aplicación
1. Clonar base de datos por problema de las migraciones
2. Configurar archivo de propiedades (es un branch .modulosunio)
3. Configurar archivo .factura (copiar branch)
4. Clonar dependencia de workspace
5. Crear base de datos con docker

```docker run --name mysql-5.5 -e MYSQL_ROOT_PASSWORD=makingdevs -e MYSQL_USER=makingdevs -e MYSQL_PASSWORD=makingdevs -e MYSQL_DATABASE=m1_dev -p 3306:3306 -d mysql:5.5```
- Conectar a base

```docker run -it --name modulusuno --link mysql-5.5:mysql --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -umakingdevs -p' ```
- Correr migración

```docker exec -i mysql-5.5 mysql -uroot -pmakingdevs m1_dev < ~/Downloads/m1_qa_080119.sql```

Notas
- En stage necesita un rabbit
- Si tiene problemas para bajar la dependencia workspace deberas copiarla manualmente en la ubicación que la esta pidiendo

## Rabbit

- docker run -d --hostname modulusuno --name m1_rabbit_management -p 15672:15672 -p 5672:5672 rabbitmq:3-management
- Tienes que agregar la configuración en web como en api para el archivo de propiedades
- Recuerda que el username y password de rabbit por default es guest

## Facturacioón
- Clonar proyecto apartit de M1-modulus
- Proyecto en M1 corre con gradle jettyRun
- Cambiar properties a development

# Versiones

* Gradle 3.4.1
* Grails Version: 3.2.11
* Groovy Version: 2.4.11

# Logs
tail -f /opt/tomcat_web/logs/host-manager.2020-01-28.log

tail -f /opt/tomcat_web/logs/localhost.2020-01-28.log

tail -f /opt/tomcat_web/logs/modulusuno.log



tail -f ~/tomcat/logs/catalina.2020-01-28.log # también depende del día

tail -f ~/tomcat/logs/facturacion-2020-01.log # este depende del mes

tail -f ~/tomcat/logs/localhost.2020-01-28.log # en casos de que haya algo mal con el XML este te lo va a decir
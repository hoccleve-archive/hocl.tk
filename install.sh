#!/bin/sh

mvn clean
mvn -e package
cp target/my-webapp.war /var/lib/tomcat7/webapps/

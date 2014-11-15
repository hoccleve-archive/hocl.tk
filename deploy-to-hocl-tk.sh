#!/bin/sh

mvn clean
mvn -e package
rsync target/my-webapp.war $USER@hocl.tk:/var/lib/tomcat7/webapps/

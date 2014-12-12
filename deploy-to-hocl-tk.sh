#!/bin/sh

mvn clean
mvn -e package
rsync target/hocl.war $USER@hocl.tk:/var/lib/tomcat7/webapps/

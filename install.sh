#!/bin/sh

mvn clean
mvn -e package
cp target/hocl.war /var/lib/tomcat7/webapps/

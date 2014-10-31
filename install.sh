#!/bin/sh

mvn package
sudo cp target/my-webapp.war /var/lib/tomcat7/webapps/

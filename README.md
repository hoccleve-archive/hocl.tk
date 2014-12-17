hocl.tk
=======

Java servlet and JSP based version of hocl.tk

This is meant to replace the Play Framework version of hocl.tk. It has the advantage that it can run in any Java servlet container, doesn't have as many up-front dependencies as a Java Play Framework project, and doesn't have to work around Scala.

See `src/main/java/controllers` for the meat of the application. The XSLT files are in `src/main/resources` and get called by the controllers.

Rationale
---------
hocl.tk provides a set of XSL Transformations and means to access them over the web. An important aspect of hocl.tk is share-ability: you can share documents generated with a public hocl.tk server using just a URL and embed generated documents wherever you like.

The stylesheets that define transformations used on a hocl.tk server are also made public so that they can be downloaded when used.

Build
-----
You'll need the [XSLTXT library](https://github.com/mwatts15/xsltxt) which isn't in Maven Central repository. You need to either deploy it to your group's maven repository and configure (in the `pom.xml` file) where that repository is, or install it in the local repository where you build hocl.tk. Aside from that, building the web app should be as easy as:
    
    mvn clean install

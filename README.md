hocl.tk
=======

Java servlet and JSP based version of hocl.tk

This is meant to replace the Play Framework version of hocl.tk. It has the advantage that it can run in any Java servlet container, doesn't have as many up-front dependencies as a Java Play Framework project, and doesn't have to work around Scala.

See `src/main/java/controllers` for the meet of the application. The XSLT files are in `src/main/webapp` and get called by the controllers.

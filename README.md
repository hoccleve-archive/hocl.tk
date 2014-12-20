hocl.tk
=======

Rationale
---------
hocl.tk provides a set of XSL Transformations for [TEI](http://tei-c.org/) documents and the means to access them over the web. An important aspect of hocl.tk is share-ability: you can share documents generated with a public hocl.tk server using just a URL and embed generated documents wherever you like. This only works, however, when the source documents have URLs of their own.

The transformations performed by the server are:

- tei-html : Present a TEI document as HTML. Currently only works with poems.
- tei-numbers : Adds line numbers to a lined TEI document.
- include-interp : Combines a analysis and source text into a unified document. Currently only works with [TEI `spanGrp`](http://www.tei-c.org/release/doc/tei-p5-doc/en/html/ref-spanGrp.html) elements.
- ctable : Creates, from an analysis text encoded in TEI, a concordance table, which is an association between locations in a text and the salient features of that part of the text. The table is in an [XML format](src/main/resources/ctable.rng), called CTable, created for this purpose.
- ctable-html : Creates an HTML document from a CTable document displaying the table.

Each transformation is accessed through the server like:

    <server-root>/<transformation-name>?q=<source-document>&<param1>=<value1>&<param2>=<value2>

where `<source-document>` is a URL to the source document (an XML file) of the transformation. 

The transformation names are listed in `src/main/webapp/WEB-INF/web.xml`, but should be the same as the transformations listed above.

Build
-----
You'll need the [XSLTXT library](https://github.com/mwatts15/xsltxt) which isn't in Maven Central repository. You need to either install it to your group's maven repository and configure (in the `pom.xml` file) where that repository is, or install it in the local repository where you build hocl.tk. Aside from that, building the web app should be as easy as:
    
    mvn clean install

The application can be deployed to any Java Servlets container. You should follow the documentation for the container you intend to use. [Apache Tomcat](https://tomcat.apache.org/tomcat-7.0-doc/index.html) is an easy to setup and use container with ample documentation.

Additional documentation can be found at hocltk.readthedocs.org or in the `docs` subdirectory of this project.

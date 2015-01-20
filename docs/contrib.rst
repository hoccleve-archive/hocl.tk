.. _contrib:

=======================
Contributing to hocl.tk
=======================

XSLT Recommendations
--------------------
This project is largely composed of XSLT documents. The most useful and 
authoritative reference for XSLT is the `W3C XSL Transformations recommendation 
<http://www.w3.org/TR/xslt>`_. Below is a list of recommendations for authoring 
XSLT documents (called "stylesheets") in this project.

Testing and Debugging
~~~~~~~~~~~~~~~~~~~~~
I strongly recommend using a command line tool, or a simple GUI if you prefer, 
to develop and debug stylesheets. Re-deploying the application just to test 
simple changes to a stylesheet is huge waste of time. As long as you make sure 
any extensions you use are supported by your command line tool and the Java 
XSLT library (they may even be the same), then you shouldn't have any problems. 
I use xsltproc for this purpose.

HTML Recommendations
--------------------
For HTML used in the project, try to make the pages readable without extensive 
use of Javascript and CSS. In general, it is better to support a wide range of 
readers, some of which may not be modern personal computers or which may have 
Javascript disabled or which just display differently. For example, the table 
format of the poem presented by the ``TEIHtml`` transformation still holds up 
with or without the Javascript.

Naming
~~~~~~

These rules help to keep the relationships between resources and code from 
becoming obscured. They do not, however, affect functionality.

The stylesheet should have a base name, (i.e., file name, excluding the 
``.xslt`` extension), matching the last element of the associated URL path. The 
URL path is determined either by the ``WebServlet`` annotation on a 
``XSLTTransformer`` subclass or in the web.xml_ file. In addition, the main 
stylesheet associated with an ``XSLTTransformer`` should have a file name that 
corresponds to the ``XSLTTransformer`` subclass name like in these examples::

    tei-html.xslt --> TEIHtml
    table-to-tei.xslt --> TableToTEI
    include-author-date.xslt --> IncludeAuthorDate
    add-ids.xslt --> AddIDs

.. _web.xml: https://github.com/hoccleve-archive/hocl.tk/blob/master/src/main/webapp/WEB-INF/web.xml

In other words:

- remove the hyphens,
- capitalize individual words,
- and for abbreviations that are typically in all caps, put them in all caps in 
  the class name, but not in the stylesheet name.

For a stylesheet which takes parameters, a particular ``XSLTTransformer`` may 
be a parameterized version of that stylesheet. In this case, the name of the 
``XSLTTransformer`` does not need to have the correspondence with the 
stylesheet like that described above.

Adding transformations
----------------------
To add a document transformation, you have to do three things:

1. Make the stylesheet_ and put it in the `resources folder`_.
2. Make the servlet_ corresponding to the stylesheet and put it in the 
   `controllers folder`_.
3. Set the URL pattern for the servlet either using the `WebServlet 
   annotation`_ (`servlet API version`_ >= 3.0) or in the web.xml_ file.

.. _stylesheet: https://github.com/hoccleve-archive/hocl.tk/blob/master/src/main/resources/tei-html.xslt
.. _resources folder: https://github.com/hoccleve-archive/hocl.tk/tree/master/src/main/resources
.. _servlet: https://github.com/hoccleve-archive/hocl.tk/tree/master/src/main/java/com/mycompany/app/controllers/TEIHtml.java
.. _controllers folder: https://github.com/hoccleve-archive/hocl.tk/tree/master/src/main/java/com/mycompany/app/controllers
.. _WebServlet Annotation: https://github.com/hoccleve-archive/hocl.tk/blob/0e4d1fe57da912575b528074bab5be5eeda51d45/src/main/java/controllers/TEIHtml.java#L10
.. _servlet API version: https://github.com/hoccleve-archive/hocl.tk/blob/0e4d1fe57da912575b528074bab5be5eeda51d45/pom.xml#L20
.. _web.xml: https://github.com/hoccleve-archive/hocl.tk/blob/master/src/main/webapp/WEB-INF/web.xml

Source code
~~~~~~~~~~~
See ``src/main/java/controllers`` for the Java web servlets and the 
transformation. The XSLT files are in ``src/main/resources`` and get called 
by the sub classes of XSLTTransformer.

Javadoc comments should be added to every class and to private members as well 
as public as these are for internal documentation.

Subclasses of XSLTTransformer should only include documentation for the 
transformation as whole, and not refer to implementation. As an exception, 
special use of URL query parameters beyond the default name-based usage (e.g., 
URL parameter 'g' corresponds to transform parameter 'g'), should be documented 
on the subclass.

Specifications
~~~~~~~~~~~~~~
In describing requirements, please utilize the keywords defined in `RFC 2119 
<http://tools.ietf.org/html/rfc2119>`_ .

.. _todo:

Things to do
------------
A longer list of to-dos and considerations is given in the TODO file in the root
of the hocl.tk project directory. The list below is intended to highlight 
immediate needs for the project that may or may not be listed in the TODO file
or as Github issues (yet!).

1. Make a Wordpress plugin for showing the "Regiment of Princes" time referents
   in a concordance table on the Hoccleve Archive `Wordpress site`_.

2. Write more transformations for creating CTables and HTML documents from TEI.
   See issue `#11`_.

3. Harden server against abuses from users. Some problems to start on are listed
   `on Github`_.

.. _#11: https://github.com/hoccleve-archive/hocl.tk/issues/11
.. _on Github: https://github.com/hoccleve-archive/hocl.tk/issues/12
.. _Wordpress site: http://hocclevearchive.org/

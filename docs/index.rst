===========================
Hoccleve Concordance Tables
===========================

This is the documentation for the `Hoccleve Archive`_ `concordance table subproject`_. The project is `hosted on github`_ as hocl.tk. This project uses a combination of XSLT, Java Server Pages, and Java WebServlets to modify and present documents composed in TEI_ XML markup.

This documentation is composed in rst_.

.. _rst: http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html
.. _hosted on github: https://github.com/hoccleve-archive/hocl.tk
.. _TEI: http://www.tei-c.org/index.xml
.. _Hoccleve Archive: http://hocclevearchive.org/hocclevearchive/
.. _concordance table subproject: http://hocclevearchive.org/hocclevearchive/time-references/

XSLT Recommendations
--------------------
This project is largely composed of XSLT documents. The most useful and authoritative reference for XSLT is the `W3C XSL Transformations recommendation <http://www.w3.org/TR/xslt>`_. Below is a list of recommendations for authoring XSLT documents (called "stylesheets") in this project.

1. The stylesheet should have a basename, excluding the extension, matching the last element of the associated URL path. The URL path is determined by the WebServlet annotation on a XSLTTransformer subclass.

HTML Recommendations
--------------------
For HTML used in the project, try to make the pages readable without extensive use of Javascript and CSS. In general, it is better to support a wide range of readers, some of which may not be modern personal computers or which may have Javascript disabled or which just display differently.

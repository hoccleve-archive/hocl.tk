.. _tei-elements:

====================
Supported TEI markup 
====================

The TEI standard is large and composed of many modules, not all of which are required or expected to be used in a given project. The TEI Consortium recommends for users of TEI that they define a subset of TEI markup which is supported by their application. For our case, the important thing is to be able to identify when there is an error in an XSL Transformation (XSLT) and to tell the user what that error is. For performing an XSL transformation, it is not required to match a schema exactly as long as we can process the document, so writing a schema for the subset of TEI which we support is unnecessary. Focus should, instead,  be placed on generating appropriate error messages and warnings in the XSLT.

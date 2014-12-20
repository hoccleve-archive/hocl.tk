.. _docs:

=========================
Documentation for hocl.tk
=========================

This documentation is composed in reStructuredText_ and is hosted on `Read the Docs`_. 

.. _reStructuredText: http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html
.. _Read the Docs: http://hocltk.readthedocs.org/en/latest/


Adding documentation
---------------------
Documentation for hocl.tk is housed in two locations: 

    #. In the top-level project directory as :file:`README.md`. 
    #. As a `Sphinx <http://sphinx-doc.org/>`_ project under the ``docs`` directory 

To add a page about supported TEI document elements to the documentation, include an entry in the list under ``toctree`` in :file:`docs/index.rst` like::

    tei-elements

and create the file :file:`tei-elements.rst` under the :file:`docs` directory and add a line::

    .. _tei-elements:

to the top of your file, remembering to leave an empty line before the rest of your text.

You can get a preview of what your documentation will look like when it is published by running ``sphinx-build`` on the docs directory::

    sphinx-build -w sphinx-errors docs <build_destination>

The docs will be compiled to html which you can view by pointing your web browser at :file:`<build_destination>/index.html`. If you want to view the documentation locally with the `ReadTheDocs theme <https://github.com/snide/sphinx_rtd_theme>`_ you'll need to download and install it.

API Documentation
------------------
Documentation is included in Java source files as Javadoc comments (the ones that start with ``/**``) and can be built locally with the Maven ``javadoc:javadoc`` goal::

    mvn javadoc:javadoc

Substitutions
-------------
Project-wide substitutions can be (conservatively!) added to allow for easily changing a value over all of the documentation. Currently defined substitutions can be found in :file:`docs/conf.py` in the ``rst_epilog`` setting. `More about substitutions <http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html#substitution-definitions>`_ is provided in the reStructuredText documentation.

Conventions
-----------
If you'd like to add a convention, list it here and start using it.

Currently there are no real conventions to follow for documentation style, but additions to the docs will be subject to style and content review by project maintainers.

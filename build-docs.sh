javadoc -sourcepath src/main/java -d api-docs.build @java-docs/javadoc-packages @java-docs/javadoc-args
sphinx-build -w sphinx-errors docs prose-docs.build

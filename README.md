
Description
===========

This program is a backend to [BystroTeX](http://andreimikhailov.com/slides/bystroTeX/slides-manual/index.html),
a server to run on the `localhost`. It accepts a `LaTeX` string as a query in the `POST` request
to `http://localhost/svg` returning the `SVG` file.

A sample client `sample-client.rkt` is included.

Building
========

You need to install the program called `sbt` and another program called `mvn`.
Then (in the same directory where this `README.md` is located) execute the following commands:

    git submodule init
    git submodule update

    cd jlatexmath/
    mvn install
    cd ..
    sbt stage

Running
=======

    target/universal/stage/bin/latex2svg -DbystroFile=/path/to/bystroConf.xml -Dhttp.port=9749 -Dhttp.address=127.0.0.1 -Dpidfile.path=/tmp/latex2svg.pid

The file `/path/to/bystroConf.xml` will be auto-generated

Updating
========

It seems that `JLaTeXMath` is under active development. To use the latest version:

    cd jlatexmath/
    mvn clean
    git pull
    git checkout master
    mvn install

    cd ..
    sbt clean
    sbt stage



Description
===========

A server to run on the `localhost`. It accepts a `LaTeX` string as a query in the `POST` request
to `http://localhost/svg` returning the `SVG` file.

A sample client `sample-client.rkt` is included.

Building
========

You need to install the program called `sbt`. Perhaps the easiest way (on Debian) is to download
the latest `.deb` file from [bintray](https://dl.bintray.com/sbt/debian/) and do `dpkg -i ...`

Then say (in the same directory where this `README.md` is located):

    sbt stage

Running
=======

    target/universal/stage/bin/latex2svg -Dhttp.port=9749 -Dhttp.address=127.0.0.1 -Dpidfile.path=/tmp/latex2svg.pid


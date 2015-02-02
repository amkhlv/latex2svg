
Description
===========

A server to run on the `localhost`. It accepts a `LaTeX` string as a query in the `POST` request
to `http://localhost/svg` returning the `SVG` file.

A sample client `sample-client.rkt` is included.

Building
========

    ./activator stage

Running
=======

    target/universal/stage/bin/latex2svg -Dhttp.port=9749 -Dhttp.address=127.0.0.1 -Dpidfile.path=/var/run/latex2svg.pid


#!/usr/bin/env python3

import httperfpy as HP
import subprocess as S
import urllib.parse as UP
import yaml

with open("perf_tests.yml", "r") as f:
    data = yaml.load(f.read())
    tests = data["tests"]
    servers = data["servers"]
    for s in servers:
        print ("Server:", s)
        for t in tests:
            print ("Path:", t['path'])
            qs = UP.urlencode(t["params"])
            urlparts = UP.urlparse(UP.urlunsplit(("http", s, t["path"], qs, "")))
            port = urlparts.port if urlparts.port is not None else 80
            perf = HP.Httperf(server=urlparts.hostname, port=port, uri=urlparts.path+"?"+urlparts.query)
            perf.parser=True # XXX: What does this do??
            res = perf.run()
            print(res['connection_time_median'])

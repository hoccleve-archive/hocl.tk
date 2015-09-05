#!/usr/bin/env python3

import httperfpy as HP
import urllib.parse as UP
import yaml


def main(argv):
    tests_yaml = "perf_tests.yml"
    if len(argv) > 1:
        tests_yaml = argv[1]

    with open(tests_yaml, "r") as f:
        data = yaml.load(f.read())
        tests = data["tests"]
        servers = data["servers"]
        for s in servers:
            print ("Server:", s)
            for t in tests:
                print ("Path:", t['path'])
                qs = UP.urlencode(t["params"])
                urlparts = UP.urlparse(
                    UP.urlunsplit(
                        ("http", s, t["path"], qs, "")))
                port = urlparts.port if urlparts.port is not None else 80
                perf = HP.Httperf(server=urlparts.hostname,
                                  port=port,
                                  uri=urlparts.path + "?" + urlparts.query,
                                  num_calls=50)
                perf.parser = True  # XXX: What does this do??
                res = perf.run()
                print('response_time', res['reply_time_response'])
                print('successful_replies', res['reply_status_2xx'])

if __name__ == "__main__":
    import sys
    main(sys.argv)

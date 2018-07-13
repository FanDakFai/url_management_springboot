#!/usr/bin/python

import requests
import requests.auth
import base64
import sys

mykey = sys.argv[1]

print mykey 

headers = {"Authorization": "Bearer " + mykey, "Content-Type": "application/x-www-form-urlencoded"}


response = requests.get("https://localhost:8082/domainnames", headers=headers, verify=False)
#print "[" + str(response.raw.read()) + "]"

json_result = response.json()
#print json_result


def decodeIpv4Address(binAddr):
    addr1 = binAddr >> 24
    if addr1 < 0:
        addr1 = 256 + addr1
    return str(addr1) + "." + str((binAddr >> 16) & 255) + "." + str((binAddr >> 8) & 255) + "." + str(binAddr & 255)

print "\n"
for jsonitem in json_result:
    print jsonitem[u'domainName'] + "\t->\t" + decodeIpv4Address(jsonitem[u'ipv4Address'])


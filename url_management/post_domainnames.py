#!/usr/bin/python

import requests
import requests.auth
import base64
import sys
import json
import os

try:
   mykey = os.environ["OAUTH2_TOKEN"]
except KeyError:
   mykey = ""

print mykey 

headers = {"Authorization": "Bearer " + mykey, "Content-Type": "application/json"}


postdata = {
    'domainName': "ubuntuhost",
    'ipv4Address': 129,
}

payload = json.dumps(postdata)

response = requests.post("https://localhost:8082/domainnames", headers=headers, verify=False, data=payload)

#print response.status_code
#print "[" + str(response.raw.read()) + "]"

print response.status_code
print "[" + str(response.raw.read()) + "]"

if response.status_code != 201:
    exit()

json_result = response.json()
#print json_result


def decodeIpv4Address(binAddr):
    addr1 = binAddr >> 24
    if addr1 < 0:
        addr1 = 256 + addr1
    return str(addr1) + "." + str((binAddr >> 16) & 255) + "." + str((binAddr >> 8) & 255) + "." + str(binAddr & 255)

print "\n"
#for jsonitem in json_result:
#    print jsonitem[u'domainName'] + "\t->\t" + decodeIpv4Address(jsonitem[u'ipv4Address'])

jsonitem = json_result
print jsonitem[u'domainName'] + "\t->\t" + decodeIpv4Address(jsonitem[u'ipv4Address'])


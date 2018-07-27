#!/usr/bin/python

import requests
import requests.auth
import base64
import sys
import os

try:
   mykey = os.environ["OAUTH2_TOKEN"]
except KeyError:
   mykey = ""

print mykey 

target_domain_name = None
if len(sys.argv) >= 2:
    target_domain_name = sys.argv[1]


headers = {"Authorization": "Bearer " + mykey, "Content-Type": "application/x-www-form-urlencoded"}

#endpoint_url = "https://localhost:8082/canonicaldomainnames"
endpoint_url = "https://localhost:8082/aliasdomainnames"

if target_domain_name is None:
    response = requests.get(endpoint_url, headers=headers, verify=False)
    #print "[" + str(response.raw.read()) + "]"
else:
    response = requests.get(endpoint_url + target_domain_name, headers=headers, verify=False)


if response.status_code == 404:
    print "NOT FOUND - empty list"
    exit();


json_result = response.json()
print json_result


def decodeIpv4Address(binAddr):
    addr1 = binAddr >> 24
    if addr1 < 0:
        addr1 = 256 + addr1
    return str(addr1) + "." + str((binAddr >> 16) & 255) + "." + str((binAddr >> 8) & 255) + "." + str(binAddr & 255)

print "\n"

#if target_domain_name is None:
#    for jsonitem in json_result:
#        print jsonitem[u'domainName'] + "\t->\t" + decodeIpv4Address(jsonitem[u'ipv4Address'])
#else:
#        print json_result[u'domainName'] + "\t->\t" + decodeIpv4Address(json_result[u'ipv4Address'])

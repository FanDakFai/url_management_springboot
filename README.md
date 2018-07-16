# INTRODUCTION
A domain name management REST api service. It supports:
* Let client do CRUD use cases on domain name object. The domain name object attributes are: domain name and ipv4address value.
* Let client do resolve domain name usecase to translate domain name to ipv4address. This is archived by:
    * Query for domain name object with provided domain name
    * If query results in no object found. It access google https REST apis to resolve

# GUIDANCE

Step to build and test this demo:
* [Create postgres database](#create-postgres-databse)
* [Build demo](#build-demo)
* [Run servers](#run-servers)
* [Run client test code](#run-client-test-code)

## Create postgres databse
* Create new demo user: (this could be skip if an available account exists)
    * Login as postgres: # su postgres
    * Enter command:     # createuser springbootdemo --no-createdb --encrypted --login --pwprompt --no-superuser 
        * take note your password then update file application.properties
        * make sure your pg_hba.conf configuration allow remote connection 
* Create demo databse
    * Launch psql:       # psql
    * Issue sql command: create database dns_over_https_db;
    * Allow user springbootdemo manipulate database springbootdemodb:
        GRANT ALL PRIVILEGES ON DATABASE dns_over_https_db to springbootdemo;

## Build demo
* Launch: # mvn package

## Run servers
* Run authentication server first: https://github.com/FanDakFai/oauth2_springboot/blob/master/README.md#run-client-test-code
* Launch: # java -jar target/url_management-0.0.1.jar

## Run client test code
* Run authentication server client test code to obtain access token: https://github.com/FanDakFai/oauth2_springboot/blob/master/README.md#run-client-test-code
* Create enviroment variable OAUTH2_TOKEN hold the token value: # export OAUTH2_TOKEN=dcc17b35-1cc1-405b-a53f-b384aeb4c50a
* For get all domain name objects launch: # ./get_domainnames.py
* For get domain name objects for domain name mygateway.local: # ./get_domainnames.py mygateway.local
* For post domain name object with value is harded code in source code variable: # ./post_domainnames.py
* For put (update) domain name object with value is harded code in source code variable:  # ./put_domainnames.py
* For delete domain name object with domain name mygateway.local:  # ./del_domainnames.py


# APPENDIX
T.B.D


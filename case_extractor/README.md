### How use script:
1) In config.txt file: 
  - CONTRACT_NUMBER= in this place should be located the contract number for the case looked for 
  - CONTAINER_ID= in this place should be located ID of container where postgres run (use `docker ps`)
  - PSQL_USER= in this place should be located username
  - PSQL_DB= in this place should be located name of DB
2) Use ./extract_case.sh command and all results will be in results directory, .zip should be transferred to EY

#### If password to DB required during execution -> go to step 3 and repeat step 1

3) If Connection to DB is not established in .sh OR password to DB required during execution:
   - verify ID of postgres container;
   - verify DB name and username;
   - If all correct but connection is not established:
     - run ./create_pgpass.sh,
     this script will create all what necessary to connect to DB, you should prepare next data to create connection: 
     PG_HOSTNAME, PG_PORT, PG_DBNAME, PG_USERNAME, PG_PASSWORD

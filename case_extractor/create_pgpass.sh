#!/bin/bash

# Prompt the user for PostgreSQL connection details
read -p "Enter PostgreSQL hostname: " PG_HOSTNAME
read -p "Enter PostgreSQL port: " PG_PORT
read -p "Enter PostgreSQL database name: " PG_DBNAME
read -p "Enter PostgreSQL username: " PG_USERNAME
read -sp "Enter PostgreSQL password: " PG_PASSWORD
echo # Add a new line after password prompt

# Write the connection details to the .pgpass file
echo "$PG_HOSTNAME:$PG_PORT:$PG_DBNAME:$PG_USERNAME:$PG_PASSWORD" > ~/.pgpass

# Set appropriate permissions for the .pgpass file
chmod 600 ~/.pgpass

echo ".pgpass file created successfully."

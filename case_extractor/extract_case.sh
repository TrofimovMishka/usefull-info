#!/bin/bash

# Function to print each letter from a string with a specified pause
print() {
    local text="$1"
    local pause_duration="${2:-0.05}"  # Default pause duration is 0.08 seconds

    for ((i=0; i<${#text}; i++)); do
        echo -n "${text:$i:1}"
        sleep "$pause_duration"
    done
    echo
}

# Read configuration file line by line
while IFS='=' read -r key value; do
    case $key in
        "CONTRACT_NUMBER") CONTRACT_NUMBER=$value ;;
        "CONTAINER_ID") CONTAINER_ID=$value ;;
        "PSQL_USER") PSQL_USER=$value ;;
        "PSQL_DB") PSQL_DB=$value ;;
    esac
done < config.txt

# Put in file contract number
VARIABLE='CONTRACT_NUMBER'
SQL_QUERY_FILE="data_query.sql"
sed -i "s/$VARIABLE/$CONTRACT_NUMBER/g" "$SQL_QUERY_FILE"

# Copy the SQL query file to the Docker container
docker cp "$SQL_QUERY_FILE" "$CONTAINER_ID:/tmp/data_query.sql"

# Replace in file contract number to placeholder
sed -i "s/$CONTRACT_NUMBER/$VARIABLE/g" "$SQL_QUERY_FILE"

# Create files where store query result
DATE_TIME=$(date +"%Y-%m-%d_%H-%M-%S")
FILE_NAME="_${CONTRACT_NUMBER}_${DATE_TIME}.csv"
FILE_NAME_AUD="_${CONTRACT_NUMBER}_${DATE_TIME}.csv"

mkdir -p results/"${DATE_TIME}"
mkdir -p results/"${DATE_TIME}"/aud

print "Making request to PostgreSQL..."

# Execute requests for data:
docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -f "/tmp/data_query.sql" > "results/${DATE_TIME}/case$FILE_NAME"

# Define table names
TABLES=("attachments" "client_interaction_events" "credit_data" "credit_decision_events" "negotiation_limits" "court_hearing_meetings" "notes" "knf_operations_history")

# Define SQL queries
for TABLE in "${TABLES[@]}"; do
    SQL_QUERY="\COPY (SELECT * FROM mediator.$TABLE x JOIN mediator.case_data cd ON cd.id = x.case_id WHERE cd.numer_kontraktu = '$CONTRACT_NUMBER') TO STDOUT WITH CSV HEADER"

    # Connect to PostgreSQL and execute the SQL query
    docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -c "$SQL_QUERY" > "results/${DATE_TIME}/${TABLE}$FILE_NAME"
done

# Execute requests for aud data:

docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -c "\COPY (select * from mediator.case_data_aud where numer_kontraktu = '$CONTRACT_NUMBER') TO STDOUT WITH CSV HEADER" > "results/${DATE_TIME}/aud/case_aud$FILE_NAME"

# Define aud table names
AUD_TABLES=("attachments_aud" "calculation_summaries_aud" "case_mediators_aud" "client_interaction_events_aud" "client_interaction_summary_aud" "court_hearing_meetings_aud" "credit_data_aud" "credit_decision_events_aud" "credit_decisions_aud" "documentation_data_aud" "finalization_parameters_aud" "knf_data_aud" "mediation_outcomes_aud" "negotiation_limits_aud" "notes_aud")
AUD_TABLES_FULL=("calculation_summaries_limit_negocjacyjny_aud" "client_interaction_summary_rozmowy_ids" "case_organization_attorneys_aud" "knf_mediators_aud" "organization_attorneys_aud")

for TAB in "${AUD_TABLES[@]}"; do
    SQL_QUERY_2="\COPY (SELECT * FROM mediator.$TAB where case_id = (select id from mediator.case_data WHERE numer_kontraktu = '$CONTRACT_NUMBER')) TO STDOUT WITH CSV HEADER"

    # Connect to PostgreSQL and execute the SQL query
    docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -c "$SQL_QUERY_2" > "results/${DATE_TIME}/aud/${TAB}$FILE_NAME_AUD"
done

for TAB_FULL in "${AUD_TABLES_FULL[@]}"; do
    SQL_QUERY_3="\COPY (SELECT * FROM mediator.$TAB_FULL) TO STDOUT WITH CSV HEADER"

    # Connect to PostgreSQL and execute the SQL query
    docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -c "$SQL_QUERY_3" > "results/${DATE_TIME}/aud/${TAB_FULL}$FILE_NAME_AUD"
done

# Get "client_data" and client_data_aud

SQL_QUERY_4="\COPY (SELECT id, create_date, creator_id, deleted, last_modify_change_date, last_modify_user_id, organisation_id, zgoda_email, case_id, id_klienta, klient_podpisal_oswiadczenie_pit, kredytobiorca_do_kontaktu FROM mediator.client_data where case_id = (select id from mediator.case_data WHERE numer_kontraktu = '$CONTRACT_NUMBER')) TO STDOUT WITH CSV HEADER"
docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -c "$SQL_QUERY_4" > "results/${DATE_TIME}/client_data$FILE_NAME_AUD"

SQL_QUERY_5="\COPY (SELECT rev, revtype, id, create_date, creator_id, deleted, last_modify_change_date, last_modify_user_id, organisation_id, zgoda_email, case_id, id_klienta, klient_podpisal_oswiadczenie_pit, kredytobiorca_do_kontaktu FROM mediator.client_data_aud where case_id = (select id from mediator.case_data WHERE numer_kontraktu = '$CONTRACT_NUMBER')) TO STDOUT WITH CSV HEADER"
docker exec -i "$CONTAINER_ID" psql -U "$PSQL_USER" -d "$PSQL_DB" -c "$SQL_QUERY_5" > "results/${DATE_TIME}/aud/client_data_aud$FILE_NAME_AUD"

# Remove the SQL query file from /tmp after operation
docker exec "$CONTAINER_ID" rm "/tmp/data_query.sql"

zip -r "results/${DATE_TIME}.zip" "results/${DATE_TIME}"

echo "Row results located in directory results/${DATE_TIME}"
echo "ZIP results located in directory results/${DATE_TIME}.zip"

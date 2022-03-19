#!/bin/bash

echo "Starting the frontend..."

profile="$(printenv profile_env)"
echo "$profile"

if [ "$profile" == "local" ]
then
    echo "Starting the local environment..."
    yarn start
elif [ "$profile" == "dev" ]
then
    echo "Starting the dev environment..."
    yarn build
else
    echo "environment not supported"
    exit
fi

echo "end"

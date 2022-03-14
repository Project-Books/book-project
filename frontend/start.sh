#!/bin/bash

echo "Starting the frontend..."

# TODO: check which environment we should run in
echo "$1"

if [ "$1" == "local" ]
then
    echo "local env"
    yarn start
elif [ "$1" == "dev" ]
then
    echo "dev env"
    yarn build
fi


echo "end"

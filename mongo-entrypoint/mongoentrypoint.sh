#!/usr/bin/env bash
echo "Creating mongo users..."
mongosh --authenticationDatabase admin --host localhost -u mongoadmin -p mongopasswd expertsconseil --eval "db.createUser({user: 'devUser', pwd: 'devUserPass', roles: [{role: 'readWrite', db: 'expertsconseil'}]});"
echo "Mongo users created."

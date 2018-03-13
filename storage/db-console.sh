#!/bin/bash

cd $(dirname $0)

docker run --rm -ti \
    --link marker-mysqld:mysql \
    mysql:8.0 \
    sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -u"$MYSQL_ENV_MYSQL_USER" -p"$MYSQL_ENV_MYSQL_PASSWORD" $MYSQL_ENV_MYSQL_DATABASE'

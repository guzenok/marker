#!/bin/bash

cd $(dirname $0)


docker run -d --rm --name marker-mysqld \
    -p 3306:3306 \
    -e MYSQL_USER=marker \
    -e MYSQL_PASSWORD=zfkdg8jdfWyr7wyr \
    -e MYSQL_DATABASE=marker \
    -e MYSQL_RANDOM_ROOT_PASSWORD=true \
    -u `id -u`:`id -g` \
    -v ${PWD}/mysqlDB:/var/lib/mysql \
    mysql:5.7.21 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
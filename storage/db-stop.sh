#!/bin/bash

cd $(dirname $0)

docker container stop marker-mysqld

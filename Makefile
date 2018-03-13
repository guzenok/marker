.PHONY: all build clean


all: build


push:
	`aws --profile test ecr get-login --no-include-email --region us-west-2 --registry-ids 446211518857`
	docker tag marker-server:latest 446211518857.dkr.ecr.us-west-2.amazonaws.com/test/marker-server:latest
	docker push 446211518857.dkr.ecr.us-west-2.amazonaws.com/test/marker-server:latest


build: server/build/libs/marker-server-0.1.0.jar
	docker build -t marker-server -f server/Dockerfile server


server/build/libs/marker-server-0.1.0.jar:
	cd server && ./gradlew build


clean:
	git clean -f -X


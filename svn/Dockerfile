FROM ubuntu:latest

RUN apt-get update && apt-get install -y subversion

RUN apt-get update && \
    apt-get install -y net-tools && \
    apt-get install -y iputils-ping

WORKDIR /scripts
ENTRYPOINT ["./batch-svn-info.sh"]
# ENTRYPOINT ["./scripts/get-last-svn-commit.sh"]
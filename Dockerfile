FROM ubuntu:latest
LABEL authors="FPT"

ENTRYPOINT ["top", "-b"]
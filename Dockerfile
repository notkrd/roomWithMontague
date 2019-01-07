hseeberger/scala-sbt:8u151-2.12.5-1.1.2

WORKDIR /app

COPY . /app

RUN sbt build

CMD ["sbt run"]

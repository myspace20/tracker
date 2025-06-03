
ARG BUILD_HOME=/tracker

FROM gradle:jdk24 AS build-image


ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME


COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src


RUN gradle --no-daemon build --scan


FROM eclipse-temurin:24-jdk-alpine


ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
COPY --from=build-image $APP_HOME/build/libs/*.jar app.jar


ENTRYPOINT ["java","-jar","/app.jar"]
# Слой для сборки
FROM maven:3.8.4-openjdk-17 AS build

# Установите рабочую директорию
WORKDIR /app

# Скопируйте файлы pom.xml обоих проектов
COPY OrderService/pom.xml OrderService/pom.xml
COPY MarketPlace/pom.xml MarketPlace/pom.xml

# Скопируйте исходный код обоих проектов
COPY OrderService/src OrderService/src
COPY MarketPlace/src MarketPlace/src

# Соберите оба проекта
RUN mvn -f OrderService/pom.xml clean package -DskipTests
RUN mvn -f MarketPlace/pom.xml clean package -DskipTests

# Финальный образ
FROM openjdk:17

# Установите рабочую директорию
WORKDIR /app

# Скопируйте собранные jar файлы из предыдущего слоя
COPY --from=build /app/OrderService/target/OrderService-0.0.1-SNAPSHOT.jar OrderService.jar
COPY --from=build /app/MarketPlace/target/MarketPlace-0.0.1-SNAPSHOT.jar MarketPlace.jar

# Скопируйте директорию ресурсов (если используется Thymeleaf)
COPY --from=build /app/MarketPlace/src/main/resources /app/resources

# Запустите оба приложения
CMD ["sh", "-c", "java -jar OrderService.jar & java -jar MarketPlace.jar"]